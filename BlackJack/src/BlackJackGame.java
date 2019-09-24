import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.stage.Stage;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.*;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
//import java.util.ArrayList;
import javafx.geometry.Insets;

public class BlackJackFX extends Application 
{

    TextField inputUser; // gets username
    TextField inputSoftAce; // soft ace indicator
    TextField inputNumDecks; // number of decks
    String userName = "";
    String softHitIndicate = "";
    String outOfMoney = "";
    int gameDecks = 0;
    int userBet;
    private Button startGame = new Button("Start Game"); // gets game going
    private Button hit = new Button("Hit");
    private Button stand = new Button("Stand");
    private Button doubleDownButton = new Button("Double Down");
    TextArea displayGameEvents; // where the game text is displayed
    Label userLabel;
    Label greetingLabel;
    Label yesOrNo;
    RadioButton yes;
    RadioButton no;
    Label aceIndicator;
    Label incorrectDeckNumber;
    Label numDecksLabel;
    Label hitMessages;
    Label doubleMessages;
    Label standMessages;
    VBox softAceArea, deckNumArea; 
    Stage theStage;
    BlackJackGame aGame; // initialized if conditions met in initial pane
    private boolean handInPlay = false; // monitors game progress hand to hand
    private boolean zeroCache = false; // monitors users monetary cache and ensures positive number
    private Font font2 = Font.font("Verdana", FontWeight.BOLD, 14); // font for buttons and a few fields
    private Font labelFont = Font.font("Tahoma", FontWeight.EXTRA_BOLD, 14);
    
  @Override // Override the start method in the Application class
  public void start(Stage primaryStage) throws Exception 
  {
    // Create a scene and place it in the stage

    Scene scene = new Scene(getInitialPane(), 1300, 300);
    theStage = primaryStage;
    startGame.setFont(font2);
    hit.setFont(font2);
    stand.setFont(font2);
    doubleDownButton.setFont(font2);
    startGame.setOnAction(e-> switchToPlayArea(e)); 
    theStage.setTitle("BlackJack"); // Set the stage title
    theStage.setScene(scene); // Place the scene in the stage
    theStage.show(); // Display the stage
  
  }

