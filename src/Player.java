import java.util.ArrayList;
import java.util.Scanner;

/*
Class: Player
represents a single player in the game

data:
	mHand - the player's hand (melds are also in here)
	mPond - the player's pond of discards
	
	mSeatWind - the player's seat wind (ESWN)
	mController - who is controlling the player (human or computer)
	
	mCallStatus - the player's call (reaction) to the most recent disacrd (chi, pon, kan, ron, or none)
	mDrawNeeded - the type of draw the player needs for their next turn (normal draw, kan draw, or no draw) 
	
	mHoldingRinshanTile - is true if the player is holding a rinshan tile that they drew this turn, false otherwise
	mRiichiStatus - is true if the player has declared riichi, false if not
	mFuritenStatus - is true if the player is in furiten status, false if not
	
	linkShimocha - a link to the player's shimocha (player to the right)
	linkToimen - a link to the player's toimen (player directly across)
	linkKamicha - a link to the player's kamicha (player to the left)
	

methods:
	constructors:
	2-arg - takes seat and controller, initializes wall and pond, and status info
	1-arg - takes seat, sets default controller
	no-arg - sets default seat and controller
	
	
	mutators:
	setController - sets the player's controller
 	setSeatWind - sets the player's seat wind
	setShimocha, setToimen, setKamicha, setNeighbors - set links to the player's neighbors
 	
 	accessors:
 	getHandSize - returns hand size
	getSeatWind - return seat wind
	getPlayerNumber - returns (1,2,3,4) corresponding to (E,S,W,N)
	getKamicha, getToimen, getShimocha - returns links to the player's neighbors
	
	checkRiichi - returns true if the player is in riichi status
	checkFuriten - returns true if the player is in furiten status
	checkRinshan - returns true if the player is holding a rinshan tile that they drew this turn
	
	called - returns true if the player has called a discarded tile
	checkCallStatus - returns the specific type of call the player has made
	checkDrawNeeded - returns the type of draw the player needs (normal draw, kan draw, or none)

	
	
	private:
	discardTile - gets a player's discard choice, discards it, and returns the tile
	askSelfForDiscard- asks a player which tile they want to discard, returns their choice
	__askDiscardHuman - asks a human player which tile they want to discard, returns their choice
	__askDiscardCom - asks a computer player which tile they want to discard, returns their choice
	
	askSelfForReaction - asks the player how they want to react to the discarded tile
	__askReactionHuman - asks a human player how they want to react to the discarded tile
	_askReactionCom - asks a computer player how they want to react to the discarded tile
	
	putTileInPond - adds a tile to the player's pond
	ableToCallTile - checks if the player is able to make a call on Tile t
	
	
	other:
	addTileToHand - add a tile to the player's hand
	takeTurn - walks the player through their discard turn, returns their discard
	reactToDiscard - shows a player a discarded tile, and gets their reaction (call or no call) to it
	
	showHand - display the player's hand
	showPond - display the player's pond
	getPondAsString - get the player's pond as a string
*/
public class Player {
	
	public static final char SEAT_UNDECIDED = 'U';
	public static final char SEAT_EAST = 'E';
	public static final char SEAT_SOUTH = 'S';
	public static final char SEAT_WEST = 'W';
	public static final char SEAT_NORTH = 'N';
	public static final char SEAT_DEFAULT = SEAT_UNDECIDED;

	public static final char CONTROLLER_UNDECIDED = 'u';
	public static final char CONTROLLER_HUMAN = 'h';
	public static final char CONTROLLER_COM = 'c';
	public static final char CONTROLLER_DEFAULT = CONTROLLER_UNDECIDED;
	

	public static final int CALLED_NONE = 0;
	public static final int CALLED_CHI = 1;
	public static final int CALLED_PON = 2;
	public static final int CALLED_KAN = 3;
	public static final int CALLED_RON = 5;
	public static final int CALLED_CHI_L = 11;
	public static final int CALLED_CHI_M = 12;
	public static final int CALLED_CHI_H = 13;
	public static final int CALLED_GAY = 6;
	
	
	public static final int DRAW_NONE = 0;
	public static final int DRAW_NORMAL = 1;
	public static final int DRAW_KAN = 3;

