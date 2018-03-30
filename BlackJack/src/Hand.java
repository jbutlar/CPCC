import java.util.*;
 /* this class is to handle Hand operations in a blackjack game, such as display,
  discard, and hit operations
  */
public class Hand
{
	
	private List<Card> aHand;
 	


	public Hand()
	{
    
    aHand = new ArrayList<>();

	}

	public String firstCardUp() // this is for when the only card the dealer shows is the top until stand
	{
		String thisHand = "";
		
		if (!aHand.isEmpty())
		{
			for (int i = 0; i < aHand.size(); i++)
			{
		         if(i == 0)
		         {
	             	thisHand += " " + aHand.get(i) + " ";
		         }
		         else 
		         {
		         	thisHand += " X-X ";
		         }
			}
		}
		else
		{
		thisHand = " [ ] [ ] ";
		}	

		return thisHand;
	}

	public String showHand() // this shows the cards in the hand
	{
		String thisHand = "";
		
		if (!aHand.isEmpty())
		{
	        for (int i = 0; i < aHand.size(); i++)
			{
		        
	          thisHand += " " + aHand.get(i) + " ";
		         
		    }
		}
		else
		{
			thisHand = " [ ] [ ] ";
		}

		return thisHand;
	}

	public void hit(Shoe aShoe) // passes in a shoe and gets a card from it
	{
      Card copy = aShoe.deal();
		aHand.add(copy);
	}
	
	public Card removeCard() // this is really how hands are discarded
	{
		Card cardInHand = new Card();
		if (!aHand.isEmpty())
		{

			cardInHand = aHand.remove(0);
			
		}
		
		return cardInHand;
	}

	public List<Card> getHand() // returns the hand list
	{      
      return aHand;
	}

	public boolean hasAce() // flag that ties into softAce
	{
		boolean aceThere = false;
        
        for (Card eachCard : aHand)
        {
			if (eachCard.getCardRank().equalsIgnoreCase("A") || eachCard.getCardValue() == 1)
			{
				aceThere = true;
			}
		} 

		return aceThere;
	}

	public int handValue() // gets value of hand and considers if hand has ace
	{
		int theValue = 0;
		if (!aHand.isEmpty())
		{
			
		  for (Card aCard : aHand)
		  {	
			  if (aCard.getCardValue() == 1)
			  {  
			     
				 if(theValue + 11 < 22)  // the first if and else if block (not the third)
			     {
                										 // test to see if there is an ace and considers 
                        // how it's value would be beneficial to the hand
                	
	                	theValue += 11;
	                	
	             }
				else if(theValue + 11 > 21)
				{
					
						theValue += 1;
					
				}
				  
		     }
		     else if (aCard.getCardValue() != 1) // this adds value for any other card besides an ace
			 {
					theValue += aCard.getCardValue();
		     }

			
		  }

			

		}

	    return theValue;
	} 

	public String toString() // prints the hand
	{
		String thisHand = "";
		
		if (!aHand.isEmpty())
		{
	        for (int i = 0; i < aHand.size(); i++)
			{
		        
	          thisHand += " " + aHand.get(i) + " ";
		         
		    }
		}

		return thisHand;
	}



}