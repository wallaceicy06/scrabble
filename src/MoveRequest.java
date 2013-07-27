//Networked Scrabble
//Created by Chris Miles and Sean Harger
//Loyola High School class of 2011
import java.util.ArrayList;
import java.io.Serializable;

public class MoveRequest extends Message implements Serializable
   {
   private String playerIP;
   private ArrayList<Tile> candidateTiles;
   private ArrayList<Location> candidateLocs;

   public MoveRequest( String pIP, ArrayList<Tile> tiles, ArrayList<Location> locs )
      {
      super( "MoveRequest" );
      playerIP = new String( pIP );
      candidateTiles = tiles;
      candidateLocs = locs;
      } //end constructor MoveRequest
   
   public MoveRequest( MoveRequest other )
      {
      super( "MoveRequest" );
      playerIP = new String( other.getPlayerIP() );

      candidateTiles = new ArrayList<Tile>();
      for( Tile t: other.getTiles() )
         {
         Tile tCopy;
         if( t instanceof FixedTile )
            {
            tCopy = new FixedTile( (FixedTile) t );
            candidateTiles.add( tCopy );
            } //end if
         else if( t instanceof OmniTile )
            {
            tCopy = new OmniTile( (OmniTile) t );
            candidateTiles.add( tCopy );
            } //end if
         } //end for

      candidateLocs = new ArrayList<Location>();
      for( Location l: other.getLocations() )
         {
         Location lCopy = new Location( l );
         candidateLocs.add( lCopy );
         } //end for
      } //end constructor MoveRequest

   public String getPlayerIP()
      {
      return playerIP;
      } //end method getPlayerIP

   public ArrayList<Tile> getTiles()
      {
      return candidateTiles;
      } //end method getTiles

   public ArrayList<Location> getLocations()
      {
      return candidateLocs;
      } //end method getLocations

   public String toString()
      {
      String printout = new String();
      printout += ( "Tiles: " + candidateTiles + "\n" );
      printout += ( "Locs:  " + candidateLocs + "\n" );
      
      return printout;
      } //end method toString
   } //end class MoveRequest