	public static final Tile WANT_SOMETHING = null;
	public static final int WANT_KAN_DRAW = 20;
	
	
	
	private Hand mHand;
	private Pond mPond;
	
	private char mSeatWind;
	private char mController;
	
	private int mCallStatus;
	private int mDrawNeeded;
	
	private boolean mHoldingRinshanTile;
	private boolean mRiichiStatus;
	private boolean mFuritenStatus;
	
	//private boolean mTenpai;
	//private ArrayList<Tile> mWaits;

	private Player linkShimocha;
	private Player linkToimen;
	private Player linkKamicha;
	
	
	
	public Player(char seat, char controller){
		
		mSeatWind = seat;
		
		mHand = new Hand(mSeatWind);
		mPond = new Pond();
		
		mCallStatus = CALLED_NONE;
		
		mRiichiStatus = false;
		mFuritenStatus = false;
		mHoldingRinshanTile = false;
		
		mController = controller;
	}
	
	public Player(char seat){
		this(seat, CONTROLLER_DEFAULT);
	}
	public Player(){
		this(SEAT_DEFAULT);
	}
	
	
	
	
	/*
	method: takeTurn
	walks the player through their discard turn
	
	returns their discarded tile
	
	
	discardedTile = discard a tile
	set drawNeeded = normal draw for next turn
	
	put discardedTile in the pond
	return discardedTile
	*/
	public Tile takeTurn(){
		
		Tile discardedTile;
		
		//discard a tile
		discardedTile = discardTile();
		//set draw status to normal
		mDrawNeeded = DRAW_NORMAL;
		
		//put the tile in the pond
		putTileInPond(discardedTile);
		
		//return the discarded tile
		return discardedTile;
	}
	
	
	//adds a tile to the pond
	private void putTileInPond(Tile t){
		mPond.addTile(t);
	}
	
	
	
	
	/*
	private method: discardTile
	gets a player's discard choice, discards it, and returns the tile
	
	returns the player's discarded tile
	
	
	chosenDiscard = ask self which tile to discard
	discardedTile = remove the chosen tile from the hand
	
	assign player wind as discarder attribute on discarded tile
	return discardedTile
	*/
	private Tile discardTile(){
		
		Tile discardedTile;
		int chosenDiscard;
		
		//ask player for which tile to discard
		chosenDiscard = askSelfForDiscard();
		
		//remove tile from hand
		discardedTile = mHand.getTile(chosenDiscard);
		mHand.removeTile(chosenDiscard);
		
		//set discarder attribute on discarded tile
		discardedTile.setDiscarder(mSeatWind);
		
		//return the discarded tile
		return discardedTile;
		
	}
	
	
	
	/*
	private method: askSelfForDiscard
	asks a player which tile they want to discard, returns their choice
	
	returns the index of the tile the player wants to discard
	
	
	if (controller == human)
		chosenDiscard = ask human
	else if (controller == computer)
		chosenDiscard = ask computer
	end if
	return chosenDiscard
	*/
	private int askSelfForDiscard()
	{
		int chosenDiscard;
		
		if (mController == CONTROLLER_HUMAN)
			chosenDiscard = __askDiscardHuman();
		else
			chosenDiscard = __askDiscardCom();
		
		return chosenDiscard;
	}
	
	
	
	
	/*
	private method: __askDiscardHuman
	asks a human player which tile they want to discard, returns their choice
	
	returns the index of the tile the player wants to discard
	
	
	chosenDiscard = ask user through keyboard
	return chosenDiscard
	*/
	private int __askDiscardHuman(){
		
		int chosenDiscard = 0;
		
		showHand();

		//ask user which tile they want to discard
		@SuppressWarnings("resource")
		Scanner keyboard = new Scanner(System.in);
		//disallow numbers outside the range of the hand size
		while (chosenDiscard < 1 || chosenDiscard > mHand.getSize())
		{
			System.out.print("\nWhich tile do you want to discard? (enter number): "); 
			chosenDiscard = keyboard.nextInt();
		}
		
		return chosenDiscard - 1;	//adjust for index
	}
	
	
	
