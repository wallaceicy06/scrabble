//Networked Scrabble
//Created by Chris Miles and Sean Harger
//Loyola High School class of 2011
import java.util.ArrayList;
import java.io.Serializable;
import java.io.*;
import java.util.HashSet;

public class GameBoard implements Serializable
   {
   private Tile[][] board;
   private static final int BOARD_LENGTH = 15;
   private static final int BOARD_HEIGHT = 15;

   private boolean isFirstMove;
   
   private transient Dictionary dictionary;
   private String strLine;

   public GameBoard()
      {
      board = new Tile[ BOARD_HEIGHT ][ BOARD_LENGTH ];
      dictionary = new Dictionary();
      isFirstMove = true;
      } //end constructor GameBoard

   public void add( Tile t, Location l )
      {
      if( board[ l.getY() ][ l.getX() ] == null )
         {
         board[ l.getY() ][ l.getX() ] = t;
         } //end if
      } //end method addTile

   public void remove( Tile t )
      {
      Location tileLoc = getTileLocation( t );
      
      if( tileLoc != null )
         {
         board[ tileLoc.getY() ][ tileLoc.getX() ] = null;
         } //end if
      } //end method removeTile

   public boolean getIsFirstMove()
      {
      return isFirstMove;
      }
   
   public void setIsFirstMove( boolean b )
      {
      isFirstMove = b;
      }

   public Tile get( Location l )
      {
      return board[ l.getY() ][ l.getX() ];
      } //end method getTile

   public boolean allCorrect( MoveRequest mr )
      {
      boolean containsPlacedTile = false;
      boolean passesThroughCenter = false;
      if( !isFirstMove )
         {
         passesThroughCenter = true;
         }
      for( int indexY = 0; indexY < board.length; indexY++ )
         {
         for( int indexX = 0; indexX < board[indexY].length; indexX++ )
            {
            if( board[ indexY ][ indexX ] != null )
               {
               if( ( indexY - 1 < 0 || board[ indexY - 1 ][ indexX ] == null ) && ( !(indexY + 1 >= board.length) && board[ indexY + 1 ][ indexX ] != null ) )
                  {
                  containsPlacedTile = false;
                  for( Tile t : buildWord( indexX, indexY, "down" ) )
                     {
                     if( !isFirstMove )
                        {
                        if( t.isPartOfWord() )
                           {
                           containsPlacedTile = true;
                           }
                        }
                     else if( board[7][7] != null && isFirstMove )
                        {
                        passesThroughCenter = true;
                        containsPlacedTile = true;
                        }
                        }
                  if( !passesThroughCenter )
                     {
                     System.out.println( "not in center" );
                     return false;
                     }
                  else if ( !containsPlacedTile )
                     {
                     System.out.println( "not part of another word" );
                     return false;
                     }
                  if( !checkWord( constructWord( indexX, indexY, "down" ) ) )
                     {
                     return false;
                     } // end if
                  for( Tile t : buildWord( indexX, indexY, "down" ) )
                     {
                     t.isPartOfWord( true );
                     }
                  } //end if
               if( ( indexX - 1 < 0 || board[ indexY ][ indexX - 1 ] == null ) && ( !(indexX + 1 >= board[indexY].length) && board[ indexY ][ indexX + 1] != null ) )
                  {
                  containsPlacedTile = false;
                  for( Tile t : buildWord( indexX, indexY, "right" ) )
                     {
                     if( !isFirstMove )
                        {
                        if( t.isPartOfWord() )
                           {
                           System.out.println( "not part of another word" );
                           containsPlacedTile = true;
                           }
                        }
                     else if( board[7][7] != null && isFirstMove )
                        {
                        System.out.println( "not in center" );
                        passesThroughCenter = true;
                        containsPlacedTile = true;
                        }
                     }
                  if( !passesThroughCenter )
                     {
                     System.out.println( "not in center" );
                     return false;
                     }
                  else if ( !containsPlacedTile )
                     {
                     System.out.println( "not part of another word" );
                     return false;
                     }
                  if( !checkWord( constructWord( indexX, indexY, "right" ) ) )
                     {
                     System.out.println( "not a word" );
                     return false;
                     } // end if
                  for( Tile t : buildWord( indexX, indexY, "right" ) )
                     {
                     t.isPartOfWord( true );
                     }
                  } //end if
               if( (( indexY - 1 < 0 || board[ indexY - 1 ][ indexX ] == null ) && ( !(indexY + 1 >= board.length) && board[ indexY + 1 ][ indexX ] == null ))  
                  && (( indexX - 1 < 0 || board[ indexY ][ indexX - 1 ] == null ) && ( !(indexX + 1 >= board[indexY].length) && board[ indexY ][ indexX + 1] == null)))
                  {
                  return false;
                  } 
               } //end if
            } //end inner for
         } //end outer for 
      return true;
      } //end method Correct

   public int scoreWords( MoveRequest mr )
      {
      int score = 0;
      boolean willBeScored = false;
      for( int indexY = 0; indexY < board.length; indexY++ )
         {
         for( int indexX = 0; indexX < board[indexY].length; indexX++ )
            {
            if( board[ indexY ][ indexX ] != null )
               {
               System.out.println( board[indexY][indexX] );
               if( ( indexY - 1 < 0 || board[ indexY - 1 ][ indexX ] == null ) && ( !(indexY + 1 >= board.length) && board[ indexY + 1 ][ indexX ] != null ) )
                  {
                  for( Tile t : buildWord( indexX, indexY, "down" ) )
                     {
                     for( Tile t2 : mr.getTiles() )
                        {
                        if( t.equals(t2) )
                           {
                           willBeScored = true;
                           }
                        }
                     }
                  if( willBeScored )
                     {
                     for( Tile t : buildWord( indexX, indexY, "down" ) )
                        {
                        score += t.getPoints();
                        }
                     willBeScored = false;
                     }
                  } //end if
               if( ( indexX - 1 < 0 || board[ indexY ][ indexX - 1 ] == null ) && ( !(indexX + 1 >= board[indexY].length) && board[ indexY ][ indexX + 1] != null ) )
                  {
                  for( Tile t : buildWord( indexX, indexY, "right" ) )
                     {
                     for( Tile t2 : mr.getTiles() )
                        {
                        if( t.equals(t2) )
                           {
                           willBeScored = true;
                           }
                        }
                     }
                  if( willBeScored )
                     {
                     for( Tile t : buildWord( indexX, indexY, "right" ) )
                        {
                        score += t.getPoints();
                        }
                     willBeScored = false;
                     }
                  } //end if
               } //end if
            } //end inner for
         } //end outer for 
      return score;
      } //end method Correct

   public String constructWord( int x, int y, String dir )
      {
      boolean endOfWord = false;
      String word = new String();
      while( !endOfWord )
         {
         if( ( y >= board.length ) || ( x >= board[y].length ) || board[y][x] == null )
            {
            endOfWord = true;
            }
         else if( board[y][x] != null )
            {
            word += board[y][x];
            System.out.println( word );
            } 
         if( dir.equals( "down" ) )
            {
            y++;
            }   
         else if( dir.equals( "right" ) )
            {
            x++;
            }
         }
      System.out.println( word.length() );
      return word;          
      } //end method constructWord

   public ArrayList<Tile> buildWord( int x, int y, String dir )
      {
      boolean endOfWord = false;
      ArrayList<Tile> word = new ArrayList<Tile>();
      while( !endOfWord )
         {
         if( ( y >= board.length ) || ( x >= board[y].length ) || board[y][x] == null )
            {
            endOfWord = true;
            }
         else if( board[y][x] != null )
            {
            word.add( board[y][x] );
            System.out.println( word );
            } 
         if( dir.equals( "down" ) )
            {
            y++;
            }   
         else if( dir.equals( "right" ) )
            {
            x++;
            }
         }
      return word;          
      } //end method constructWord

   public boolean checkWord( String word )
      {
      System.out.println( "check word called" + dictionary.contains(word));
      return dictionary.contains( word );
      } //end method checkWord

   public ArrayList<Tile> getTilesOnBoard()
      {
      ArrayList<Tile> tilesOnBoard = new ArrayList<Tile>();
      for( int indexY = 0; indexY < board.length; indexY++ )
         {
         for( int indexX = 0; indexX < board[0].length; indexX++ )
            {
            if( board[ indexY ][ indexX ] != null )
               {
               tilesOnBoard.add( board[ indexY ][ indexX ] );
               } //end if
            } //end inner for
         } //end outer for 

      return tilesOnBoard;
      } //end method getTilesOnBoard

   public Location getTileLocation( Tile t )
      {
      Location tileLoc = null;

      for( int yIndex = 0; yIndex < board.length; yIndex++ )
         {
         for( int xIndex = 0; xIndex < board[0].length; xIndex++ )
            {
            if( board[ yIndex ][ xIndex ] == t )
               {
               tileLoc = new Location( yIndex, xIndex );
               } //end if
            } //end inner for
         } //end outer for

      return tileLoc;
      } //end method getTileLocation

   public String toString()
      {
      String boardString = new String();

      for( int col = 0; col < board.length; col++ )
         {
         boardString += ( "\t" + col );
         } //end for

      for( int yIndex = 0; yIndex < board.length; yIndex++ )
         {
         boardString += ( "\n\n\n" + yIndex );
         for( int xIndex = 0; xIndex < board[0].length; xIndex++ )
            {
            boardString += "\t";
            if( board[ yIndex ][ xIndex ] != null )
               {
               boardString += ( board[ yIndex ][ xIndex ] );
               } //end if
            } //end inner for
         } //end outer for
   
      return boardString;
      } //end method toString
   } //end class GameBoard