    //  gets parameters from user to start game and switches to playarea if 
    // params are acceptable
    public BorderPane getInitialPane()
    {

            BorderPane welcomePane = new BorderPane();
	        HBox greetBox = new HBox();
	        VBox greeter = new VBox();
	        greetingLabel = new Label("Welcome to BlackJack...first a few preliminaries\n\n\n");
	        greetingLabel.setFont(font2);
	        greeter.getChildren().add(greetingLabel);
	        greetBox.getChildren().add(greeter);
	        greetBox.setPadding(new Insets(5));
	        greetBox.setAlignment(Pos.CENTER);

	        HBox gameParametersPanel = new HBox(); // 30

	        // 2 HBox to 1 VBox to keep formatting inline for fields and labels
	        VBox userArea = new VBox();

            HBox userLabelBox = new HBox();
            userLabel = new Label("Enter your name");
            userLabel.setFont(labelFont);
            userLabelBox.setMinHeight(50);
            userLabelBox.getChildren().add(userLabel);

            HBox userField = new HBox();
            inputUser = new TextField();
            inputUser.setPromptText("Name goes here");
            inputUser.setFocusTraversable(false); 
            userField.setMinHeight(50);
            userField.getChildren().add(inputUser);

            HBox boxForLabel = new HBox();
            Label spaceTaker = new Label("");
            spaceTaker.setFont(labelFont);
            boxForLabel.setMinHeight(50);
            boxForLabel.getChildren().add(spaceTaker);

            userArea.setPadding(new Insets(20));
            userArea.getChildren().addAll(userLabelBox, userField, boxForLabel);
    

            softAceArea = new VBox(); //20

            HBox softField = new HBox(60);
            //inputSoftAce = new TextField();
            //inputSoftAce.setMaxHeight(50);
            yes = new RadioButton("Yes");
            no = new RadioButton("No");
            ToggleGroup radioButtons = new ToggleGroup();
            yes.setToggleGroup(radioButtons);
            no.setToggleGroup(radioButtons);
            softField.setMinHeight(50);

            softField.getChildren().addAll(yes, no);


            HBox softLabel = new HBox();
            aceIndicator = new Label("Does dealer hit on soft ace?\n"
            	                          + "Y or N");
            aceIndicator.setFont(labelFont);
            softLabel.setMinHeight(50);
            softLabel.getChildren().add(aceIndicator);


            HBox yesNoBox = new HBox();
            yesOrNo = new Label("");
            yesOrNo.setFont(labelFont);
            yesNoBox.setMinHeight(50);
            yesNoBox.getChildren().add(yesOrNo);

            softAceArea.setPadding(new Insets(20));
            softAceArea.getChildren().addAll(softLabel, softField, yesNoBox);

            deckNumArea = new VBox(); 

            HBox numDeckField = new HBox();
	        inputNumDecks = new TextField();
	        numDeckField.setMinHeight(50);
	        numDeckField.getChildren().add(inputNumDecks);


	        HBox boxForNumDeckLabel = new HBox();
	        numDecksLabel = new Label("How many decks will be used: (1-4)\n");
	        numDecksLabel.setFont(labelFont);
	        boxForNumDeckLabel.setMinHeight(50);
	        boxForNumDeckLabel.getChildren().add(numDecksLabel);


	        HBox incorrectDeckBox = new HBox();
	        incorrectDeckNumber = new Label("");
	        incorrectDeckNumber.setFont(labelFont);
	        incorrectDeckBox.setMinHeight(50);
	        incorrectDeckBox.getChildren().add(incorrectDeckNumber);

	        deckNumArea.setPadding(new Insets(20));
	        deckNumArea.getChildren().addAll(boxForNumDeckLabel, numDeckField, incorrectDeckBox);


	        gameParametersPanel.getChildren().addAll(userArea, softAceArea, deckNumArea);

	        VBox buttonBox = new VBox(); 

	        HBox startButtonBox = new HBox();
            startButtonBox.setMinHeight(50);
            startButtonBox.getChildren().add(startGame);
            HBox filler1 = new HBox();
            filler1.setMinHeight(50);
            HBox filler2 = new HBox();
            filler2.setMinHeight(50);
            buttonBox.getChildren().addAll(filler1, startButtonBox, filler2);
            buttonBox.setPadding(new Insets(20));

            gameParametersPanel.getChildren().add(buttonBox);
            gameParametersPanel.setMargin(userArea, new Insets(20));
            gameParametersPanel.setMargin(softAceArea, new Insets(20));
            gameParametersPanel.setMargin(deckNumArea, new Insets(20));
            gameParametersPanel.setMargin(buttonBox, new Insets(20));

            gameParametersPanel.setAlignment(Pos.CENTER);

            welcomePane.setTop(greetBox);
	        welcomePane.setCenter(gameParametersPanel);
	        welcomePane.setPadding(new Insets(20));


      // this is initial panel 
      return welcomePane;

    }

    
    // pane for main play area, gives user option to start over with new bet should they run 
    // out of money
    public BorderPane playArea()
    {

	    BorderPane playPane = new BorderPane();
	    
	    HBox gameEventDisplay = new HBox(15);
	    
	    displayGameEvents = new TextArea(userName + ", you have $" + aGame.getCache() + 
		    					". \nEnter an even-numbered bet between $2 and $100 or press Exit Game");
	    displayGameEvents.setFont(font2);
	    gameEventDisplay.getChildren().add(displayGameEvents);
	    gameEventDisplay.setAlignment(Pos.CENTER);


	    

	    HBox buttonsAndBet = new HBox(75);

	    VBox betInteractionArea = new VBox(25);
	    
		// this is second panel for inital bet
		TextField inputBet = new TextField();
		
		inputBet.setPromptText("Enter bet here");
		inputBet.setFocusTraversable(false); 
		
		Button play = new Button("Play a hand");
		play.setFont(font2);
		Label enterCorrectAmount = new Label();
	
      
      /* Sets up what will happen when a user presses play and prevents certain things from
      happening when the button is pressed as well
      */
	  play.setOnAction((ActionEvent e) -> 
	  {
    
	    boolean isNumber = true;
		
	    if (zeroCache)
		{
		  
			aGame = new BlackJackGame(gameDecks, userName, softHitIndicate);
			zeroCache = false;
					  	
		}
	   
	    try 
	    {
		userBet = Integer.parseInt(inputBet.getText());
		}
		catch (NumberFormatException err)
		{
	        
	        isNumber = false;
		}
	    if (handInPlay)
	    {
	    		enterCorrectAmount.setText("Hand is in play!");
	    }
	    else if (!isNumber)
	    {
	    		enterCorrectAmount.setText("Please enter a whole even number between 2 and 100");
	    }
		else if (isNumber && (userBet < 2 || userBet > 100) || userBet % 2 != 0)
		{
			enterCorrectAmount.setText("Please enter an even-numbered bet between $2 and $100");
		}
		else if (!aGame.hasEnough(userBet))
		{

			if (aGame.getCache() > 0)
			{
				
				 
				String toDisplay = displayGameEvents.getText();
				toDisplay += "\n\nYou don't have enough money to place that bet." +
							"\nYou have $" + aGame.getCache();
							
				displayGameEvents.setText(toDisplay);
			
			}
			
		}
		else if (!handInPlay)
		{ 
			
			enterCorrectAmount.setText("");
		    handInPlay = true;
		    aGame.playHand();
		    
		    String notifyOfZero = "";
		    
		    if (aGame.getCache() - userBet == 0)
		    {
		    	notifyOfZero = "\nThat's all your money! Good luck!\n\n";
		    }
		    
		    //String playAnother = "Enter another bet to play another hand or press Exit Game to exit";
		    String initialHandText = userName + ", you have $" + aGame.getCache();
		    	
		    if(aGame.playerHasBlackJ() || aGame.dealerHasBlackJ())
		    { 
					initialHandText += "\n\n" + aGame.stand(userBet);
	
						    if (aGame.getCache() <= 1)
							{
								outOfMoney = userName + ", you are out of bettable money, enter another bet " + 
								                "to start the game over.";
								zeroCache = true;

							}
							else
							{
								outOfMoney = "\n" + userName + ", you have $" + aGame.getCache() +
								              "\nEnter another bet to play another hand";
							}
					
						    displayGameEvents.setText(notifyOfZero + initialHandText + "\n"
											 + "\n\nShoe now has " + aGame.shoeSize() 
						                     + " cards" + "\n" + outOfMoney); 
					aGame.discardHand();
					handInPlay = false;
	
				}
				else
				{
					initialHandText += 	(notifyOfZero +
					                    "\nShoe now has " + aGame.shoeSize() 
						                + " cards" + "\n\n" + aGame.showCurrentHand() + 
					  					"\n\nOptions Above: Hit, Stand, Doubledown, Exit");
					displayGameEvents.setText(initialHandText);
	
				}
	      }
		 
	    });
	

	    Label betAmount  = new Label("Enter an even # bet between 2 and 100" + 
	    	                         "\nBet Amount:");
        
	    betInteractionArea.getChildren().addAll(betAmount, inputBet, play, enterCorrectAmount);
        
	    VBox hitArea = new VBox(20);
	    Label hitLabel = new Label("Press to hit current hand");
	    hitLabel.setFont(labelFont);
        hitMessages = new Label();
        hitMessages.setFont(labelFont);
	    hit.setOnAction(e-> hitAction(e));
	    hitArea.getChildren().addAll(hitLabel, hit, hitMessages);
        
	    VBox standArea = new VBox(20);
	    Label standLabel = new Label("Stand with current hand");
	    standMessages = new Label();
	    standMessages.setFont(labelFont);
	    standLabel.setFont(labelFont);
	    stand.setOnAction(e-> standAction(e));  
	    standArea.getChildren().addAll(standLabel, stand, standMessages);
        
	    VBox doubleDownArea = new VBox(20);
	    Label doubleDownLabel = new Label("Double Down with curent hand");
        
	    doubleMessages = new Label();
	    doubleDownLabel.setFont(labelFont);
	    doubleMessages.setFont(labelFont);
	    doubleDownButton.setOnAction(e-> doubleAction(e));
	    doubleDownArea.getChildren().addAll(doubleDownLabel, doubleDownButton, doubleMessages);
        
	    VBox exitArea = new VBox(20);
	    Label exitLabel = new Label();
	    exitLabel.setFont(labelFont);
	    Button exitButton = new Button("Exit Game");
	    exitButton.setFont(font2);
        
	    exitButton.setOnAction((ActionEvent e) -> 
	    {
            System.exit(0);
	    });
    
	    exitArea.getChildren().addAll(exitLabel, exitButton);
    
	    buttonsAndBet.getChildren().addAll(exitArea, hitArea, standArea, doubleDownArea, betInteractionArea);
	    buttonsAndBet.setAlignment(Pos.CENTER);
	    playPane.setTop(buttonsAndBet);
	    playPane.setCenter(gameEventDisplay);
	    playPane.setPadding(new Insets(75));
   
      return playPane;
	}
    