	/*
	private method: __askDiscardCom
	asks a computer player which tile they want to discard, returns their choice
	
	returns the index of the tile the player wants to discard
	
	
	chosenDiscard = the last tile in the player's hand
	return chosenDiscard
	*/
	private int __askDiscardCom(){
		
		int chosenDiscard;
		
		//always choose the last tile in the hand (most recently drawn one)
		chosenDiscard = mHand.getSize() - 1;
		return chosenDiscard;
	}
	
	
	
	
	
	/*
	method: addTileToHand
	receives a tile, adds the tile to the player's hand
	
	add the tile to the player's hand
	set drawNeeded = none (since the player has just drawn)
	*/
	public void addTileToHand(Tile t){
		
		//add the tile to the hand
		mHand.addTile(t);
		
		//no longer need to draw
		mDrawNeeded = DRAW_NONE;
	}
	
	

	
	
	
	
	
	
	/*
	method: reactToDiscard
	shows a player a tile, and gets their reaction (call or no call) for it
	
	input: t is the tile that was just discarded, and the player has a chance to react to it
	
	returns the type of call the player wants to make on the tile (none, chi, pon, kan, ron)
	
	
	call = none
	if (the player is able to call the tile)
		call = ask self for a reaction to the tile
		update call status
		update the type of draw needed for the player's next turn
	end if
	return call
	*/
	public int reactToDiscard(Tile t)
	{
		int call = CALLED_NONE;
		if (ableToCallTile(t))
			;
		
		//if able to call the tile, ask self for reaction
		if (ableToCallTile(t))
		{
			//ask self for reaction
			call = askSelfForReaction(t);
			
			//update call status
			mCallStatus = call;
			
			//update what the player will need to draw next turn
			if (mCallStatus == CALLED_NONE)
				//draw normally if no call
				mDrawNeeded = DRAW_NORMAL;
			else
				//draw nothing if called chi/pon
				mDrawNeeded = DRAW_NONE;
			//if called kan, do a kan draw
			if (mCallStatus == CALLED_KAN)
				mDrawNeeded = DRAW_KAN;
		}
		
		return call;
	}
	
	
	
	
	/*
	private method: askSelfForReaction
	asks the player how they want to react to the discarded tile
	
	input: t is the tile that was just discarded, and the player has a chance to react to it
	
	returns the type of call the player wants to make on the tile (none, chi, pon, kan, ron)
	
	
	if (controller == human)
		call = ask human
	else if (controller == computer)
		call = ask computer
	end if
	return call
	*/
	private int askSelfForReaction(Tile t)
	{
		int call = CALLED_NONE;
		
		if (mController == CONTROLLER_HUMAN)
			call = __askReactionHuman(t);
		else
			call = __askReactionCom(t);
		
		return call;
	}
	
	
	/*
	private method: __askReactionHuman
	asks a human player how they want to react to the discarded tile
	
	input: t is the tile that was just discarded, and the player has a chance to react to it
	
	returns the type of call the player wants to make on the tile (none, chi, pon, kan, ron)
	
	
	if (controller == human)
		call = ask human
	else if (controller == computer)
		call = ask computer
	end if
	return call
	*/
	private int __askReactionHuman(Tile t)
	{
		int call = CALLED_NONE;
		

		@SuppressWarnings("resource")
		Scanner keyboard = new Scanner(System.in);
		System.out.print("Do you want to call? (0-no, _-chi, 2-pon, _-kan, _-ron, 6-gay): "); 
		call = keyboard.nextInt();
		
		return call;
	}
	
	
	
	/*
	private method: __askReactionCom
	asks a computer player how they want to react to the discarded tile
	
	input: t is the tile that was just discarded, and the player has a chance to react to it
	
	returns the type of call the player wants to make on the tile (none, chi, pon, kan, ron)
	
	
	if (controller == human)
		call = ask human
	else if (controller == computer)
		call = ask computer
	end if
	return call
	*/
	private int __askReactionCom(Tile t)
	{
		int call = CALLED_NONE;
		return call;
	}
	
	
	
