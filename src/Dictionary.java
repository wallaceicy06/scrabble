//Networked Scrabble
//Created by Chris Miles and Sean Harger
//Loyola High School class of 2011
import java.io.*;
import java.util.HashSet;
public class Dictionary implements Serializable
   {
   private HashSet<String> validWords;
   
   public Dictionary()
      {
      try
         {
         fillDictionary();
         } //end try
      catch( Exception ex )
         {
         System.out.println( ex );
         } //end catch
      }

   public void fillDictionary() throws Exception
      {
      validWords = new HashSet<String>();
      FileReader in = new FileReader( "res/ScrabbleWords.txt" );
      BufferedReader br = new BufferedReader(in);
      String strLine = new String();
      while ((strLine = br.readLine()) != null) 
         {
         for (int i = 0; i < 127002; i++)
            {
            validWords.add( br.readLine() );
            }        
         }
      in.close();
      }

   public boolean contains( String s )
      {
      return validWords.contains( s );
      }
   }