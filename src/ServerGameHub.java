//Networked Scrabble
//Created by Chris Miles and Sean Harger
//Loyola High School class of 2011
import java.util.ArrayList;
import java.io.*;
import java.net.*;
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

public class ServerGameHub implements ActionListener
   {
   private GameBoard gameBoard;
   private ArrayList<Tile> gameTiles; 
   private ArrayList<Player> players; 
   private ArrayList<ClientReader> readers;
   private int activePlayerIndex;

   private ServerSocket server;
   private ArrayList<Socket> clients;
   private ArrayList<ObjectOutputStream> outputStreams;

   private JFrame serverFrame;
   private JPanel serverPanel;
   private JButton startButton;
   private JButton terminateButton;
   private JLabel clientNumLabel;
   private JLabel serverIPLabel;
   private final Integer[] clientNums = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14 };
   private JComboBox clientNumOptions;

   private int numClients;

   private static final int NUM_A = 9;
   private static final int NUM_B = 2;
   private static final int NUM_C = 2;
   private static final int NUM_D = 4;
   private static final int NUM_E = 12;
   private static final int NUM_F = 2;
   private static final int NUM_G = 3;
   private static final int NUM_H = 2;
   private static final int NUM_I = 9;
   private static final int NUM_J = 1;
   private static final int NUM_K = 1;
   private static final int NUM_L = 4;
   private static final int NUM_M = 2;
   private static final int NUM_N = 6;
   private static final int NUM_O = 8;
   private static final int NUM_P = 2;
   private static final int NUM_Q = 1;
   private static final int NUM_R = 6;
   private static final int NUM_S = 4;
   private static final int NUM_T = 6;
   private static final int NUM_U = 4;
   private static final int NUM_V = 2;
   private static final int NUM_W = 2;
   private static final int NUM_X = 1;
   private static final int NUM_Y = 2;
   private static final int NUM_Z = 1;
   private static final int NUM_OMNI = 2;

   private static final int PTS_A = 1;
   private static final int PTS_B = 3;
   private static final int PTS_C = 3;
   private static final int PTS_D = 2;
   private static final int PTS_E = 1;
   private static final int PTS_F = 4;
   private static final int PTS_G = 2;
   private static final int PTS_H = 4;
   private static final int PTS_I = 1;
   private static final int PTS_J = 8;
   private static final int PTS_K = 5;
   private static final int PTS_L = 1;
   private static final int PTS_M = 3;
   private static final int PTS_N = 1;
   private static final int PTS_O = 1;
   private static final int PTS_P = 3;
   private static final int PTS_Q = 10;
   private static final int PTS_R = 1;
   private static final int PTS_S = 1;
   private static final int PTS_T = 1;
   private static final int PTS_U = 1;
   private static final int PTS_V = 4;
   private static final int PTS_W = 4;
   private static final int PTS_X = 8;
   private static final int PTS_Y = 4;
   private static final int PTS_Z = 10;

   public ServerGameHub()
      {
      gameBoard = new GameBoard();
      gameTiles = new ArrayList<Tile>();
      players = new ArrayList<Player>();
      clients = new ArrayList<Socket>();
      readers = new ArrayList<ClientReader>();
      outputStreams = new ArrayList<ObjectOutputStream>();

      setUpGUI();
      } //end constructor Game
   
   public void setUpGUI()
      {
      serverFrame = new JFrame( "Scrabble Server" );
      serverFrame.setSize( 200, 200 );
      serverFrame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
      serverFrame.setLayout( null );
      serverFrame.setResizable( false );

      serverPanel = new JPanel();
      serverPanel.setSize( 200, 200 );
      serverFrame.add( serverPanel );

      clientNumLabel = new JLabel( "Players: " );
      
      String ip = new String();
      try
         {
         InetAddress serverAddress = InetAddress.getLocalHost(); 
         ip = serverAddress.getHostAddress();
         } 
      catch( Exception ex ) 
         {
         }
      serverIPLabel = new JLabel( "Server Address: " + ip );

      clientNumOptions = new JComboBox( clientNums );

      startButton = new JButton( "start server" );
      startButton.addActionListener( this );
      startButton.setLocation( ( serverFrame.getWidth() - startButton.getWidth() ) / 2, 100 );
      startButton.setEnabled( true );

      terminateButton = new JButton( "terminate server" );
      terminateButton.addActionListener( this );
      terminateButton.setLocation( ( serverFrame.getWidth() - terminateButton.getWidth() ) / 2, 200 );
      terminateButton.setEnabled( false );

      serverPanel.add( clientNumLabel );
      serverPanel.add( clientNumOptions );
      serverPanel.add( startButton );
      serverPanel.add( terminateButton );
      serverPanel.add( serverIPLabel );

      serverFrame.setVisible( true );
      } //end method setUpGUI

   public void actionPerformed( ActionEvent e )
      {
      if( e.getActionCommand().equals( "start server" ) )
         {
         try
            {
            startGame();
            startButton.setEnabled( false );
            terminateButton.setEnabled( true );
            } //end try
         catch( Exception ex )
            { 
            System.out.println( ex );
            } //end catch
         } //end if
      if( e.getActionCommand().equals( "terminate server" ) )
         {
         System.exit(0);
         } //end if
      } //end method actionPerformed

   public void startGame() throws Exception
      {
      numClients = (Integer) clientNumOptions.getSelectedItem();
      server = new ServerSocket( 3000 );
      System.out.println( "Server listening on port 3000." );
      waitForConnections();

      for( int index = 0; index < clients.size(); index++ )
         {
         outputStreams.add( new ObjectOutputStream( clients.get( index ).getOutputStream() ) );
         readers.add( new ClientReader( this, clients.get( index ) ) );
         readers.get( index ).start();
         } //end for

      activePlayerIndex = 0;

      constructTiles();
      distributeTiles();

      sendDisplayMessage();
      } //end method startGam

   public void advancePlayer()
      {
      if( activePlayerIndex == numClients - 1 )
         {
         activePlayerIndex = 0;
         } //end if
      else
         {
         activePlayerIndex++;
         } //end else
      } //end method advancePlayer

   public void processMoveRequest( MoveRequest mr )
      {   
      System.out.println( "processMoveRequest called" );
      for( Tile t: mr.getTiles() )
         {
         int index = mr.getTiles().indexOf( t );
         gameBoard.add( t, mr.getLocations().get( index ) );
         } //end for
      try //why is this a try-catch block????
         {
         gameBoard.allCorrect( mr );
         }
      catch( Exception ex )
         {
         System.out.println( ex );
         }
      boolean allCorrect = gameBoard.allCorrect( mr );
      if( !allCorrect )
         {
         System.out.println( "something is wrong here" );
         for( Tile t: mr.getTiles() )
            {
            //int index = mr.getTiles().indexOf( t );
            gameBoard.remove( t );
            } //end for
         } //end if
      else
         {  
         System.out.println( "all are correct" );
         scoreTiles( mr );
         removeTiles( mr.getTiles() );
         gameBoard.setIsFirstMove( false );
         distributeTiles();
         advancePlayer();
         }
      
      printStatus();
      sendMoveConfirmation( allCorrect );
      sendDisplayMessage();
      } //end method processMoveRequest

   public void scoreTiles( MoveRequest mr)
      {
      for( Player p: players )
         {
         if( p.getIP().equals( mr.getPlayerIP() ) )
            {
            p.addScore( gameBoard.scoreWords( mr ) );
            } //end if
         } //end for
      } //end method scoreTiles
   
   public Location getTopLeft( ArrayList<Location> locs )
      {
      Location topLeft = locs.get( 0 );
      for( Location l : locs )
         {
         if( l.getX() < topLeft.getX() || l.getY() < topLeft.getY() )
            {
            topLeft = l;
            } //end if
         } //end for
      
      return topLeft;
      }

   public void waitForConnections() 
      {
      while( clients.size() < numClients )
         {
         try 
            {
            System.out.println( "Waiting for connections." );
            Socket client = server.accept();
            System.out.println( "Accepted a connection from: " + client.getInetAddress() );
            clients.add( client );  
            } //end try
         catch(Exception e) 
            {
            } //end catch
         } //end while
      for( Socket s: clients )
         {  
         players.add( new Player( s.getInetAddress().getHostAddress() ) );
         } //end for
      System.out.println( players );
      System.out.println( "Finished waiting" );
      } //end method waitForConnections

   public void sendDisplayMessage()
      {
      System.out.println( "send display message called" );
      DisplayMessage msg = new DisplayMessage( players.get( activePlayerIndex ), gameBoard, players );
      try
         {
         for( ObjectOutputStream oos: outputStreams )
            {
            System.out.println( msg );
            oos.writeObject( msg );  
            System.out.println( "sent display message" ); 
            oos.reset();
            } //end for
         } //end try
      catch( Exception ex )
         {
         System.out.println( ex );
         } //end catch
      } //end method sendDisplayMessage

   public void sendMoveConfirmation( boolean isCorrect )
      {
      System.out.println( "send move confirmation called" );
      MoveConfirmation msg = new MoveConfirmation( isCorrect );
      try
         {
         for( ObjectOutputStream oos: outputStreams )
            {
            oos.writeObject( msg );  
            System.out.println( "sent confirmation message" ); 
            oos.reset();
            } //end for
         } //end try
      catch( Exception ex )
         {
         } //end catch
      } //end method sendMoveConfirmation

   public void constructTiles()
      {
      int idCounter = 0;
      for( int counter = 0; counter < NUM_A; counter++, idCounter++ )
         {
         gameTiles.add( new FixedTile( idCounter, "A", PTS_A ) );
         } //end for
      for( int counter = 0; counter < NUM_B; counter++, idCounter++ )
         {
         gameTiles.add( new FixedTile( idCounter, "B", PTS_B ) );
         } //end for
      for( int counter = 0; counter < NUM_C; counter++, idCounter++ )
         {
         gameTiles.add( new FixedTile( idCounter, "C", PTS_C ) );
         } //end for
      for( int counter = 0; counter < NUM_D; counter++, idCounter++ )
         {
         gameTiles.add( new FixedTile( idCounter, "D", PTS_D ) );
         } //end for
      for( int counter = 0; counter < NUM_E; counter++, idCounter++ )
         {
         gameTiles.add( new FixedTile( idCounter, "E", PTS_E ) );
         } //end for
      for( int counter = 0; counter < NUM_F; counter++, idCounter++ )
         {
         gameTiles.add( new FixedTile( idCounter, "F", PTS_F ) );
         } //end for
      for( int counter = 0; counter < NUM_G; counter++, idCounter++ )
         {
         gameTiles.add( new FixedTile( idCounter, "G", PTS_G ) );
         } //end for
      for( int counter = 0; counter < NUM_H; counter++, idCounter++ )
         {
         gameTiles.add( new FixedTile( idCounter, "H", PTS_H ) );
         } //end for
      for( int counter = 0; counter < NUM_I; counter++, idCounter++ )
         {
         gameTiles.add( new FixedTile( idCounter, "I", PTS_I ) );
         } //end for
      for( int counter = 0; counter < NUM_J; counter++, idCounter++ )
         {
         gameTiles.add( new FixedTile( idCounter, "J", PTS_J ) );
         } //end for
      for( int counter = 0; counter < NUM_K; counter++, idCounter++ )
         {
         gameTiles.add( new FixedTile( idCounter, "K", PTS_K ) );
         } //end for
      for( int counter = 0; counter < NUM_L; counter++, idCounter++ )
         {
         gameTiles.add( new FixedTile( idCounter, "L", PTS_L ) );
         } //end for
      for( int counter = 0; counter < NUM_M; counter++, idCounter++ )
         {
         gameTiles.add( new FixedTile( idCounter, "M", PTS_M ) );
         } //end for
      for( int counter = 0; counter < NUM_N; counter++, idCounter++ )
         {
         gameTiles.add( new FixedTile( idCounter, "N", PTS_N ) );
         } //end for
      for( int counter = 0; counter < NUM_O; counter++, idCounter++ )
         {
         gameTiles.add( new FixedTile( idCounter, "O", PTS_O ) );
         } //end for
      for( int counter = 0; counter < NUM_P; counter++, idCounter++ )
         {
         gameTiles.add( new FixedTile( idCounter, "P", PTS_P ) );
         } //end for
      for( int counter = 0; counter < NUM_Q; counter++, idCounter++ )
         {
         gameTiles.add( new FixedTile( idCounter, "Q", PTS_Q ) );
         } //end for
      for( int counter = 0; counter < NUM_R; counter++, idCounter++ )
         {
         gameTiles.add( new FixedTile( idCounter, "R", PTS_R ) );
         } //end for
      for( int counter = 0; counter < NUM_S; counter++, idCounter++ )
         {
         gameTiles.add( new FixedTile( idCounter, "S", PTS_S ) );
         } //end for
      for( int counter = 0; counter < NUM_T; counter++, idCounter++ )
         {
         gameTiles.add( new FixedTile( idCounter, "T", PTS_T ) );
         } //end for
      for( int counter = 0; counter < NUM_U; counter++, idCounter++ )
         {
         gameTiles.add( new FixedTile( idCounter, "U", PTS_U ) );
         } //end for
      for( int counter = 0; counter < NUM_V; counter++, idCounter++ )
         {
         gameTiles.add( new FixedTile( idCounter, "V", PTS_V ) );
         } //end for
      for( int counter = 0; counter < NUM_W; counter++, idCounter++ )
         {
         gameTiles.add( new FixedTile( idCounter, "W", PTS_W ) );
         } //end for
      for( int counter = 0; counter < NUM_X; counter++, idCounter++ )
         {
         gameTiles.add( new FixedTile( idCounter, "X", PTS_X ) );
         } //end for
      for( int counter = 0; counter < NUM_A; counter++, idCounter++ )
         {
         gameTiles.add( new FixedTile( idCounter, "Y", PTS_Y ) );
         } //end for
      for( int counter = 0; counter < NUM_Z; counter++, idCounter++ )
         {
         gameTiles.add( new FixedTile( idCounter, "Z", PTS_Z ) );
         } //end for
      for( int counter = 0; counter < NUM_OMNI; counter++, idCounter++ )
         {
         gameTiles.add( new OmniTile( idCounter ) );
         } //end for
      } //end method constructTiles

   public void distributeTiles()
      {
      System.out.println( "Distribute tiles called" );
      for( Player p: players )
         {
         int numTiles = p.getTiles().size();
         while( numTiles < 7 )
            {
            int randomNum = (int)( Math.random() * gameTiles.size() );
            
            Tile pickedTile = gameTiles.get( randomNum );
            gameTiles.remove( pickedTile );
            p.addTile( pickedTile );
            System.out.println( "added a tile" );  

            numTiles = p.getTiles().size();
            } //end while
         } //end for
      System.out.println( "Distribute tiles complete" );
      } //end method distributeTiles

   public void removeTiles( ArrayList<Tile> tilesToRemove )
      {
      System.out.println( "Remove tiles called" );

      for( Player p: players )
         {
         ArrayList<Tile> removeThese = new ArrayList<Tile>();
         ArrayList<Tile> myTiles = p.getTiles();
         for( Tile mT: myTiles )
            {
            for( Tile t: tilesToRemove )
               {
               if( t.equals( mT ) )
                  {
                  removeThese.add( mT );
                  } //end if
               } //end for 
            } //end for
         for( Tile removeThis: removeThese )
            {
            p.removeTile( removeThis );
            System.out.println( "removed a tile " + removeThis );
            } //end for
         } //end for
      System.out.println( "Remove tiles complete" );
      } //end method updateTiles

   public GameBoard getBoard()
      {
      return gameBoard;
      } //end method getBoard

   public ArrayList<Tile> getTiles()
      {
      return gameTiles;
      } //end method getTiles

   public void printStatus()
      {
      System.out.println( gameBoard );
      for( Player p: players )
         {
         System.out.println( "Player " + players.indexOf( p ) + ": " + p.getTiles() + "  Score: " + p.getScore() );
         System.out.println( p );
         } //end for
      } //end method printStatus
   } //end class ServerGameHub

class ClientReader extends Thread
   {
   Socket sock;
   ObjectInputStream ois;
   ServerGameHub serverHub;

   public ClientReader( ServerGameHub sRef, Socket s )
      {
      serverHub = sRef;
      sock = s;
      try
         {
         ois = new ObjectInputStream( sock.getInputStream() );
         } //end try
      catch( Exception ex )
         {
         } //end catch
      } //end constructor ClientReader

    //reads messages from clients
   public void run() 
      {
      while( true )
         {
         try 
            {
            MoveRequest clientRequest = (MoveRequest) ois.readObject();
              
            serverHub.processMoveRequest( clientRequest );
            } //end try
         catch(Exception e)
            {
            } //end catch
         try
            {
            Thread.sleep( 100 );
            }
         catch( Exception ex )
            {
            } 
         } //end while
      } //end method run
   } //end class ClientReader