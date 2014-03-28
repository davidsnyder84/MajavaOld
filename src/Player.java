import java.util.Scanner;


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
	public static final int CALLED_GAY = 6;
	
	
	public static final int DRAW_NONE = 0;
	public static final int DRAW_NORMAL = 1;
	public static final int DRAW_KAN = 3;
	
	
	
	
	private Hand mHand;
	private Pond mPond;
	
	private char mSeatWind;
	private char mController;
	
	private int mCallStatus;
	private int mDrawNeeded;
	
	private boolean mHoldingRinshanTile;
	private boolean mRiichiStatus;
	private boolean mFuritenStatus;
	

	private Player linkShimocha;
	private Player linkToimen;
	private Player linkKamicha;
	
	
	
	public Player(char seat, char controller){
		mHand = new Hand();
		mPond = new Pond();
		
		mSeatWind = seat;
		/*
		playerNumber = 0;
		if (mSeatWind == SEAT_EAST)
			playerNumber = 1;
		if (mSeatWind == SEAT_SOUTH)
			playerNumber = 2;
		if (mSeatWind == SEAT_WEST)
			playerNumber = 3;
		if (mSeatWind == SEAT_NORTH)
			playerNumber = 4;
		*/
		
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
	
	
	
	public Tile takeTurn(){
		
		
		Tile discardedTile;
		
		//discard a tile
		discardedTile = discardTile();
		//set draw status to normal
		mDrawNeeded = DRAW_NORMAL;
		
		//put the tile in the pond
		putTileInPond(discardedTile);
		
		//return
		return discardedTile;
	}
	
	
	
	private void putTileInPond(Tile t){
		mPond.addTile(t);
	}
	
	
	
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
	
	
	private int askSelfForDiscard()
	{
		int chosenDiscard;
		
		if (mController == CONTROLLER_HUMAN)
			chosenDiscard = __askDiscardHuman();
		else
			chosenDiscard = __askDiscardCom();

		
		return chosenDiscard;
	}
	
	
	
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
	
	
	private int __askDiscardCom(){
		
		int chosenDiscard;
		
		//always choose the last tile in the hand (most recently drawn one)
		chosenDiscard = mHand.getSize() - 1;
		return chosenDiscard;
	}
	
	
	
	
	
	
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
	 
	 ask self for a reaction to the tile
	 
	 update call status
	 update the type of draw needed for the player's next turn
	 return call
	*/
	public int reactToDiscard(Tile t)
	{
		int call = CALLED_NONE;
		
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
	
	
	
	
	private int askSelfForReaction(Tile t)
	{
		int call = CALLED_NONE;
		
		if (mController == CONTROLLER_HUMAN)
			call = __askReactionHuman(t);
		else
			call = __askReactionCom(t);
		
		return call;
	}
	

	private int __askReactionHuman(Tile t)
	{
		int call = CALLED_NONE;
		

		@SuppressWarnings("resource")
		Scanner keyboard = new Scanner(System.in);
		System.out.print("Do you want to call? (0-no, _-chi, 2-pon, _-kan, _-ron, 6-gay): "); 
		call = keyboard.nextInt();
		
		return call;
	}
	
	//ask com for their reaction (none by default)
	private int __askReactionCom(Tile t)
	{
		int call = CALLED_NONE;
		return call;
	}
	
	
	
	private boolean ableToCallTile(Tile t){
		return true;
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
