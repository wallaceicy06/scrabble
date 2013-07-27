//Networked Scrabble
//Created by Chris Miles and Sean Harger
//Loyola High School class of 2011
public class FixedTile extends Tile
   {
   public FixedTile( int id, String l, int pts )
      {
      super( id, l, pts );
      } //end constructor FixedTile

   public FixedTile( FixedTile other )
      {
      super( other );
      } //end constructor FixedTile
   } //end class FixedTile