//Networked Scrabble
//Created by Chris Miles and Sean Harger
//Loyola High School class of 2011
import java.io.Serializable;

public abstract class Tile implements Serializable
   {
   protected String letter;
   private int points;
   private int tileID;
   private boolean isPartOfWord;

   public Tile( int id, String l, int pts )
      {
      tileID = id;
      letter = new String( l );
      points = pts;
      } //end constructor Tile

   public Tile( Tile other )
      {
      tileID = other.getTileID();
      letter = new String( other.toString() );
      points = other.getPoints();
      } //end constructor Tile
   
   public boolean isPartOfWord()
      {
      return isPartOfWord;
      }
   
   public void isPartOfWord( boolean b )
      {
      isPartOfWord = b;
      }

   public int getPoints()
      {
      return points;
      } //end method getPoints

   public int getTileID()
      {
      return tileID;
      } //end method getTileID

   public boolean equals( Tile other )
      {
      boolean isEqual = false;
      
      if( getTileID() == other.getTileID() )
         {
         isEqual = true;
         } //end if

      return isEqual;
      } //end method equals

   public String toString()
      {
      return letter;
      } //end method toString
   } //end class Tile