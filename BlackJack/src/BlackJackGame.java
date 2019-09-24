/* 
Game engine for BlackJack that takes a userName, number of decks and has user
indicate whether or not dealer must hit if he has an ace and a hand value of 17
this class uses the stand method to return what the interface will print and to 
keep track of the amount of money a user has during the game
*/
public class BlackJackGame
{

private int numDecks;
private int numChips; // cache
private String softHit, userName; // softHit must be yes to change to dealer behavior for hand value of 17
private Shoe theShoe; // shoe for game
private Hand playerHand; 
private Hand dealerHand;
private boolean busted = false; // this only indicate if player has busted
private boolean isSoftHit = false; // passed in later to make dealer behave accordingly
private boolean dealerBlackJack = false;
private boolean playerBlackJack = false;
private boolean softAce = false; // determines if hand contains ace and has value of 17, works in conjunction
								// with isSoftHit to decide whether or not to hit dealer hand


    // 3-param constructor for setup of game
   public BlackJackGame(int deckNum, String userID, String softConsider)
   {
   
	   numChips = 1000;
	   numDecks = deckNum;
	   theShoe = new Shoe(numDecks);
	   userName = userID;
	   softHit = softConsider.toLowerCase();
	
		if (softHit.equalsIgnoreCase("y"))
		{
		isSoftHit = true;	
		}	

     }
   


    //begins a game and determines if blackjack has been met
	public void playHand()
	{
		busted = false;
		dealerBlackJack = false;
		playerBlackJack= false; 
		softAce = false;
		
		playerHand = new Hand();
		dealerHand= new Hand();
		
		for (int i = 1; i < 5; i++)
		{
		    if (i % 2 == 0)
		    {
			hitDealerHand();
		    } 
		    else
		    {
		    hitPlayerHand();
		    } 
	 	}
	 	if (playerHand.getHand().size() == 2 && playerHand.handValue() == 21)
	 	{
            		playerBlackJack = true;
	 	}
	 	if (dealerHand.getHand().size() == 2 && dealerHand.handValue() == 21)
	 	{
	 		dealerBlackJack = true;
	 	}
	    if (dealerHand.handValue() == 17 && dealerHand.hasAce())
	 	{
	 		softAce = true;
		}

	}
    
    // shows a hand that hasn't been stood yet, with dealer's second card hidden
	public String showCurrentHand()
	{
    String currentHand = "";
    currentHand = (userName + ": " + playerHand.showHand() + " (" + 
    	        playerHand.handValue() + ")" + "\n" +
    			"Dealer: " + dealerHand.firstCardUp());


    return currentHand;

	}

    
    // much of the action of the game, where different hand outcomes are decided (bust, blackjack, etc.)
    // and where cache is updated
	public String stand(int bet)
	{
	String addText = "";
	int winnings = 0;
    
    if (!busted && !playerBlackJack && dealerHand.handValue() <= 17)
    {	
    	hitDealerHand();
    }

    if (playerBlackJack && !dealerBlackJack)
    {
    	winnings = (int) (bet * 1.5);
    	incCache(winnings);
    	addText = "You won $" + winnings + " becuase you had BlackJack!";
    }
    else if (!playerBlackJack && dealerBlackJack)
    {
    	winnings = bet;
    	decCache(winnings);
    	addText = "You lost $" + winnings + " becuase dealer had BlackJack!";
    }
    else if (playerBlackJack && dealerBlackJack)
    {
    	addText = "You and dealer have BlackJack. Tie. Your bet is returned to you";
    }
    else if (busted)
    {
    decCache(bet);
    }
   	else if (((!playerBlackJack && !busted) && dealerHand.handValue() > 21) 
   		     || playerHand.handValue() > dealerHand.handValue())
   	{
   		winnings = bet;
   		addText = ("You have " + playerHand.handValue() + " and dealer has " +
   					dealerHand.handValue() + " You win $" + winnings);
   		incCache(winnings);

   	}
   	else if ((!busted && dealerHand.handValue() < 22
   		     && playerHand.handValue() < dealerHand.handValue()) || dealerBlackJack)
	{
		winnings = bet;
		addText = ("You have " + playerHand.handValue() + " and dealer has " +
   					dealerHand.handValue() + " You lose $" + winnings);
		decCache(winnings);
	}
   	else if (isTie())
   	{
   		addText = "You have " + playerHand.handValue() + " and dealer has " +
   					dealerHand.handValue() + ". Tie game! \n";
   	}
   	
	
	String currentHand = "";
    currentHand = String.format(userName + ": " + playerHand.showHand() + "\n" +
    			"Dealer: " + dealerHand.showHand() + "\n" + addText);
    
    if (busted)
    {
    	
    	addText = "You have " + playerHand.handValue() +". You busted, you lose";
    	currentHand = showCurrentHand() + "\n\n" + addText;
    
    }
    
    return currentHand;


	}
    
    // checks to see if user can do it and stands with double bet if so
	public boolean doubleDown(int bet)
	{
		boolean doubleAble = false;
		if (playerHand.getHand().size() == 2 && (getCache() - (bet * 2)) > 0)
		{
			doubleAble = true;
		}


		return doubleAble;
	}
    
    // gets hands ready for play hand, or emptys them if called
	public void discardHand()
	{
		theShoe.discard(playerHand);
		theShoe.discard(dealerHand);
	}
    // hits player hand regardless and monitors if bust or not
	public void hitPlayerHand()
	{ 

		playerHand.hit(theShoe);
		if (playerHand.handValue() > 21)
		{
			busted = true;
		}

	}

	// considers scenarios and hits if it should
	public void hitDealerHand()
	{

		if(dealerHand.handValue() == 17 && dealerHand.hasAce())
		{
				softAce = true;
		}
	    if ((softAce && isSoftHit) || (dealerHand.handValue() <= 16))
	    {	
	    	  dealerHand.hit(theShoe);
	    }

	}

	public int getCache()
	{
		return numChips;
	}

	public void incCache(int add)
	{
		numChips += add;
	}

	public void decCache(int subtract)
	{
		numChips -= subtract;
	}

	public int shoeSize()
	{
		return theShoe.shoeSize();
	}
    // only considers player's hand a bust or not
	public boolean isBusted()
	{
    boolean isBust = false;
		
    if (playerHand.handValue() > 21)
		{
		  isBust = true;
		}
      return isBust;

	}

	public boolean playerHasBlackJ()
	{
		return playerBlackJack;
	}
	
	public boolean dealerHasBlackJ()
	{
		return dealerBlackJack;
	}
    
    // fairly self-explanatory but used to test if hand is a tie
	public boolean isTie()
	{
		boolean tie = false;
		int playerCount = 1;
		int dealerCount = 0;
		if(!isBusted() && (!playerHand.getHand().isEmpty()) && 
				(!dealerHand.getHand().isEmpty()))
		{
		playerCount = playerHand.handValue();
		dealerCount = dealerHand.handValue();
		}
		if ((playerBlackJack && dealerBlackJack) || (playerCount == dealerCount))
		{
			tie = true;
		}

		return tie;
	}
   
   // can the user bet a certain amount, very useful to call from UI
   public boolean hasEnough(int aBet)
   {
		boolean enoughMoney = false;
		
		if (getCache() - aBet >= 0)
		{
			enoughMoney = true;
		}

		return enoughMoney;

   }
 
   
   
   
   







}



