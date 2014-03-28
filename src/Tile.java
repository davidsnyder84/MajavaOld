


/*
 Class: Tile
 
 data:
 	id - 1 to 34
 	mSuit - man, pin, sou,   wind, dragon
 	mFace - 1-9, ESWN, white/green/red
 
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
	
	public static final char NO_DISCARD = 'N';

	//private static String STR_REPS_BY_ID_SPACES = "   M1 M2 M3 M4 M5 M6 M7 M8 M9 C1 C2 C3 C4 C5 C6 C7 C8 C9 B1 B2 B3 B4 B5 B6 B7 B8 B9 WE WS WN WW DW DG DR ";
	//private static String STR_REPS_BY_ID_SPACES = "M1 M2 M3 M4 M5 M6 M7 M8 M9 C1 C2 C3 C4 C5 C6 C7 C8 C9 B1 B2 B3 B4 B5 B6 B7 B8 B9 WE WS WN WW DW DG DR ";
	private static String STR_REPS_BY_ID = "M1M2M3M4M5M6M7M8M9C1C2C3C4C5C6C7C8C9B1B2B3B4B5B6B7B8B9WEWSWNWWDWDGDR";
	
	
	
	
	private int mID;
	private char mSuit;
	private char mFace;
	
	private String stringRepr;
	
	private char mDiscardedBy;
	
	
	
	
	public Tile(int newID, char newSuit, char newFace){
		mID = newID;
		mSuit = Character.toUpperCase(newSuit);
		mFace = Character.toUpperCase(newFace);
		
		//stringRepr = Character.toString(mSuit) + Character.toString(mFace);
		stringRepr = stringReprOfId(mID);
		mDiscardedBy = NO_DISCARD;
	}
	public Tile(char newSuit, char newFace){
		this(DEFAULT_ID, newSuit, newFace);
	}
	public Tile(){
		this(1, 'M', '1');
	}
	
	public Tile(int id){
		
		mID = id;
		stringRepr = stringReprOfId(mID);
		mSuit = stringRepr.charAt(0);
		mFace = stringRepr.charAt(1);
		

		mDiscardedBy = NO_DISCARD;
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
	
	
	
	
	
	public void setDiscarder(char discarder){
		mDiscardedBy = discarder;
	}
	
	
	public char discardedBy(){
		return mDiscardedBy;
	}
	
	
	
	
	public String toString(){
		//return stringRepr;
		return stringReprOfId(mID);
	}
	
	
	public int compareTo(Tile other){
		
		return (this.mID - other.mID);
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
