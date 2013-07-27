//Networked Scrabble
//Created by Chris Miles and Sean Harger
//Loyola High School class of 2011
public class OmniTile extends Tile
   {
   public OmniTile( int id )
      {
      super( id, "", 0 );
      } //end constructor OmniTile

   public OmniTile( OmniTile other )
      {
      super( other );
      } //end constructor OmniTile

   public void setLetter( String l )
      {
      letter = l;
      } //end method setLetter
   } //end class OmniTile