//Networked Scrabble
//Created by Chris Miles and Sean Harger
//Loyola High School class of 2011
import java.io.*;
import java.net.*;
import java.util.ArrayList;

public class ClientGameHub extends Thread
   {
   private GUI guiRef;
   private String myIP;
   private String serverIP;
   private int serverPort;
   private GameBoard gameBoard;
   private ArrayList<Player> players;
   private boolean scoreBoardSetUp;

   private ObjectOutputStream oos;
   private ObjectInputStream ois;
   private Socket socket;

   public ClientGameHub( GUI g, String sIP, String sPort  )
      {
      scoreBoardSetUp = false;
      guiRef = g;
      myIP = new String("127.0.0.1");
      try
         {
         InetAddress self = InetAddress.getLocalHost(); 
         myIP += self.getHostAddress();
         }
      catch( Exception ex )
         {
         System.out.println( "Could not determine local IP address" );
         } 
      serverIP = sIP;
      serverPort = Integer.parseInt( sPort );
   
      try
         {
         socket = new Socket( serverIP, serverPort );
         oos = new ObjectOutputStream(socket.getOutputStream());
         ois = new ObjectInputStream(socket.getInputStream());
         } //end try
      catch( ConnectException ex )
         {
         System.exit( 0 );
         } //end catch
      catch( Exception ex )
         {
         } //end catch

      this.start();
      } //end constructor ClientGameHub

   public void sendMoveRequest( MoveRequest msg )
      {
      try 
         {
         oos.writeObject( msg );
         oos.reset();
         } //end try
      catch(Exception e) 
         {
         } //end catch
      } //end method network

   //receives messages from the server
   public void run() 
      {
      while( true )
         {
         try 
            {
            Message msg = (Message) ois.readObject();
            if( msg.getMessageType().equals( "DisplayMessage" ) )
               {
               DisplayMessage dM = (DisplayMessage) msg;
               gameBoard = dM.getBoard();
               players = dM.getPlayers();
               if( !scoreBoardSetUp )
                  {
                  guiRef.setUpScoreBoard();
                  scoreBoardSetUp = true;
                  } //end if
               guiRef.setActivePlayer( dM.getActivePlayer() );
               guiRef.updateScoreBoard();
               guiRef.update();
               } //end if
            if( msg.getMessageType().equals( "MoveConfirmation" ) )
               {
               MoveConfirmation mC = (MoveConfirmation) msg;
               System.out.println( mC.isCorrect() );
               guiRef.resetMoveCandidates();
               //if false, remove from board and place back into player's rack
               //if true
               } //end if
            ois.reset();
            } //end try
         catch(Exception ex)
            {
            } //end catch
         try
            {
            Thread.sleep( 100 );
            } //end try
         catch( Exception ex )
            {
            } //end catch
         } //end while
      }

   public String getMyIP()
      {
      return myIP;
      } //end method getMyIP

   public GameBoard getBoard()
      {
      return gameBoard;
      } //end method getBoard
  
   public ArrayList<Tile> getMyTiles()
      {
      for( Player p: getPlayers() )
         {
         if( p.getIP().equals( myIP ) )
            {
            return p.getTiles();
            }
         } //end for

      return getPlayers().get( 0 ).getTiles();
      } //end method getTiles

   public ArrayList<Player> getPlayers()
      {
      return players;
      } //end method getTiles
   } //end class ClientGameHub