    // action method for the start game button, many checks on input
	public void switchToPlayArea(ActionEvent e)
	{
	

	    userName = inputUser.getText();
	    boolean yesForSoft = yes.isSelected();
	    boolean noForSoft = no.isSelected();
		
    	
    	    boolean parametersComply = false;
      
    	if (e.getSource() == startGame)
        {
      
            boolean isNumber = true;
   
        try 
	    {
		    gameDecks = Integer.parseInt(inputNumDecks.getText());
		}
		catch (NumberFormatException err)
		{
	        
	        isNumber = false;
		}
		if(!isNumber)
		{
			incorrectDeckNumber.setText("That's not a number");
		}
	    else if(isNumber && gameDecks < 1 || gameDecks > 4 && 
	           (!yesForSoft && !noForSoft))
		{
			incorrectDeckNumber.setText("You must use at least one, but no more than four, "
				                        + "decks.\n" + "Please enter a number between 1 and 4.");
			yesOrNo.setText("Please select Yes or No");	
			
		}
		else if (gameDecks < 1 || gameDecks > 4)
		{

			incorrectDeckNumber.setText("You must use at least one, but no more than four, "
				                        + "decks.\n" + "Please enter a number between 1 and 4.");
			
		}
		else if  (!yes.isSelected() && !no.isSelected())
		{
			yesOrNo.setText("Please select Yes or No");
		}
		else 
		{
			parametersComply = true;
		}
		if (parametersComply)
		{
			
			incorrectDeckNumber.setText("");
			yesOrNo.setText("");
			aGame = new BlackJackGame(gameDecks, userName, softHitIndicate);
			Scene playScene = new Scene(playArea(), 1500, 800); 
			theStage.setScene(playScene); 
			
		}
      
     }

	}

