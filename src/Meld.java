import java.util.ArrayList;

import utility.GenSort;


/*
Class: Meld
represents a meld of tiles (chi, pon, kan, pair)

data:
	mTiles - list of tiles in the meld
	mMeldType - the type of meld (chi, pon, kan, pair)
	mClosed = if the meld is a closed meld, this will be true, false otherwise
	mFu - the fu value of the meld
	
	mPlayerSeatWind - the seat wind of the owner of the meld (needed for checking fu of wind pairs)
	
methods:
	constructors:

	mutators:
 	
 	accessors:
	
	other:
	
	private:
*/
public class Meld {

	public static final int MELD_TYPE_UNKNOWN = 0;
	public static final int MELD_TYPE_CHI_L = 1;
	public static final int MELD_TYPE_CHI_M = 2;
	public static final int MELD_TYPE_CHI_H = 3;
	public static final int MELD_TYPE_PON = 4;
	public static final int MELD_TYPE_KAN = 5;
	public static final int MELD_TYPE_PAIR = 8;
	public static final int MELD_TYPE_DEFAULT = MELD_TYPE_UNKNOWN;
	/*
	public static final int MELD_TYPE_CHI = 123;
	public static final int MELD_TYPE_GAY = 6;
	public static final int MELD_TYPE_CHI_L = 11;
	public static final int MELD_TYPE_CHI_M = 12;
	public static final int MELD_TYPE_CHI_H = 13;
	*/
	public static final char OWNER_UNKOWN = '?';
	public static final char OWNER_DEFAULT = OWNER_UNKOWN;
	
	public static final boolean CLOSED_STATUS_DEFAULT = true;

	
	
	
	
	//list of tiles in the meld
	private ArrayList<Tile> mTiles;
	
	private int mMeldType;
	private boolean mClosed;

	private char mOwnerSeatWind;
	
	private Tile mCompletedTile;
	private char mPlayerResponsible;
	
	private int mFu;
	
	
	
	
	/*
	3-arg Constructor
	forms a meld from the given tiles, of the given meld type
	
	input: handTiles are the tiles that are coming from the player's hand
		   newTile is the "new" tile that is being added to form the meld
		   meldType is the type of meld that is being formed
	
	form the meld
	fu = 0
	*/
	public Meld(ArrayList<Tile> handTiles, Tile newTile, int meldType){		
		
		__formMeld(handTiles, newTile, meldType);
		
		mFu = 0;
	}
	//2-arg, takes list of tiles and meld type (used when making a meld only from hand tiles, so no "new" tile)
	//passes (handtiles 0 to n-1, handtile n, and meld type)
	public Meld(ArrayList<Tile> handTiles, int meldType){
		this(new ArrayList<Tile>(handTiles.subList(0, handTiles.size() - 1)), handTiles.get(handTiles.size() - 1), meldType);
	}
	
	/*
	//Constructor, takes a list of tiles and a closed status (true/false)
	public Meld(ArrayList<Tile> tiles, char ownerWind, boolean closed){
		
		//set the meld's list of tiles to the received list of tiles
		mTiles = tiles;
		
		//update closed status
		mClosed = closed;
		mOwnerSeatWind = ownerWind;
		
		//meld type
		mMeldType = MELD_TYPE_DEFAULT;
	}
	public Meld(ArrayList<Tile> tiles, char ownerWind){
		this(tiles, ownerWind, DEFAULT_CLOSED_STATUS);
	}
	public Meld(ArrayList<Tile> tiles){
		this(tiles, OWNER_DEFAULT);
	}
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
	
	
	
	
	
	/*
	private method: __formMeld
	forms a meld of the given type with the given tiles
	
	input: handTiles are the tiles from the player's hand
		   newTile is the "new" tile that is added to form the meld
		   meldType is the type of meld that is being formed
	
	
	set onwer's seat wind = owner of the hand tiles
	set player responsible = player who discarded the newTile
	set tile that completed the meld = newTile
	set meld type
	
	if (owner's wind == responsible's wind)
		set closed = true (tiles all came from owner's hand)
	else (owner's wind is different from responsible's wind)
		set closed = false (tile was called)
	end if
	
	add the hand tiles to the meld
	add the new tile to the meld
	sort the meld if it is a chi
	*/
	public void __formMeld(ArrayList<Tile> handTiles, Tile newTile, int meldType){
		
		//set the owner's seat wind
		mOwnerSeatWind = handTiles.get(0).getOrignalOwner();
		//check who is responsible for discarding the new tile
		mPlayerResponsible = newTile.getOrignalOwner();
		//set the new tile as the tile that completed the meld
		mCompletedTile = newTile;
		
		//check if the new tile came from someone other than the owner
		//closed = false if the tile came from someone else
		if (mPlayerResponsible == mOwnerSeatWind)
			mClosed = true;
		else
			mClosed = false;
		
		//set meld type
		mMeldType = meldType;
		

		//add the hand tiles to the meld
		mTiles = handTiles;
		//add the new tile to the meld
		mTiles.add(newTile);
		
		//sort the meld if it is a chi
		if (mMeldType == MELD_TYPE_CHI_L || mMeldType == MELD_TYPE_CHI_M || mMeldType == MELD_TYPE_CHI_H)
		{
			GenSort<Tile> sorter = new GenSort<Tile>(mTiles);
			sorter.sort();
		}
	}
	
	
	
	
	
	
	
	
	public int calculateFu(){
		int fu = 0;
		
		mFu = fu;
		return mFu;
	}
	
	
	
	
	
	//accessors
	public int getMeldType(){
		return mMeldType;
	}
	
	public boolean isClosed(){
		return mClosed;
	}
	
	public char getOwnerSeatWind(){
		return mOwnerSeatWind;
	}
	public char getResponsible(){
		return mPlayerResponsible;
	}
	
	
	//toString
	public String toString(){
		
		String meldString = "";
		
		//add the tiles to the string
		for (Tile t: mTiles)
			meldString += t.toString() + " ";

		//meldString += "  [Closed: " + mClosed + ", Responsible: " + mPlayerResponsible + "]";
		//show closed or open
		if (mClosed == true)
			meldString += "  [Closed]";
		else
			meldString += "  [Open, called from: " + mPlayerResponsible + "'s " + mCompletedTile.toString() + "]";
		
		return meldString;
	}

}
