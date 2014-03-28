import java.util.ArrayList;

import utility.GenSort;


public class Hand {
	
	
	public static final int MAX_HAND_SIZE = 14;

	public static final int NUM_TILES_NEEDED_TO_CHI = 2;
	public static final int NUM_TILES_NEEDED_TO_PON = 2;
	public static final int NUM_TILES_NEEDED_TO_KAN = 3;
	

	public static final boolean DEFAULT_CLOSED_STATUS = true;
	
	private static final int NOT_FOUND = -1;
	
	
	
	private ArrayList<Tile> mTiles;
	private ArrayList<Meld> mMelds;
	
	private boolean mClosed;
	private int mNumMeldsMade; 
	
	private char mPlayerSeatWind;
	
	

	private boolean mCanChiL;
	private boolean mCanChiM;
	private boolean mCanChiH;
	private boolean mCanPon;
	private boolean mCanKan;
	private boolean mCanRon;
	private ArrayList<Integer> mPartnerIndicesChiL;
	private ArrayList<Integer> mPartnerIndicesChiM;
	private ArrayList<Integer> mPartnerIndicesChiH;
	private ArrayList<Integer> mPartnerIndicesPon;
	private ArrayList<Integer> mPartnerIndicesKan;
	private Tile mCallCandidate;
	
	
	
	
	public Hand(char playerWind){
		mTiles = new ArrayList<Tile>(14);
		mMelds = new ArrayList<Meld>(5);
		
		mClosed = DEFAULT_CLOSED_STATUS;
		mNumMeldsMade = 0;
		
		mPlayerSeatWind = playerWind;
		
		//reset callable flags
		__resetCallableFlags();
		mCallCandidate = null;
		
	}
	public Hand(){
		this(Player.SEAT_UNDECIDED);
	}
	
	
	
	
	
	public Tile getTile(int index){
		
		if (index >= 0 && index < mTiles.size())
			return mTiles.get(index);
		else
			return null;
	}
	
	
	
	public int getSize(){
		return mTiles.size();
	}
	
	public boolean isClosed(){
		return mClosed;
	}
	
	public int getNumMeldsMade(){
		return mNumMeldsMade;
	}
	
	
	
	
	
	
	
	
	public boolean addTile(Tile t)
	{
		if (mTiles.size() < MAX_HAND_SIZE)
		{
			mTiles.add(t);
			return true;
		}
		else
			return false;
	}
	//overloaded for tileID, accepts integer tileID and adds a new tile with that ID to the hand (debug use)
	public boolean addTile(int tileID){
		return addTile(new Tile(tileID));
	}
	
	
	
	
	public boolean removeTile(int index){
		if (index >= 0 && index < mTiles.size())
		{
			//remove the tile
			mTiles.remove(index);
			
			//sort hand
			sortHand();
			
			return true;
		}
		else
			return false;
	}
	
	
	
	public void sortHand(){
		GenSort<Tile> sorter = new GenSort<Tile>(mTiles);
		sorter.sort();
	}
	
	
	
	
	
	
	
	/*
	public boolean demoMakeMeld(){
		
		if (getSize() >= 3)
		{
			//build a demo meld from the first 3 tiles in the hand
			ArrayList<Tile> meldTiles = new ArrayList<Tile>(0);
			meldTiles.add(mTiles.get(0));
			meldTiles.add(mTiles.get(1));
			meldTiles.add(mTiles.get(2));
			
			//make a meld out of the tiles
			mMelds.add(new Meld(meldTiles, mPlayerSeatWind));
			
			//remove the tiles from the hand
			mTiles.remove(0);
			mTiles.remove(2);
			mTiles.remove(3);
			
			return true;
		}
		else
			return false;
	
	}
	*/
	
