import java.util.Scanner;

/*
kamicha - player to your left
shimocha - player to your right
toimen - player directly across 
*/
public class Player {
	
	public static final char SEAT_UNDECIDED = 'U';
	public static final char SEAT_EAST = 'E';
	public static final char SEAT_SOUTH = 'S';
	public static final char SEAT_WEST = 'W';
	public static final char SEAT_NORTH = 'N';
	
	public static final char CONTROLLER_HUMAN = 'h';
	public static final char CONTROLLER_COM = 'c';
	public static final char CONTROLLER_DEFAULT = CONTROLLER_HUMAN;
	

	public static final int CALLED_NONE = 0;
	public static final int CALLED_CHI = 1;
	public static final int CALLED_PON = 2;
	public static final int CALLED_KAN = 3;
	public static final int CALLED_RON = 5;
	
	
	
	
	private Hand mHand;
	private Pond mPond;
	
	private char mSeatWind;
	private char mController;
	
	private boolean mRiichiStatus;
	private boolean mFuritenStatus;
	

	private Player mShimocha;
	private Player mToimen;
	private Player mKamicha;
	
	
	
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
		
		
		mRiichiStatus = false;
		mFuritenStatus = false;
		
		mController = controller;
	}
	
	public Player(char seat){
		this(seat, CONTROLLER_DEFAULT);
	}
	public Player(){
		this(SEAT_UNDECIDED);
	}
	
	
	
	public Tile takeTurn(){
		
		
		Tile discardedTile;
		
		//discard a tile
		discardedTile = discardTile();
		
		//put the tile in the pond
		putTileInPond(discardedTile);
		
		//return
		return discardedTile;
	}
	
	
	
	private void putTileInPond(Tile t){
		mPond.addTile(t);
	}
	
	
	
	public Tile discardTile(){
		
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
		
		int chosenDiscard;
		
		showHand();

		//ask user which tile they want to discard
		@SuppressWarnings("resource")
		Scanner keyboard = new Scanner(System.in);
		System.out.print("\nWhich tile do you want to discard? (enter number): "); 
		chosenDiscard = keyboard.nextInt();
		
		return chosenDiscard - 1;	//adjust for index
	}
	
	
	private int __askDiscardCom(){
		
		int chosenDiscard;
		
		//always choose the last tile in the hand (most recently drawn one)
		chosenDiscard = mHand.getSize() - 1;
		return chosenDiscard;
	}
	
	
	
	
	
	
	public void addTileToHand(Tile t){
		mHand.addTile(t);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	public int getHandSize(){
		return mHand.getSize();
	}
	public char getSeatWind(){
		return mSeatWind;
	}
	

	
	public boolean checkRiichi(){
		return mRiichiStatus;
	}
	public boolean checkFuriten(){
		return mFuritenStatus;
	}
	
	
	
	
	
	public void fillHand(){
		mHand.fill();
	}
	
	
	
	
	
	
	
	
	public void showPond(){
		System.out.println("\n" + mSeatWind + " Player's pond:\n" + mPond.toString());
	}
	
	public void showHand(){
		System.out.println("\n" + mSeatWind + " Player's hand:\n" + mHand.toString());
	}
	
	
	

}
