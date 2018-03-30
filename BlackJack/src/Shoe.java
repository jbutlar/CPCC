import java.util.*;

/*
This is where cards are dealt in a particular BlackJackGame
it has a hopper and shuffles if the correct condition is met in the shoe
*/
public class Shoe
{
	
private ArrayList<Card> gameShoe;
private Stack<Card> gameCards;
private Stack<Card> hopper; 
private boolean debugShoe = false;


      public Shoe(int decks) // passes in a specified number of decks
	   {
        int theDecks = decks;
        if (theDecks > 0)
        {
            gameCards = new Stack<>();
            hopper = new Stack<>();
        	    gameShoe = new ArrayList<>();
        }
        	
        	// to keep track of decks upon original setup
        	for (int j = 0; j < theDecks; j++)
        	{

	        	Deck aDeck = new Deck();
          
            if(!aDeck.inTesting())
            {    
		        	for (int i = 0; i < 7; i++)
		        	{
					aDeck.shuffle();
				}
            }
            else 
            {
              debugShoe = true;
            } 
		

	        	for (Card aCard : aDeck)
	        	{
	        		gameShoe.add(aCard);
	        		Card copyCard = new Card(aCard);
	        		gameCards.push(copyCard);
	        	}

	        
	      }
	
	
	  }

	// cooperates with hit methods in other classes and deals
	// shuffles if gameCards (shoe) is empty
	public Card deal() 
	{
		
		if (gameCards.empty())
		{
			shoeShuffle();
		}
		
	    return gameCards.pop();
	}

    public int shoeSize()
    {
    		return gameCards.size();
    }

    // uses hands removeCard method to remove the cards from hand or "discard"
    public void discard(Hand hand)
    {
    	 int handSize = hand.getHand().size();
	    for (int i = 0; i < handSize; i++)
	    {
	    	  hopper.push(hand.removeCard());
	    	  
	    }

	}
    
    // shuffles cards in shoe and makes sure shoe is empty but not the hopper
	public void shoeShuffle()
	{
	
		if (gameCards.empty())
		{
		    
		   ArrayList<Card> newShoe = new ArrayList<>(); 	
			
			while(!hopper.empty())
			{
				
				Card copyCard = new Card(hopper.peek()); // makes a copy of top card in hopper stack
				newShoe.add(copyCard); // adds to new shoe
				hopper.pop(); // gets it out of hopper
			
			}
            if (!debugShoe)
            {
               for (int s = 0; s < 7; s++)
               {
   			    
   			    for (int i = 0; i < newShoe.size(); i++) // actual shuffle method
   	      		{	 
   			         // Generate an index randomly 
   			         int index = (int)(Math.random() * newShoe.size()-1);
   			         // Swap cards 
   			         Card temp = newShoe.get(i); 
   			         newShoe.set(i, newShoe.get(index)); 
   			         newShoe.set(index, temp);
   			      }
   
   		      }
               
            }
            else 
            {
            Collections.reverse(newShoe);
            }

		    gameShoe = newShoe;

		    for (Card aCard : newShoe)
		    {
		    	gameCards.push(aCard); // populates cards again
		    } 
		
		}
	
	}



}