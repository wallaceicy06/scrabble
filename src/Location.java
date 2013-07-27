//Networked Scrabble
//Created by Chris Miles and Sean Harger
//Loyola High School class of 2011
import java.io.Serializable;

public class Location implements Serializable
   {
   int locationY;
   int locationX;

   private static final int NORTH = 0;
   private static final int EAST = 90;
   private static final int SOUTH = 180;
   private static final int WEST = 270;

   public Location( int y, int x )
      {
      locationY = y;
      locationX = x;
      } //end constructor Location

   public Location( Location other )
      {
      locationY = other.getY();
      locationX = other.getX();
      } //end constructor Location

   public int getY()
      {
      return locationY;
      } //end method getY

   public int getX()
      {
      return locationX;
      } //end method getX
   
   public Location getAdjacentLocation( int direction )
      {
      int dY = 0;
      int dX = 0;

      if( direction == Location.NORTH )
         {
         dY = -1;
         dX = 0;
         } //end if
      if( direction == Location.EAST )
         {
         dY = 0;
         dX = 1;
         } //end if
      if( direction == Location.SOUTH )
         {
         dY = 1;
         dX = 0;
         } //end if
      if( direction == Location.WEST )
         {
         dY = 0;
         dX = -1;
         } //end if

      return new Location( getY() + dY, getX() + dX );
      } //end method getAdjacentLocation

   public Location toPixels()
      {
      int pixelX = 0;
      int pixelY = 0;

      pixelX = ( 32 + getX() * 31 );
      pixelY = ( 32 + getY() * 31 );

      return new Location( pixelY, pixelX );
      } //end method toPixels  
 
   public String toString()
      {
      return new String( "( " + locationY + " , " + locationX + " )" );
      } //end method toString
   } //end class Location