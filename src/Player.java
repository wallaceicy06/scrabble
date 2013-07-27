//Networked Scrabble
//Created by Chris Miles and Sean Harger
//Loyola High School class of 2011
import java.util.ArrayList;
import java.io.Serializable;

public class Player implements Serializable
   {
   private String playerIP;
   private ArrayList<Tile> myTiles;
   private int myScore;

   public Player( String ipAddress )
      {
      playerIP = new String( ipAddress );
      myTiles = new ArrayList<Tile>();
      myScore = 0;
      } //end constructor Player

   public void addTile( Tile t )
      {
      myTiles.add( t );
      } //end method addTile

   public void removeTile( Tile t )
      {
      myTiles.remove( t );
      } //end method removeTile

   public void addScore( ArrayList<Tile> tiles )
      {
      for( Tile t: tiles )
         {
         myScore += t.getPoints();
         } //end for
      } //end method addScore
  
   public void addScore( int score )
      {
      myScore += score;
      } //end method addScore

   public int getScore()
      {
      return myScore;
      } //end method getScore

   public String getIP()
      {
      return playerIP;
      } //end method getIP

   public ArrayList<Tile> getTiles()
      {
      return myTiles;
      } //end method getTiles

   public String toString()
      { 
      return playerIP + " " + myTiles;
      }
   } //end class Player