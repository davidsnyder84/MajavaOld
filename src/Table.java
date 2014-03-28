import java.util.ArrayList;
import java.util.Scanner;

import utility.GenSort;




/*
 Class: Table
 
 data:
 	p1.. p4 - four players. p1 is always east, p2 is always south, etc. 
 	mWall - wall of tiles
 	mRoundWind - the prevailing wind of the current round ('E' or 'S')
 	mTurnNumber - what the fuck even is this
 
 methods:
	
	mutators:
 	setCardName
	setCardEffect
	setLegality
	setSerial
	setDesiredSort - set the desired sort attribute before sorting
 	
 	accessors:
 	getCardName
	getCardEffect
	getLegality
	getSerial
	getCardType - returns the card's type (ie, Villain) as a string
	
	other:
	compareTo - compares a specified attribute of one card to another (ie: name, serial, type)
	clone - clone
*/
public class Table {
	
	public static final int NUM_PLAYERS = 4;
	
	public static final char DEFAULT_ROUND_WIND = 'E';
	
	
	public static final boolean DO_SINGLE_PLAYER_GAME = true;
	public static final boolean SHUFFLE_SEATS = false;
	
	
	
	
	private Player p1, p2, p3, p4;
	
	private Wall mWall;
	
	private char mRoundWind;
	
	
	
	
	/*
	 No-arg Constructor
	 initializes a table to make it ready for playing
	 
	 
	 creates a player for each seat (4)
	 creates the wall
	 
	 initializes round wind and info
	*/
	public Table(){
		
		//creates a new player to sit at each seat
		p1 = new Player(Player.SEAT_EAST);
		p2 = new Player(Player.SEAT_SOUTH);
		p3 = new Player(Player.SEAT_WEST);
		p4 = new Player(Player.SEAT_NORTH);
		
		//creates the wall
		mWall = new Wall();
		
		//initializes round wind
		mRoundWind = DEFAULT_ROUND_WIND;
	}
	
	
	
	
	/*
	 method: play
	 plays a new game of mahjong with four new players
	 
	 
	 (before play is called)
	 	note: this is done in the Constructor
		4 players are created (empty seats, not assigned controllers)
		wall/deadwall is set up
		round wind = East
	 (play is called)
	 
	 
	 decide seat order
	 deal hands
	 
	 whoseTurn = 1;
	 while (game is not over)
	 	
	 	if (whoseTurn == 1)
	 		if (p1 needs to draw)
	 			take tile from wall or dead wall depending on what p1 needs
	 			add the tile to p1's hand
	 		end if
	 		
		 	q = p1.getDiscard, show what the player discarded
		 	have other players react
	 		whoseTurn++
	 	end if
	 	
	 	^Do for each player
	 	if (no reaction && whoseTurn == 2)
	 		...
	 	end if

	 	
	 	//handle reactions here
	 	if (reaction)
	 		check who reacted
	 		handle the reaction
	 		whoseTurn = whoever reacted
	 	end if
	 	
	 end while
	*/
	public void play()
	{
		
		boolean gameIsOver = false;
		int whoseTurn = 1;

		//Tile discard = null;
		Tile q = null;
		Tile drawnTile = null;
		
		final int NO_REACTION = 0;
		int reaction = NO_REACTION;
		int drawNeeded = Player.DRAW_NONE;
		
		ArrayList<Tile> indicators = null;
		
		
		
		
		
		
		
		
		
		
		
		//DEBUG INFO
		System.out.println(mWall.toString() + "\n\n\n");
		
		
		
		//decide seats
		decideSeats();
		
		//deal and sort hands
		mWall.dealHands(p1, p2, p3, p4);
		p1.sortHand();
		p2.sortHand();
		p3.sortHand();
		p4.sortHand();
		

		//-----DEBUG INFO
		System.out.println(mWall.toString());
		indicators = mWall.getDoraIndicators();

		System.out.println("\nDora indicators:");
		for (Tile t: indicators)
			System.out.println(t.toString());
		
		p1.showHand();p2.showHand();p3.showHand();p4.showHand();
		System.out.println("\n\n\n");
		//-----DEBUG INFO
		
		
		whoseTurn = 1;
		gameIsOver = false;
		while (gameIsOver == false)
		{
			reaction = NO_REACTION;

			//do 1st player's turn
			if (whoseTurn == 1)
			{
				//if the player needs to draw a tile, draw a tile
				drawNeeded = p1.checkDrawNeeded();
				if (drawNeeded != Player.DRAW_NONE)
				{
					//draw from wall or dead wall, depending on what player needs
					if (drawNeeded == Player.DRAW_NORMAL)
						drawnTile = mWall.takeTile();
					else if (drawNeeded == Player.DRAW_KAN)
						drawnTile = mWall.takeTileFromDeadWall();
					
					//add the tile to the player's hand
					p1.addTileToHand(drawnTile);
				}
				
				//get player's discard
				q = p1.takeTurn();
				System.out.println("\nPlayer1's discard: " + q.toString() + "\n");
				reaction += p2.reactToDiscard(q);
				reaction += p3.reactToDiscard(q);
				reaction += p4.reactToDiscard(q);
				whoseTurn++;
			}
			
			
			//do 2nd player's turn
			if (reaction == NO_REACTION && whoseTurn == 2)
			{
				//if the player needs to draw a tile, draw a tile
				drawNeeded = p2.checkDrawNeeded();
				if (drawNeeded != Player.DRAW_NONE)
				{
					//draw from wall or dead wall, depending on what player needs
					if (drawNeeded == Player.DRAW_NORMAL)
						drawnTile = mWall.takeTile();
					else if (drawNeeded == Player.DRAW_KAN)
						drawnTile = mWall.takeTileFromDeadWall();
					
					//add the tile to the player's hand
					p1.addTileToHand(drawnTile);
				}
				
				//get player's discard
				q = p2.takeTurn();
				System.out.println("\n\nPlayer2's discard: " + q.toString());
				reaction += p3.reactToDiscard(q);
				reaction += p4.reactToDiscard(q);
				reaction += p1.reactToDiscard(q);
				whoseTurn++;
			}
			
			
			//do 3rd player's turn
			if (reaction == NO_REACTION && whoseTurn == 3)
			{
				//if the player needs to draw a tile, draw a tile
				drawNeeded = p3.checkDrawNeeded();
				if (drawNeeded != Player.DRAW_NONE)
				{
					//draw from wall or dead wall, depending on what player needs
					if (drawNeeded == Player.DRAW_NORMAL)
						drawnTile = mWall.takeTile();
					else if (drawNeeded == Player.DRAW_KAN)
						drawnTile = mWall.takeTileFromDeadWall();
					
					//add the tile to the player's hand
					p1.addTileToHand(drawnTile);
				}
				
				//get player's discard
				q = p3.takeTurn();
				System.out.println("\n\nPlayer3's discard: " + q.toString());
				reaction += p4.reactToDiscard(q);
				reaction += p1.reactToDiscard(q);
				reaction += p2.reactToDiscard(q);
				whoseTurn++;
			}
			
			
			//do 4th player's turn
			if (reaction == NO_REACTION && whoseTurn == 4)
			{
				//if the player needs to draw a tile, draw a tile
				drawNeeded = p4.checkDrawNeeded();
				if (drawNeeded != Player.DRAW_NONE)
				{
					//draw from wall or dead wall, depending on what player needs
					if (drawNeeded == Player.DRAW_NORMAL)
						drawnTile = mWall.takeTile();
					else if (drawNeeded == Player.DRAW_KAN)
						drawnTile = mWall.takeTileFromDeadWall();
					
					//add the tile to the player's hand
					p1.addTileToHand(drawnTile);
				}
				
				//get player's discard
				q = p4.takeTurn();
				System.out.println("\n\nPlayer4's discard: " + q.toString());
				reaction += p1.reactToDiscard(q);
				reaction += p2.reactToDiscard(q);
				reaction += p3.reactToDiscard(q);
				whoseTurn = 1;
			}
			
			
			
			
			//handle reactions here
			if (reaction != NO_REACTION)
			{
				//oh gosh
				System.out.println("*****Well gosh, it looks like somebody reacted to tile " + q.toString() + "!");
				
				//show who called
				//change turn to whoever called's turn (this is flawed [when 2 calls happen])
				if (p1.checkCallStatus() != Player.CALLED_NONE)
				{
					System.out.println("Player1 called!");
					whoseTurn = 1;
				}
				
				if (p2.checkCallStatus() != Player.CALLED_NONE)
				{
					System.out.println("Player2 called!");
					whoseTurn = 2;
				}
				
				if (p3.checkCallStatus() != Player.CALLED_NONE)
				{
					System.out.println("Player3 called!");
					whoseTurn = 3;
				}
				
				if (p4.checkCallStatus() != Player.CALLED_NONE)
				{
					System.out.println("Player4 called!");
					whoseTurn = 4;
				}
			}
			
			
			
			/*
			mTurnNumber++;
			System.out.println(mRoundWind + " Round, Turn number: " + mTurnNumber);
			
			//take turn, show pond
			recentDiscard = p1.takeTurn();
			p1.showPond();
			
			recentDiscard.toString();
			*/
			
		}
	}
	
	
	
