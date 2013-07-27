//Networked Scrabble
//Created by Chris Miles and Sean Harger
//Loyola High School class of 2011
import java.io.Serializable;

public class MoveConfirmation extends Message implements Serializable
   {
   private boolean isCorrect;

   public MoveConfirmation( Boolean b )
      {
      super( "MoveConfirmation" );
      isCorrect = b;
      } //end constructor MoveConfirmation

   public boolean isCorrect()
      {
      return isCorrect;
      } //end method isCorrect
   } //end class MoveConfirmation