	//demo values
	public void fill(){
		mTiles.add(new Tile(1));
		mTiles.add(new Tile(1));
		mTiles.add(new Tile(1));
		mTiles.add(new Tile(2));
		mTiles.add(new Tile(3));
		mTiles.add(new Tile(4));
		mTiles.add(new Tile(5));
		mTiles.add(new Tile(6));
		mTiles.add(new Tile(7));
		mTiles.add(new Tile(8));
		mTiles.add(new Tile(9));
		mTiles.add(new Tile(9));
		mTiles.add(new Tile(9));
		mTiles.add(new Tile(7));
	}
	
	
	
	
	
	
	
	//returns a list of IDs for all hot tiles for the hand
	public ArrayList<Integer> findAllHotTiles(){

		ArrayList<Integer> allHotTileIds = new ArrayList<Integer>(16);
		ArrayList<Integer> singleTileHotTiles = null;
		
		//get hot tiles for each tile in the hand
		for (Tile t: mTiles)
		{
			singleTileHotTiles = t.findHotTiles();
			for (Integer i: singleTileHotTiles)
				if (allHotTileIds.contains(i) == false)
					allHotTileIds.add(i);
		}
		
		//return list of integer IDs
		return allHotTileIds;
	}
	
	
	
	
	//////////////////////////////////////////////////////////////////////////////
	//////BEGIN MELD CHEKCERS
	//////////////////////////////////////////////////////////////////////////////
	
	private boolean __canChiType(int chiType){
		
		//if true, the player definitely CAN make a chi
		boolean can = false;
		//if true, the player definitely CANNOT make a chi
		boolean impossible = false;
		
		
		//can't chi on a wind or dragon
		if (mCallCandidate.getSuit() == Tile.SUIT_WIND || mCallCandidate.getSuit() == Tile.SUIT_DRAGON)
			return false;
		
		int partnerIndex1 = NOT_FOUND, partnerIndex2 = NOT_FOUND;
		
		
		int desiredID1 = 0, desiredID2 = 0;
		int tID = mCallCandidate.getId();
		
		//decide who the partners should be, based on chi type
		if (chiType == Meld.MELD_TYPE_CHI_L)
		{
			impossible = (mCallCandidate.getFace() == '8' || mCallCandidate.getFace() == '9');
			desiredID1 = tID + 1;
			desiredID2 = tID + 2;
		}
		if (chiType == Meld.MELD_TYPE_CHI_M)
		{
			impossible = (mCallCandidate.getFace() == '1' || mCallCandidate.getFace() == '9');
			desiredID1 = tID - 1;
			desiredID2 = tID + 1;
		}
		if (chiType == Meld.MELD_TYPE_CHI_H)
		{
			impossible = (mCallCandidate.getFace() == '1' || mCallCandidate.getFace() == '2');
			desiredID1 = tID - 2;
			desiredID2 = tID - 1;
		}
		
		//return false if a chi cannot be made with the tile 
		if (impossible)
			return false;
		
		
		//search the hand for the desired chi partners (get the indices)
		int i;
		for (i = 0; i < mTiles.size(); i++)
		{
			if (partnerIndex1 == NOT_FOUND && mTiles.get(i).getId() == desiredID1)
				partnerIndex1 = i;
			else if (partnerIndex2 == NOT_FOUND && mTiles.get(i).getId() == desiredID2)
				partnerIndex2 = i;
		}
		
		
		if (partnerIndex1 != NOT_FOUND && partnerIndex2 != NOT_FOUND)
		{
			//store the indices in the appropriate partners list
			ArrayList<Integer> storeHere = null;
			if (chiType == Meld.MELD_TYPE_CHI_L)
			{
				storeHere = mPartnerIndicesChiL;
				can = mCanChiL = true;
			}
			else if (chiType == Meld.MELD_TYPE_CHI_M)
			{
				storeHere = mPartnerIndicesChiM;
				can = mCanChiM = true;
			}
			else if (chiType == Meld.MELD_TYPE_CHI_H)
			{
				storeHere = mPartnerIndicesChiH;
				can = mCanChiH = true;
			}
			
			//sore the indices of the partners in a partner list
			__storePartnerIndices(storeHere, partnerIndex1, partnerIndex2);
		}
		
		return can;
	}
	
