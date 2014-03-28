import java.util.ArrayList;


public class Meld {

	public static final int MELD_TYPE_UNKNOWN = 0;
	public static final int MELD_TYPE_CHI = 1;
	public static final int MELD_TYPE_PON = 2;
	public static final int MELD_TYPE_KAN = 3;
	public static final int MELD_TYPE_PAIR = 5;

	public static final boolean DEFAULT_CLOSED_STATUS = true;
	public static final int DEFAULT_MELD_TYPE = MELD_TYPE_UNKNOWN;
	
	
	
	//list of tiles in the meld
	private ArrayList<Tile> mTiles;
	
	private int mMeldType;
	private boolean mClosed;
	
	private char mPlayerSeatWind;
	
	
	
	
	//Constructor, takes a list of tiles and a closed status (true/false)
	public Meld(ArrayList<Tile> tiles, char playerWind, boolean closed){
		
		//add the tiles to the meld's list of tiles
		mTiles = new ArrayList<Tile>(0);
		for(Tile t: tiles)
			mTiles.add(t);
		
		//update closed status
		mClosed = closed;
		mPlayerSeatWind = playerWind;
		
		//meld type
		mMeldType = DEFAULT_MELD_TYPE;
	}
	public Meld(ArrayList<Tile> tiles, char playerWind){
		this(tiles, playerWind, DEFAULT_CLOSED_STATUS);
	}
	public Meld(ArrayList<Tile> tiles){
		this(tiles, Player.SEAT_UNDECIDED);
	}
	
	/*
	public Meld(Tile t1, Tile t2, Tile t3, Tile t4){
		ArrayList<Tile> tiles = new ArrayList<Tile>(0);
		tiles.add(t1);
		tiles.add(t2);
		tiles.add(t3);
		tiles.add(t4);
		this(tiles);
	}
	public Meld(Tile t1, Tile t2, Tile t3){
		
	}
	public Meld(Tile t1, Tile t2){
		
	}
	public Meld(){
		
	}
	*/
	
	
	
	
	
	
	public int calculateFu(){
		int fu = 0;
		
		return fu;
	}
	
	
	
	
	
	//accessors
	
	public int getMeldType(){
		return mMeldType;
	}
	
	
	public boolean isClosed(){
		return mClosed;
	}
	
	
	//toString
	public String toString(){
		
		String meldString = "";

		for (Tile t: mTiles)
			meldString += t.toString() + " ";
		
		return meldString;
	}

}