 	/* 
 	sets up the hit action and accounts for busted hands as well as whether or not the
 	player has run out of money, offers to restart if player is out of money
	*/
	public void hitAction(ActionEvent e)
	{
      
      String textToDisplay = "";

      
      if (e.getSource() == hit)
      {

      	if (!handInPlay)
      	{

      		hitMessages.setText("Hand must be in play first");
      	}
      	else if (handInPlay)
      	{

      	  hitMessages.setText("");

          aGame.hitPlayerHand();

            if (!aGame.isBusted())
		    {
		    	aGame.hitDealerHand(); //factors in handvalue including soft ace
		    }
        
			textToDisplay = displayGameEvents.getText();
			textToDisplay += "\n\nShoe now has " + aGame.shoeSize() + " cards";

			if (aGame.isBusted())
			{
				textToDisplay += "\n\n" + aGame.stand(userBet);
				if (aGame.getCache() <= 1)
				{
					outOfMoney = "You are out of bettable money, enter another bet to start game over.";
					zeroCache = true;
				}
				else
				{
					outOfMoney = "\n" + userName + ", you have $" + aGame.getCache() + 
					          "\nEnter another bet to play another hand";
				}

				aGame.discardHand();
				handInPlay = false;
				
				textToDisplay += "\n\n" + outOfMoney;	

			}
	    	else if (!aGame.isBusted())
	    	{
	    		textToDisplay += "\n\n" + aGame.showCurrentHand();
	    		textToDisplay += "\n\nOptions above: Hit, Stand, Exit";
	    	}
        
    
	    displayGameEvents.setText(textToDisplay);

      	}
      
      }
	
	}
    