	//done
	private boolean __canChiL(){
		return __canChiType(Meld.MELD_TYPE_CHI_L);
	}
	private boolean __canChiM(){
		return __canChiType(Meld.MELD_TYPE_CHI_M);
	}
	private boolean __canChiH(){
		return __canChiType(Meld.MELD_TYPE_CHI_H);
	}
	
	//done
	private boolean __canPon(){
		boolean can = false;
		
		int count = howManyOfThisTileInHand(mCallCandidate, mPartnerIndicesPon);
		if (count >= NUM_TILES_NEEDED_TO_PON)
			can = mCanPon = true;
		
		return can;
	}
	
	//done
	private boolean __canKan(){
		boolean can = false;
		
		int count = howManyOfThisTileInHand(mCallCandidate, mPartnerIndicesKan);
		if (count >= NUM_TILES_NEEDED_TO_KAN)
			can = mCanKan = true;
		
		return can;
	}
	
	
	
	
	//returns the number of copies of this tiles that are already in the hand
	//also stores each index in a partner index list, if one is provided
	private int howManyOfThisTileInHand(Tile t, ArrayList<Integer> storeIndicesHere){
		
		int count = 0;
		int i;
		for (i = 0; i < mTiles.size(); i++)
		{
			if (mTiles.get(i).getId() == t.getId())
			{
				count++;
				
				//store the index in the list, if that's what we're doing
				if (storeIndicesHere != null && t == mCallCandidate )
					if ((storeIndicesHere == mPartnerIndicesKan && storeIndicesHere.size() < NUM_TILES_NEEDED_TO_KAN) || 
						(storeIndicesHere == mPartnerIndicesPon && storeIndicesHere.size() < NUM_TILES_NEEDED_TO_PON) )
						storeIndicesHere.add(i);
			}
		}
		
		return count;
	}
	
	//returns the number of copies of this tiles that are already in the hand
	@SuppressWarnings("unused")
	private int howManyOfThisTileInHand(Tile t){
		return howManyOfThisTileInHand(t, null);
	}
	
