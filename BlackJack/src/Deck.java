import java.util.*;
import java.io.*;

// Class to simulate a deck of 52 playing cards
// with rank and suit
//ranks are 1 - 13 , suits are seen in the Card class but listed here for reference
/*    public static String[] SUITS =
{
    "-C", "-D", "-H", "-S"
};
*/

/*
 * 
 * 
 */
public class Deck extends ArrayList<Card> 
{
   
   private static final boolean READ_DEBUG_DECK = false; // for testing
   private final String KNOWN_DECK = "src//StackedFile.txt"; // reads in known value cards for testing beginning with two hands of 21
   private Scanner sc;
   private File STACKED_DECK;


   public Deck()
	{
	
        if (READ_DEBUG_DECK)
        {
          new ArrayList<Card>();
        }
        else
        {
	   	  new ArrayList<Card>(52);
	    }
		
        generateDeck();
		
	}

	// Generates the 52 Cards and adds them to the Deck
	// Use a nested loop: Rank: 1-13 and Suit: 1-4

	public void generateDeck()
	{
		if (READ_DEBUG_DECK)
		{
      
		   try
		   {
             
           STACKED_DECK = new File(KNOWN_DECK);
      
           sc = new Scanner(STACKED_DECK);
			  
				while (sc.hasNext())
				{
					
					int rank = sc.nextInt();
					int suit = sc.nextInt();
					if (READ_DEBUG_DECK)
					{
					System.out.println(rank + "   " + suit); //file content
					}
					add(new Card(rank, suit));
				}
            
            sc.close();
			
		   }
		   catch(FileNotFoundException e)
		   {
		     e.printStackTrace();
		   }


		}
		else
		{
			for (int i = 1; i < 5; i++)
			{			
				for(int j = 1; j < 14; j++)
				{   
					
					add(new Card(j, i));

				}
			}
		}	
		
	}

	// Shuffles the 52 Cards: 
	public void shuffle()
	{      
      for (int i = 0; i < size(); i++) 
      { 
         // Generate an index randomly 
         int index = (int)(Math.random() * size()-1);
         // Swap cards 
         Card temp = get(i); 
         set(i, get(index)); 
         set(index, temp);
      } 
	
	}

	// Print the Deck: 13 cards per line
	// Call the Card's toString

	@Override
	public String toString()
	{
		String deckStr = "";
		for (int c = 0; c < size(); c++)
		{
			deckStr = deckStr + get(c).toString() +
					  ((((c+1) % 13) == 0) ? '\n' : ' ');
		}

		return deckStr;
	}
   
   public boolean inTesting()
   {
     return READ_DEBUG_DECK;
   }     
}

