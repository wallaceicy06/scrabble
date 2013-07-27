//Networked Scrabble
//Created by Chris Miles and Sean Harger
//Loyola High School class of 2011
import java.io.Serializable;

public abstract class Message implements Serializable
   {
   private String messageType;
   
   //message types:
   //"MoveRequest"
   //"DisplayMessage"
   //"MoveConfirmation"

   public Message( String type )
      {
      messageType = new String( type );
      } //end constructor Message

   public String getMessageType()
      {
      return messageType;
      } //end method getMessageType
   }