import java.util.ArrayList;




/*
Class: Tile
represents a single tile

data:
	mID - number ID of the tile, 1 to 34, same ID means same tile (ie, M2 has ID 2)
	mSuit - man, pin, sou, wind, dragon (M,C,B,W,D)
	mFace - 1-9, ESWN, white/green/red
	mRedDora - if true, indicates that the tile is a Red Dora 5 tile 
	
	mDiscardedBy - holds the wind of the player who discarded the tile. NONE if no discard.
	stringRepr - string representation of the suit and face. stored for convenience.
	
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
public class Tile implements Comparable<Tile> {
	
	public static final int NUMBER_OF_DIFFERENT_TILES = 34;
	
	public static final int DEFAULT_ID = 0;
	public static final String CHAR_FOR_RED_DORA = "%";
	
	public static final char DISCARDER_NONE = 'N';

	//private static final String STR_REPS_BY_ID_SPACES = "   M1 M2 M3 M4 M5 M6 M7 M8 M9 C1 C2 C3 C4 C5 C6 C7 C8 C9 B1 B2 B3 B4 B5 B6 B7 B8 B9 WE WS WN WW DW DG DR ";
	//private static final String STR_REPS_BY_ID_SPACES = "M1 M2 M3 M4 M5 M6 M7 M8 M9 C1 C2 C3 C4 C5 C6 C7 C8 C9 B1 B2 B3 B4 B5 B6 B7 B8 B9 WE WS WN WW DW DG DR ";
	private static final String STR_REPS_BY_ID = "M1M2M3M4M5M6M7M8M9C1C2C3C4C5C6C7C8C9B1B2B3B4B5B6B7B8B9WEWSWNWWDWDGDR";
	

	public static final char SUIT_MAN = 'M';
	public static final char SUIT_PIN = 'C';
	public static final char SUIT_SOU = 'B';
	public static final char SUIT_WIND = 'W';
	public static final char SUIT_DRAGON = 'D';
	
	
	
	private int mID;
	private char mSuit;
	private char mFace;
	
	private String stringRepr;
	
	private char mDiscardedBy;
	private boolean mRedDora;
	
	
	
	//2-arg Constructor, takes tile ID and boolean value for if it's a Red Dora 
	public Tile(int id, boolean isRed){
		
		mID = id;
		stringRepr = stringReprOfId(mID);
		mSuit = stringRepr.charAt(0);
		mFace = stringRepr.charAt(1);

		mDiscardedBy = DISCARDER_NONE;
		mRedDora = isRed;
	}
	//1-arg Constructor, takes tile ID
	public Tile(int id){
		this(id, false);
	}
	
	public Tile(int newID, char newSuit, char newFace){
		mID = newID;
		mSuit = Character.toUpperCase(newSuit);
		mFace = Character.toUpperCase(newFace);
		
		//stringRepr = Character.toString(mSuit) + Character.toString(mFace);
		stringRepr = stringReprOfId(mID);
		mDiscardedBy = DISCARDER_NONE;
		mRedDora = false;
	}
	public Tile(char newSuit, char newFace){
		this(DEFAULT_ID, newSuit, newFace);
	}
	public Tile(){
		this(1, 'M', '1');
	}

	
	
	
	
	
	
	//accessors
	public int getId(){
		return mID;
	}
	public char getSuit(){
		return mSuit;
	}
	public char getFace(){
		return mFace;
	}
	public boolean isRedDora(){
		return mRedDora;
	}
	
	public char discardedBy(){
		return mDiscardedBy;
	}
	
	
	
	
	
	public void setDiscarder(char discarder){
		mDiscardedBy = discarder;
	}
	
	//makes the tile a red dora tile
	public void setRedDora(){
		if (mFace == '5')
			mRedDora = true;
	}
	
	
	
	
	
	
	
	
	//returns a list of IDs for hot tiles
	public ArrayList<Integer> findHotTiles(){
		
		//ArrayList<Tile> hotTiles = new ArrayList<Tile>(1);
		//hotTiles.add(this);
		
		ArrayList<Integer> hotTileIds = new ArrayList<Integer>(1); 
		
		//a tile is always its own hot tile (pon/kan/pair)
		hotTileIds.add(mID);
		
		//add possible chi partners, if suit is not honor
		if (mSuit != SUIT_WIND && mSuit != SUIT_DRAGON)
		{
			if (mFace != '1' && mFace != '2')
				hotTileIds.add(mID - 2);
			if (mFace != '1')
				hotTileIds.add(mID - 1);
			if (mFace != '9')
				hotTileIds.add(mID + 1);
			if (mFace != '8' && mFace != '9')
				hotTileIds.add(mID + 2);
		}
		
		//return list of integer IDs
		return hotTileIds;
	}
	
	
	

	
	
	
	
	
	
	public int compareTo(Tile other){
		
		return (this.mID - other.mID);
	}
	
	//returns true if the tiles have the same ID
	public boolean equals(Object other){
		return (((Tile)this).mID == ((Tile)other).mID);
	}
	
	public String toString(){
		//return stringRepr;
		if (mRedDora)
			return (Character.toString(mSuit) + CHAR_FOR_RED_DORA); 
		else
			return stringReprOfId(mID);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	//returns all of the tile's info as a string (for debug use)
	public String toStringAllInfo(){
		
		String tileString = "";
		tileString += "Tile: " + mSuit + mFace + '\n';
		tileString += "\tID: " + mID + ", Suit: " + mSuit + ", Face: " + mFace + '\n';
		tileString += "\tDiscarded by: " + mDiscardedBy + '\n';
		tileString += "\tRed Dora?: " + mRedDora;
		
		return tileString;
	}
	
	
	
	
	
	public static String stringReprOfId(int id){
		/*
		 * 	private static String STR_REPS_BY_ID_SPACES = "M1 M2 M3 M4 M5 M6 M7 M8 M9 C1 C2 C3 C4 C5 C6 C7 C8 C9 B1 B2 B3 B4 B5 B6 B7 B8 B9 WE WS WN WW DW DG DR ";
	private static String STR_REPS_BY_ID = "M1M2M3M4M5M6M7M8M9C1C2C3C4C5C6C7C8C9B1B2B3B4B5B6B7B8B9WEWSWNWWDWDGDR";
		String s;
		s = STR_REPS_BY_ID.substring(3*mID + 1, 3*mID + 1 + 2);
		*/

		//return STR_REPS_BY_ID.substring(3*id + 1, 3*id + 1 + 2);
		//return STR_REPS_BY_ID.substring(3*id + 1, 3*id + 1 + 2);
		return STR_REPS_BY_ID.substring(2*(id-1), 2*(id-1) + 2);
	}
	
	
	
	
	

}