    // doesn't do anything if hand is not in play and stands the players hand if hand is in play
	public void standAction(ActionEvent e)
	{

      String textForStand = "";
      
      if (e.getSource() == stand)
      {

      	if (!handInPlay)
      	{

      		standMessages.setText("Hand must be in play to Stand");
      	}
      	else if(handInPlay)
      	{
 		
 		    standMessages.setText("");

      	    textForStand = displayGameEvents.getText();

            String stoodHand = aGame.stand(userBet);
            textForStand += "\n\nShoe now has " + aGame.shoeSize() + " cards";

		  if (!aGame.isTie() || aGame.isTie())
		  {

				if (aGame.getCache() <= 1)
				{
					outOfMoney = "You are out of bettable money, enter another bet to start game over.";
					zeroCache = true;


				}
				else
				{
					outOfMoney = "\n" + userName + ", you have $" + aGame.getCache() + 
					              "\nEnter another bet to play another hand";
				}
			  			  
			  aGame.discardHand();
			  handInPlay = false;					  
			 

			  textForStand += "\n\n" + stoodHand + "\n\n" + outOfMoney;
			  displayGameEvents.setText(textForStand);
              
		  }

      	}

	  }
    
    }
	
    // similar to stand
	public void doubleAction(ActionEvent e)
	{
    
    String textForDouble = "";

    if (e.getSource() == doubleDownButton)
    {
	
		if (!handInPlay)
		{
			doubleMessages.setText("Hand must be in play to Double Down");
		}
		else if (handInPlay)
		{

		    if (!aGame.doubleDown(userBet))
			{
				doubleMessages.setText("You either have too many cards " + "\n or not enough chips to "
						+ "\nDouble Down!");
			}
		    else if (aGame.doubleDown(userBet))	
		    {
		    	doubleMessages.setText("");
				textForDouble = displayGameEvents.getText();
				
				aGame.hitPlayerHand();
				
				String stoodHand = aGame.stand(userBet * 2);
				
				textForDouble += "\n\nShoe now has " + aGame.shoeSize() + " cards";	
			    	    
				    if (!aGame.isTie() || aGame.isTie())
			    	{
						if (aGame.getCache() <= 1)
						{
							outOfMoney = userName + 
							             ", you are out of bettable money, enter another bet to start game over.";
							zeroCache = true;


						}
						else
						{
							outOfMoney = "\n" + userName + ", you have $" + aGame.getCache() + 
							              "\nEnter another bet to play another hand";
						}
			  			  
			            aGame.discardHand();
			  			handInPlay = false;					  
			 

					    textForDouble += "\n\n" + stoodHand + "\n\n" + outOfMoney;
					    displayGameEvents.setText(textForDouble);

	          
		             }	  
			    		  
			     }

			  }
	     
  		  }

	  }
 
  /**
   * The main method is only needed for the IDE with limited
   * JavaFX support. Not needed for running from the command line.
   */
  public static void main(String[] args) {
    launch(args);
  }
}