//Networked Scrabble
//Created by Chris Miles and Sean Harger
//Loyola High School class of 2011
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.util.ArrayList;

public class GUI implements MouseListener, MouseMotionListener, ActionListener
   {
   private ClientGameHub clientHub;
   private JFrame gameFrame;
   private JFrame controlFrame;
   private DrawPanel gameDrawPanel;
   private ArrayList<JLabel> playerScores;
   private JButton makeMoveButton;
   private Font myFont24;
   private FontMetrics fontMetrics24;
   private Tile selectedTile;
   private Location selectedTileLoc;
   private ArrayList<Tile> moveCandidates;
   private ArrayList<Location> moveCandidateLocs;

   private static final int TILE_LENGTH = 30;
   private static final int TILE_HEIGHT = 30;

   private static final Location[] myTilePixelLocs = { new Location( 527, 156 ), new Location( 527, 187 ), new Location( 527, 218 ), new Location( 527, 249 ),
                                                       new Location( 527, 280 ), new Location( 527, 311 ), new Location( 527, 342 ) };

   public GUI()
      {
      String serverIP = new String( JOptionPane.showInputDialog( gameFrame, "Enter server IP address:", "Connect to Server", JOptionPane.QUESTION_MESSAGE ) );
      clientHub = new ClientGameHub( this, serverIP, "3000" );

      moveCandidates = new ArrayList<Tile>();
      moveCandidateLocs = new ArrayList<Location>();

      playerScores = new ArrayList<JLabel>();
      } 

   public void go()
      {
      gameFrame = new JFrame( "Scrabble" );
      gameFrame.setSize( 526, 600 );
      gameFrame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
      gameFrame.setResizable( false );
      gameFrame.setFocusable( true );

      gameDrawPanel = new DrawPanel();
      gameDrawPanel.setSize( 526, 600 );

      gameFrame.add( gameDrawPanel, BorderLayout.CENTER );

      gameDrawPanel.addMouseListener( this );
      gameDrawPanel.addMouseMotionListener( this );

      controlFrame = new JFrame( "Controller" );
      controlFrame.setSize( 200, 200 );
      controlFrame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
      controlFrame.setLayout( null );
      controlFrame.setResizable( true );
      controlFrame.setLocation( 527, 0 );

      makeMoveButton = new JButton( "Make move" );
      makeMoveButton.setSize( makeMoveButton.getPreferredSize() );
      makeMoveButton.setLocation( ( controlFrame.getWidth() - makeMoveButton.getWidth() ) / 2, 25 );
      makeMoveButton.addActionListener( this );
      makeMoveButton.setEnabled( true );       

      controlFrame.add( makeMoveButton );

      gameFrame.setVisible( true );
      controlFrame.setVisible( true );
      } //end method go

   public void update()
      {
      gameDrawPanel.repaint();
      } //end method update

   public void setUpScoreBoard()
      {
      for( int index = 0; index < clientHub.getPlayers().size(); index++ )
         {
         Player p = clientHub.getPlayers().get( index );
         JLabel l = new JLabel();
         l.setText( p.getIP() + " : " + p.getScore() );
         l.setSize( l.getPreferredSize() );
         l.setLocation( 50, 70 + index * 20 );
         playerScores.add( l );
         } //end for
      for( JLabel l: playerScores )
         {
         controlFrame.add( l );
         } //end for 
      } //end method setUpScoreBoard

   public void updateScoreBoard()
      {
      for( int index = 0; index < clientHub.getPlayers().size(); index++ )
         {
         Player p = clientHub.getPlayers().get( index );
         JLabel l = playerScores.get( index );
         l.setText( p.getIP() + " : " + p.getScore() );
         l.setSize( l.getPreferredSize() );
         } //end for
      } //end method updateScoreBoard

   public void assignOmniTile( OmniTile t )
      { 
      boolean valid = false;  
      while( !valid )
         {
         t.setLetter( new String(JOptionPane.showInputDialog( gameFrame, "Please enter a letter for this tile", "OmniTile Assignment", JOptionPane.QUESTION_MESSAGE ).toUpperCase()) );
         if( !(t.toString().toUpperCase().equals(t.toString().toLowerCase() )) && t.toString().length() == 1)
            {
            valid = true;
            }
         }
      System.out.println( "The tile's letter is " +  t );
      } //end method assignOmniTile

   public void setActivePlayer( Player p )
      {
      if( p.getIP().equals( clientHub.getMyIP() ) )
         {
         makeMoveButton.setEnabled( true );
         } //end if
      else
         {
         makeMoveButton.setEnabled( false );
         } //end else
      
      int activePlayerIndex = clientHub.getPlayers().indexOf( p );
      playerScores.get( activePlayerIndex ).setForeground( Color.RED );
      for( JLabel l: playerScores )
         {
         if( playerScores.indexOf( l ) != activePlayerIndex )
            {
            l.setForeground( Color.BLACK );
            } //end if
         } //end for
      } //end method setTurn

   public void resetMoveCandidates()
      {
      moveCandidates.clear();
      moveCandidateLocs.clear();
      } //end method resetMoveCandidates

   public void mouseClicked( MouseEvent e )
      {
      } //end method mouseClicked

   public void mouseEntered( MouseEvent e )
      {
      } //end method mouseEntered

   public void mouseExited( MouseEvent e )
      {
      } //end method mouseExited

   public void mousePressed( MouseEvent e )
      {
      if( makeMoveButton.isEnabled() )
         {
         selectedTile = null;
         selectedTileLoc = null;
         for( Tile t: clientHub.getMyTiles() )
            {
            if( moveCandidates.indexOf( t ) == -1 )
               {
               int tileIndex = clientHub.getMyTiles().indexOf( t );
               Location tilePixelLoc = myTilePixelLocs[ tileIndex ];
               if( e.getX() >= tilePixelLoc.getX() && e.getX() < tilePixelLoc.getX() + TILE_LENGTH && e.getY() >= tilePixelLoc.getY() && e.getY() < tilePixelLoc.getY() + TILE_HEIGHT )
                  {
                  selectedTile = t;
                  selectedTileLoc = new Location( e.getY() - TILE_HEIGHT/2, e.getX() - TILE_LENGTH/2 );
                  } //end if
               } //end if
            } //end for
         for( Tile t: moveCandidates )
            {
            int tileIndex = moveCandidates.indexOf( t );
            Location tileCoordinateLoc = moveCandidateLocs.get( tileIndex );
            Location tilePixelLoc = tileCoordinateLoc.toPixels();
            if( e.getX() >= tilePixelLoc.getX() && e.getX() < tilePixelLoc.getX() + TILE_LENGTH && e.getY() >= tilePixelLoc.getY() && e.getY() < tilePixelLoc.getY() + TILE_HEIGHT )
               {
               selectedTile = t;
               selectedTileLoc = new Location( e.getY() - TILE_HEIGHT/2, e.getX() - TILE_LENGTH/2 );
               } //end if
            } //end for
         } //end if
      } //end method mousePressed

   public void mouseMoved( MouseEvent e )
      {
      } //end method mouseMoved   

   public void mouseReleased( MouseEvent e )
      {
      //when tile is being dropped from the user's collection into the board
      if( selectedTile != null && selectedTileLoc.getX() >= 16 && selectedTileLoc.getX() <= 495 && selectedTileLoc.getY() >= 16 && selectedTileLoc.getY() <= 495 )
         {
         int shortestDistance = 30;
         int closestBoxX = 0;
         int closestBoxY = 0;

         for( int boxYIndex = 0; boxYIndex < 15; boxYIndex++ )
            {
            for( int boxXIndex = 0; boxXIndex < 15; boxXIndex++ )
               {
               Location boxCoordinateLocation = new Location( boxYIndex, boxXIndex );
               Location boxPixelLocation = boxCoordinateLocation.toPixels();
               int boxX = boxPixelLocation.getX();
               int boxY = boxPixelLocation.getY();
      
               int distance = ( (int) Math.sqrt( Math.pow( boxX - selectedTileLoc.getX(), 2 ) + Math.pow( boxY - selectedTileLoc.getY(), 2 ) ) );
               if( distance < shortestDistance )
                  {
                  shortestDistance = distance;
                  closestBoxX = boxXIndex;
                  closestBoxY = boxYIndex;
                  } //end if
               } //end for
            } //end for

         boolean alreadyPlaced = false;
         for( Location l: moveCandidateLocs )
            {
            if( l.getX() == closestBoxX && l.getY() == closestBoxY )
               {
               alreadyPlaced = true;
               } //end if
            } //end for

         if( !alreadyPlaced )
            {
            if( moveCandidates.indexOf( selectedTile ) != -1 )
               {
               moveCandidateLocs.remove( moveCandidates.indexOf( selectedTile ) );
               moveCandidates.remove( selectedTile );
               } //end if

            moveCandidates.add( selectedTile );
            moveCandidateLocs.add( new Location( closestBoxY, closestBoxX ) );

            if( selectedTile instanceof OmniTile )
               {
               update();
               assignOmniTile( (OmniTile) selectedTile );
               } //end if

            System.out.println( moveCandidates );
            System.out.println( moveCandidateLocs ); 
            } //end if 
         } //end if
      
      //when tile is being replaced in the user's collection from a spot on the board
      if( selectedTile != null && selectedTileLoc.getX() >= 141 && selectedTileLoc.getX() <= 371 && selectedTileLoc.getY() >= 512 && selectedTileLoc.getY() <= 556 )
         {
         int shortestDistance = 30;
         int closestBoxX = 0;
         int closestBoxY = 0;
         
         for( int boxXIndex = 0; boxXIndex < 7; boxXIndex++ )
            {
            Location boxPixelLocation = myTilePixelLocs[ boxXIndex ];
            int boxX = boxPixelLocation.getX();
            int boxY = boxPixelLocation.getY();
   
            int distance = ( (int) Math.sqrt( Math.pow( boxX - selectedTileLoc.getX(), 2 ) + Math.pow( boxY - selectedTileLoc.getY(), 2 ) ) );
            if( distance < shortestDistance )
               {
               shortestDistance = distance;
               closestBoxX = boxXIndex;
               } //end if
            } //end for
    
         if( moveCandidates.indexOf( selectedTile ) != -1 )
            {
            moveCandidateLocs.remove( moveCandidates.indexOf( selectedTile ) );
            moveCandidates.remove( selectedTile );
            } //end if

         System.out.println( moveCandidates );
         System.out.println( moveCandidateLocs );
         } //end if

      selectedTile = null;
      update();
      } //end method mouseReleased
  
   public void mouseDragged( MouseEvent e )
      {
      if( selectedTile != null )
         {
         selectedTileLoc = null;
         selectedTileLoc = new Location( e.getY() - TILE_HEIGHT/2, e.getX() - TILE_LENGTH/2 );
         update();
         }
      } //end method mouseDragged

   public void actionPerformed( ActionEvent e )
      {
      clientHub.sendMoveRequest( new MoveRequest( clientHub.getMyIP(), moveCandidates, moveCandidateLocs ) );
      } //end method actionPerformed

   class DrawPanel extends JPanel
      {
      public void paintComponent( Graphics g )
         {
         myFont24 = g.getFont().deriveFont( 24f );
         g.setFont( myFont24 );
         fontMetrics24 = g.getFontMetrics();

         g.setColor( Color.WHITE );
         g.fillRect( 0, 0, this.getWidth(), this.getHeight() );

         g.setColor( Color.BLACK );
         for( int index = 0; index <= 15; index++ ) //draws vertical grid lines
            {
            g.setColor( Color.BLACK );
            g.drawLine( 31 + index * 31, 31, 31 + index * 31, 496 );
            } //end for
         for( int index = 0; index <= 15; index++ ) //draws horizontal grid lines
            {
            g.setColor( Color.BLACK );
            g.drawLine( 31, 31 + index * 31, 496, 31 + index * 31 );
            } //end for

         g.setColor( Color.BLACK );
         for( int index = 0; index <= 7; index++ ) //draws vertical lines
            {
            g.setColor( Color.BLACK );
            g.drawLine( 155 + index * 31, 526, 155 + index * 31, 557 );
            } //end for
         for( int index = 0; index <= 1; index++ ) //draws horizontal lines
            {
            g.setColor( Color.BLACK );
            g.drawLine( 155, 526 + index * 31, 372, 526 + index * 31 );
            } //end for

         try
            {
            for( Tile t: clientHub.getBoard().getTilesOnBoard() )
               {
               if( t != selectedTile )
                  {
                  g.setColor( Color.BLUE );
                  
                  Location tCoordinateLocation = clientHub.getBoard().getTileLocation( t );
                  Location tPixelLocation = tCoordinateLocation.toPixels();
      
                  g.fillRect( tPixelLocation.getX(), tPixelLocation.getY(), TILE_LENGTH, TILE_HEIGHT );
      
                  Rectangle stringBounds = fontMetrics24.getStringBounds( t.toString(), g ).getBounds();
                  int textX = ( TILE_LENGTH - stringBounds.width)/2;
                  int textY = ( TILE_HEIGHT - stringBounds.height)/2 + fontMetrics24.getAscent();
                  
                  g.setColor( Color.WHITE );
                  g.drawString( t.toString(), tPixelLocation.getX() + textX, tPixelLocation.getY() + textY );
                  } //end if
               } //end for

            for( Tile t: clientHub.getMyTiles() )
               {
               if( t != selectedTile && moveCandidates.indexOf( t ) == -1 )
                  {
                  g.setColor( Color.BLUE );
                  
                  Location tPixelLocation = myTilePixelLocs[ ( clientHub.getMyTiles().indexOf( t ) ) ];
      
                  g.fillRect( tPixelLocation.getX(), tPixelLocation.getY(), TILE_LENGTH, TILE_HEIGHT );
      
                  Rectangle stringBounds = fontMetrics24.getStringBounds( t.toString(), g ).getBounds();
                  int textX = ( TILE_LENGTH - stringBounds.width)/2;
                  int textY = ( TILE_HEIGHT - stringBounds.height)/2 + fontMetrics24.getAscent();
                  
                  g.setColor( Color.WHITE );
                  g.drawString( t.toString(), tPixelLocation.getX() + textX, tPixelLocation.getY() + textY );
                  } //end if
               } //end for
            } //end try
         catch( Exception ex )
            {
            System.out.println( ex );
            } //end catch
         
         for( Tile t: moveCandidates )
            {
            if( t!= selectedTile )
               {
               g.setColor( Color.BLUE );
                  
               Location tCoordinateLocation = moveCandidateLocs.get( moveCandidates.indexOf( t ) );
               Location tPixelLocation = tCoordinateLocation.toPixels();
   
               g.fillRect( tPixelLocation.getX(), tPixelLocation.getY(), TILE_LENGTH, TILE_HEIGHT );
      
               Rectangle stringBounds = fontMetrics24.getStringBounds( t.toString(), g ).getBounds();
               int textX = ( TILE_LENGTH - stringBounds.width)/2;
               int textY = ( TILE_HEIGHT - stringBounds.height)/2 + fontMetrics24.getAscent();
               
               g.setColor( Color.WHITE );
               g.drawString( t.toString(), tPixelLocation.getX() + textX, tPixelLocation.getY() + textY );
               } //end if 
            } //end for
   
         if( selectedTile != null )
            {
            g.setColor( Color.BLUE );
   
            g.fillRect( selectedTileLoc.getX(), selectedTileLoc.getY(), TILE_LENGTH, TILE_HEIGHT );
   
            Rectangle stringBounds = fontMetrics24.getStringBounds( selectedTile.toString(), g ).getBounds();
            int textX = ( TILE_LENGTH - stringBounds.width)/2;
            int textY = ( TILE_HEIGHT - stringBounds.height)/2 + fontMetrics24.getAscent();
            
            g.setColor( Color.WHITE );
            g.drawString( selectedTile.toString(), selectedTileLoc.getX() + textX, selectedTileLoc.getY() + textY );
            } //end if
         } //end method paintComponent
      } //end class drawPanel
   } //end class GUI


