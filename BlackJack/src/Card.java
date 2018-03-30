// Simple class to represent a playing card with  
// suit and rank and value, with all suits making no difference in value

public class Card implements Comparable<Card>
{
    // For printing the card rank

    public static String[] RANKS =
    {
        "A", "2", "3", "4", "5", "6", "7", "8", " 9", "10", "J", "Q", "K"
    };

    // For printing the card suit

    public static String[] SUITS =
    {
        "-C", "-D", "-H", "-S"
    };
    
    // private instance variables

    private int rank;
    private int suit;

    // Default Constructor: initialize to Ace of Clubs

    public Card()
    {
        rank = 1;
        suit = 1;
    }

    // Two-Param Constructor : initialize rank and suit
    
    public Card (int initRank, int initSuit)
    {
        rank = initRank;
        suit = initSuit;
    }

    // Copy constructor: Copies a Card from another Card
    
    public Card (Card otherCard)
    {
        this.rank = otherCard.rank;
        this.suit = otherCard.suit;
    }

    public String getCardRank()
    {
    
    return RANKS[rank-1];

    }
    
    // returns 10 if face card
    public int getCardValue()
    {
    	int value = 0;
        if (rank > 9 && rank < 14)
        {
            value = 10;
        }
        else
        {
            value = rank;
        }
        
        return value;
    }
    
    // Returns relative position of this Card to someCard.
    // This compares the Cards by value
    
    public int compareTo (Card someCard)
    {
    int thisCard = this.getCardValue();
    int otherCard = someCard.getCardValue();
    int compareValue = -2;
    if (this.rank == someCard.rank && this.suit == someCard.suit) // this means the same object exactly 
    {
        compareValue = 2;
    }
    else if (thisCard == otherCard) // same value, which is more relevant to the game
	{
    		compareValue = 0;
	}
	else if (thisCard > otherCard)
	{
		compareValue = 1;
	}
	else if (thisCard < otherCard)
	{
		compareValue = -1;
	}

    return compareValue;
    
    }



    //Determines if the cards are the same value. Note cast of obj to Card    
    @Override
    public boolean equals (Object obj)
    {
        Card someCard = (Card) obj;
        return (someCard.compareTo (this) == 0);
    }

    // Print the card's rank and suit using the static
    // String arrays defined above
    @Override
    public String toString()
    {
        
        String printString = RANKS[rank - 1] + SUITS[suit - 1];
        return printString;
                
    }


}