	/*
	private void doPlayerTurn(){
		
		//do 1st player's turn
		if (whoseTurn == 1)
		{
			//if the player needs to draw a tile, draw a tile
			if (drawNeeded != Player.DRAW_NONE)
			{
				//draw from wall or dead wall, depending on what player needs
				if (drawNeeded == Player.DRAW_NORMAL)
					drawnTile = mWall.takeTile();
				else if (drawNeeded == Player.DRAW_KAN)
					drawnTile = mWall.takeTileFromDeadWall();
				
				//add the tile to the player's hand
				p1.addTileToHand(drawnTile);
			}
			
			
			q = p1.takeTurn();
			System.out.println("\nPlayer1's discard: " + q.toString() + "\n");
			reaction += p2.reactToDiscard(q);
			reaction += p3.reactToDiscard(q);
			reaction += p4.reactToDiscard(q);
			whoseTurn++;
		}
		
	}
	*/
	
	
	
	
	//decides how many humans are playing, and randomly assigns all players to a seat
	private void decideSeats(){

		//figure out how many humans are playing
		int numHumans = 0;
		if (DO_SINGLE_PLAYER_GAME)
			numHumans = 1;
		else
		{
			Scanner keyboard = new Scanner(System.in);
			System.out.println("How many humans will be playing? (Enter 1-4): ");
			numHumans = keyboard.nextInt();
		}
		
		
		//add the requested number of humans to the list of controllers
		ArrayList<Character> controllers = new ArrayList<Character>(NUM_PLAYERS);
		int i;
		for (i = 0; i < NUM_PLAYERS; i++)
			if (i < numHumans)
				controllers.add(Player.CONTROLLER_HUMAN);
			else
				controllers.add(Player.CONTROLLER_COM);
		
		if (SHUFFLE_SEATS)
		{
			//shuffle the list controllers
			GenSort<Character> sorter = new GenSort<Character>(controllers);
			sorter.shuffle();
		}

		//assign the controllers to seats
		p1.setController(controllers.get(0));
		p2.setController(controllers.get(1));
		p3.setController(controllers.get(2));
		p4.setController(controllers.get(3));
	}
	
	
	
	
	
	public char getRoundWind(){
		return mRoundWind;
	}
	
	
	

}
