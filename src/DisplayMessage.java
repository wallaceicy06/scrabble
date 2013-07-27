//Networked Scrabble
//Created by Chris Miles and Sean Harger
//Loyola High School class of 2011
import java.util.ArrayList;
import java.io.Serializable;

public class DisplayMessage extends Message implements Serializable
   {
   private GameBoard board;
   private Player activePlayer;
   private ArrayList<Player> players;

   public DisplayMessage( Player ap, GameBoard b, ArrayList<Player> ps )
      {
      super( "DisplayMessage" );
      activePlayer = ap;
      board = b;
      players = ps;
      } //end constructor DisplayMessage

   public Player getActivePlayer()
      {
      return activePlayer;
      } //end method getActivePlayerIP

   public GameBoard getBoard()
      {
      return board;
      } //end method getBoard

   public ArrayList<Player> getPlayers()
      {
      return players;
      } //end method getPlayers
   } //end class DisplayInfo