//          for( Tile t: clientHub().getBoard().getTilesOnBoard() )
//             {
//             g.setColor( Color.BLUE );
//             
//             if( selectedPiece == null && t.getX() >= 16 && p.getX() <= 495 && p.getY() >= 16 && p.getY() <= 495 )
//                {
//                int shortestDistance = 30;
//                int closestBoxX = 0;
//                int closestBoxY = 0;
//                for( int boxYIndex = 0; boxYIndex < 15; boxYIndex++ )
//                   {
//                   for( int boxXIndex = 0; boxXIndex < 15; boxXIndex++ )
//                      {
//                      int boxX = 31 + boxXIndex * 31;
//                      int boxY = 31 + boxYIndex * 31;
//             
//                      int distance = ( (int) Math.sqrt( Math.pow( boxX - p.getX(), 2 ) + Math.pow( boxY - p.getY(), 2 ) ) );
//                      if( distance < shortestDistance )
//                         {
//                         shortestDistance = distance;
//                         closestBoxX = 31 + boxXIndex * 31;
//                         closestBoxY = 31 + boxYIndex * 31;
//                         } //end if
//                      } //end for
//                   } //end for
//                p.setLocation( closestBoxX + 1, closestBoxY + 1 );
//                } //end if
//            
//             g.fillRect( p.getX(), p.getY(), Piece.WIDTH, Piece.HEIGHT );
// 
//             int width = Piece.WIDTH;
//             int height = Piece.HEIGHT;
//             Rectangle stringBounds = fontMetrics24.getStringBounds( p.getLetter(), g ).getBounds();
//             int textX = (width - stringBounds.width)/2;
//             int textY = (height - stringBounds.height)/2 + fontMetrics24.getAscent();
//             
//             g.setColor( Color.WHITE );
//             g.drawString( p.getLetter(), p.getX() + textX, p.getY() + textY );