	public boolean __canRon(){
		return false;
	}
	
	
	//checks if a tile is callable
	//if a call is possible, updates a bool flag and list of partner indices for that call
	//returns true if a call (any call) can be made
	public boolean checkCallableTile(Tile candidate){
		
		//reset flags
		__resetCallableFlags();
		mCallCandidate = candidate;
		
		//runs checks, set flags to the check results
		mCanChiL = __canChiL();
		mCanChiM = __canChiM();
		mCanChiH = __canChiH();
		mCanPon = __canPon();
		if (mCanPon)
			mCanKan = __canKan();
		mCanRon = __canRon();
		
		//return true if a call (any call) can be made
		return (mCanChiL || mCanChiM || mCanChiH || mCanPon || mCanKan || mCanRon);
	}
	
	
	//sets boolean call flags to zero, creates new empty partner index lists
	private void __resetCallableFlags(){
		mCanChiL = mCanChiM = mCanChiH = mCanPon = mCanKan = mCanRon = false;
		mPartnerIndicesChiL = new ArrayList<Integer>(NUM_TILES_NEEDED_TO_CHI);
		mPartnerIndicesChiM = new ArrayList<Integer>(NUM_TILES_NEEDED_TO_CHI);
		mPartnerIndicesChiH = new ArrayList<Integer>(NUM_TILES_NEEDED_TO_CHI);
		mPartnerIndicesPon = new ArrayList<Integer>(NUM_TILES_NEEDED_TO_PON);
		mPartnerIndicesKan = new ArrayList<Integer>(NUM_TILES_NEEDED_TO_KAN);
	}
	
	
	private void __storePartnerIndices(ArrayList<Integer> storeHere, int... partnerIndices){
		for (Integer i: partnerIndices)
			storeHere.add(i);
	}
	
	
	//////////////////////////////////////////////////////////////////////////////
	//////END MELD CHEKCERS
	//////////////////////////////////////////////////////////////////////////////
	
	
	
	
	public boolean ableToChiL(){
		return mCanChiL;
	}
	public boolean ableToChiM(){
		return mCanChiM;
	}
	public boolean ableToChiH(){
		return mCanChiH;
	}
	public boolean ableToPon(){
		return mCanPon;
	}
	public boolean ableToKan(){
		return mCanKan;
	}
	public boolean ableToRon(){
		return mCanRon;
	}
	
	
	//true returns a string of indices (indices are +1 to match display)
	//false returns a string of actual tile values
	public String partnerIndicesString(int meldType, boolean actualTiles){
		
		String partnersString = "";
		ArrayList<Integer> wantedIndices = null;
		
		switch (meldType)
		{
		case Meld.MELD_TYPE_CHI_L:
			wantedIndices = mPartnerIndicesChiL;
			break;

		case Meld.MELD_TYPE_CHI_M:
			wantedIndices = mPartnerIndicesChiM;
			break;

		case Meld.MELD_TYPE_CHI_H:
			wantedIndices = mPartnerIndicesChiH;
			break;

		case Meld.MELD_TYPE_PON:
			wantedIndices = mPartnerIndicesPon;
			break;

		case Meld.MELD_TYPE_KAN:
			wantedIndices = mPartnerIndicesKan;
			break;
			
		default:
			return "none";
		}
		
		for (Integer i: wantedIndices)
		{
			if (actualTiles)
				partnersString += mTiles.get(i).toString() + ", ";
			else
				partnersString += Integer.toString(i+1) + ", ";
		}
		
		return partnersString;
	}
	public String partnerIndicesString(int meldType){return partnerIndicesString(meldType, false);}
	
	
	
	
	
	
	
	
	
	
	/*
	method: makeMeld
	forms a meld of the given type with the given tiles
	
	input: t is the "new" tile that is added to form the meld
		   meldType is the type of meld to form
	
	
	partnerIndices = choose which partner list to take indices from, based on meld type
	for (each index in partnerIndices)
		add hand(index) to tilesFromHand
	end for	
	
	form a new meld with the tiles
	remove the tiles from the hand
	*/
	public void makeMeld(Tile t, int meldType){
		
		//list of tiles, will hold the tiles coming form the hand that will be in the meld
		ArrayList<Tile> tilesFromHand = new ArrayList<Tile>(4);
		
		//figure out which partner list to use for the indices, based on the call type
		ArrayList<Integer> partnerIndices = null;
		if (meldType == Meld.MELD_TYPE_CHI_L)
			partnerIndices = mPartnerIndicesChiL;
		else if (meldType == Meld.MELD_TYPE_CHI_M)
			partnerIndices = mPartnerIndicesChiM;
		else if (meldType == Meld.MELD_TYPE_CHI_H)
			partnerIndices = mPartnerIndicesChiH;
		else if (meldType == Meld.MELD_TYPE_PON)
			partnerIndices = mPartnerIndicesPon;
		else if (meldType == Meld.MELD_TYPE_KAN)
			partnerIndices = mPartnerIndicesKan;
		
		
		//add the tiles from the hand to the list
		for (Integer index: partnerIndices)
			tilesFromHand.add(mTiles.get(index));
		
		//make the meld, add to the list of melds
		mMelds.add(new Meld(tilesFromHand, t, meldType));
		
		//remove the tiles from the hand
		while (partnerIndices.isEmpty() == false)
		{
			mTiles.remove(partnerIndices.get(partnerIndices.size() - 1).intValue());
			partnerIndices.remove(partnerIndices.size() - 1);
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	//prints all melds to the screen
	public void showMelds(){
		for (int i = 0; i < mMelds.size(); i++)
			System.out.println("Meld " + (i+1) + ": " + mMelds.get(i).toString());
	}
	
	
	
	
	
	
	public String toString(){

		String handString = "";
		
		int i;
		for (i = 0; i < mTiles.size(); i++)
			if (i+1 < 10)
				handString += (i+1) + "  ";
			else
				handString += (i+1) + " ";
		
		handString += "\n";
		for (i = 0; i < mTiles.size(); i++)
			handString += mTiles.get(i).toString() + " ";
		
		return handString;
	}

}