	/*
	private method: ableToCallTileBeta
	checks if the player is able to make a call on Tile t
	
	input: t is the tile to check if the player can call
	
	returns true if the player can call the tile, false if not
	
	
	return true
	*/
	private boolean ableToCallTile(Tile t){
		
		boolean ableToCall = false;
		
		
		//check if tile t is a hot tile
		ArrayList<Integer> hotList = mHand.findAllHotTiles();
		
		//if t is not a hot tile, return false
		if (hotList.contains(t.getId()) == false)
			return false;
		
		
		//////At this point, we know t is a hot tile
		//we need to check which melds it can be called for, if any
		boolean canChiL, canChiM, canChiH, canPon, canKan = false, canRon;

		//check if t can be called for a chi
		canChiL = mHand.canChiL(t);
		canChiM = mHand.canChiM(t);
		canChiH = mHand.canChiH(t);

		//check if t can be called for a pon/kan
		canPon = mHand.canPon(t);
		if (canPon)
			canKan = mHand.canKan(t);
		
		//check if t can be called for a ron
		canRon = mHand.canRon(t);
		
		
		ableToCall = (canChiL || canChiM || canChiH || canPon || canKan || canRon);
		
		
		return ableToCall;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	//accessors
	public int getHandSize(){
		return mHand.getSize();
	}
	public char getSeatWind(){
		return mSeatWind;
	}
	//returns 1,2,3,4, corresponding to seat wind E,S,W,N
	public int getPlayerNumber(){
		if (mSeatWind == SEAT_EAST)
			return 1;
		else if (mSeatWind == SEAT_SOUTH)
			return 2;
		else if (mSeatWind == SEAT_WEST)
			return 3;
		else
			return 4;
	}
	

	
	public boolean checkRiichi(){
		return mRiichiStatus;
	}
	public boolean checkFuriten(){
		return mFuritenStatus;
	}
	public int checkCallStatus(){
		return mCallStatus;
	}
	//returns true if the player called a tile
	public boolean called(){
		return (mCallStatus != CALLED_NONE);
	}
	
	public int checkDrawNeeded(){
		return mDrawNeeded;
	}
	public boolean checkRinshan(){
		return mHoldingRinshanTile;
	}
	
	//accessors for other players
	//return references to mutable objects lol
	public Player getKamicha(){
		return linkKamicha;
	}
	public Player getToimen(){
		return linkToimen;
	}
	public Player getShimocha(){
		return linkShimocha;
	}
	
	
	
	//mutator for seat wind
	public boolean setSeatWind(char wind){
		if (wind ==  SEAT_EAST || wind ==  SEAT_SOUTH || wind ==  SEAT_WEST || wind ==  SEAT_NORTH)
			mSeatWind = wind;
		else
			return false;
		
		return true;
	}
	
	//used to set the controller of the player after its creation
	public boolean setController(char newController){
		if (mController == CONTROLLER_UNDECIDED)
			if (newController == CONTROLLER_HUMAN || newController == CONTROLLER_COM)
			{
				mController = newController;
				return true;
			}
			else
				System.out.println("-----Error: controller must be human or computer\n");
		else
			System.out.println("-----Error: controller has already been set\n");
		
		return false;
	}
	
	//mutators for other player links
	/*
	shimocha - player to your right
	toimen - player directly across 
	kamicha - player to your left
	*/
	public void setShimocha(Player p){
		linkShimocha = p;
	}
	public void setToimen(Player p){
		linkToimen = p;
	}
	public void setKamicha(Player p){
		linkKamicha = p;
	}
	public void setNeighbors(Player shimo, Player toi, Player kami){
		linkShimocha = shimo;
		linkToimen = toi;
		linkKamicha = kami;
	}
	
	
	
	
	//fill hand with demo values
	public void fillHand(){
		mHand.fill();
	}
	
	
	public void sortHand(){
		mHand.sortHand();
	}
	
	
	
	
	
	
	
	public void showPond(){
		System.out.println(mSeatWind + " Player's pond " + mController + ":\n" + mPond.toString());
	}
	public String getPondAsString(){
		return (mSeatWind + " Player's pond:\n" + mPond.toString());
	}
	
	public void showHand(){
		System.out.println("\n" + mSeatWind + " Player's hand " + mController + ":\n" + mHand.toString());
	}
	
	
	

}
