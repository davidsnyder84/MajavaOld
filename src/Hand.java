import java.util.ArrayList;

import utility.GenSort;

/*
Class: Hand
represents a player's hand of tiles

data:
	mTiles - list of tiles in the hand
	mMelds - list of melds made from the hand
	
	mClosed - is true if the hand is fully concealed (no calls made on other discards)
	mNumMeldsMade - the number of melds made
	mOwnerSeatWind - holds the wind of the player who owns the hand
	
	mCallCandidate - the most recently discarded tile. checks are run on it to see if it can be called.
	mCanChiL, mCanChiM, mCanChiH, mCanPon, mCanKan, mCanRon - flags, set to true if a call can be made on mCallCandidate
	mPartnerIndicesChiL, etc - hold the hand indices of meld partners for mCallCandidate, if it can be called
	
methods:
	
	constructors:
	1-arg, takes player's seat wind
	creates list of tiles and melds, initializes flags and player info
	
	mutators:
	addTile - adds a tile to the hand (overloaded for actual tile, or an integer tile ID)
	removeTile - removes the tile at the given index
	sortHand - sorts the hand in ascending order
 	
 	accessors:
	getTile - returns the tile at the given index in the hand
	getSize - returns the number of tiles in the hand
	isClosed - returns true if the hand is fully concealed, false if an open meld has been made
	getNumMeldsMade - returns the number of melds made
	findAllHotTiles - returns a list of hot tile IDs for ALL tiles in the hand
	ableToChiL, ableToChiM, ableToChiH, ableToPon, ableToKan, ableToRon - return true if a call can be made on mCallCandidate
	
	private:
	__resetCallableFlags - resets call flags to false, creates new empty partner index lists
	__canChiL - checks if new tile can make a Chi-L
	__canChiM - checks if new tile can make a Chi-M
	__canChiH - checks if new tile can make a Chi-H
	__canChiType - checks if mCallCandidate can make a chi. populates chi partner list, sets canChi flag, returns true if chi is possible
	__canPon - checks if mCallCandidate can make a pon, populates pon partner list, sets canPon flag, returns true if pon is possible
	__canKan - checks if mCallCandidate can make a kan, populates kan partner list, sets canKan flag, returns true if kan is possible
	__howManyOfThisTileInHand - returns how many copies of a tile are in the hand, can also populate pon/kan partner index list
	__canRon - returns true if the player can call ron on the candidate tile
	__storePartnerIndices - takes a list and several integer indices, stores the indices in the list
	
	
	other:
	checkCallableTile - checks if a tile is callable. if it is callable, sets flag and populates partner index lists for that call
	makeMeld - forms a meld of the given type with mCallCandidate
	showMelds - prints all melds to the screen
	toString - returns string of all tiles in the hand, and their indices
*/
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
	
	private char mOwnerSeatWind;
	

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
	
	
	
	
	//1-arg constructor, takes player's seat wind
	public Hand(char playerWind){
		mTiles = new ArrayList<Tile>(14);
		mMelds = new ArrayList<Meld>(5);
		
		mClosed = DEFAULT_CLOSED_STATUS;
		mNumMeldsMade = 0;
		
		mOwnerSeatWind = playerWind;
		
		//reset callable flags
		__resetCallableFlags();
		mCallCandidate = null;
		
	}
	public Hand(){
		this(Player.SEAT_UNDECIDED);
	}
	
	
	
	
	//returns the tile at the given index in the hand
	public Tile getTile(int index){
		
		if (index >= 0 && index < mTiles.size())
			return mTiles.get(index);
		else
			return null;
	}
	
	
	//returns the number of tiles in the hand
	public int getSize(){
		return mTiles.size();
	}
	//returns true if the hand is fully concealed, false if an open meld has been made
	public boolean isClosed(){
		return mClosed;
	}
	//returns the number of melds made
	public int getNumMeldsMade(){return mNumMeldsMade;}
	//returns the hand owner's seat wind
	public char getOwnerSeatWind(){return mOwnerSeatWind;}
	
	
	
	
	
	
	
	//adds a tile to the hand (cannot add more than max hand size)
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
	
	
	
	//removes the tile at the given index
	//returns true if successful, returns false if index is outside of the hand's range
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
	
	
	//sort the hand in ascending order
	public void sortHand(){
		GenSort<Tile> sorter = new GenSort<Tile>(mTiles);
		sorter.sort();
	}
	

	
	
	
	
	
	
	
	
	
	//////////////////////////////////////////////////////////////////////////////
	//////BEGIN MELD CHEKCERS
	//////////////////////////////////////////////////////////////////////////////
	
	/*
	private method: __canChiType
	checks if a chi can be made with the new tile
	
	input: chiType is the type of chi to check (ChiL, ChiM, ChiH)
	
	populates the chi partner list, sets canChi flag, and returns true if chi type is possible
	
	
	if (candidate is an honor tile): return false
	partnerIndex1 = partnerIndex2 = NOT_FOUND
	
	desiredID1 and desiredID2 = IDs of the tiles that form the specififed chi type with candidate
	if (candidate cannot form the specified chi type, ie: ChiH with M1): return false
	
	partnerIndex1 and partnerIndex2 = search hand for desiredIDs, take first occurence of each
	if (indexes were found for both partners)
		set appropriate canChi flag
		store partner indices in appropriate chiPartnerIndices list
		can = true
	end if
	return can
	*/
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
		if (chiType == Meld.MELD_TYPE_CHI_L){
			impossible = (mCallCandidate.getFace() == '8' || mCallCandidate.getFace() == '9');
			desiredID1 = tID + 1;
			desiredID2 = tID + 2;
		}
		if (chiType == Meld.MELD_TYPE_CHI_M){
			impossible = (mCallCandidate.getFace() == '1' || mCallCandidate.getFace() == '9');
			desiredID1 = tID - 1;
			desiredID2 = tID + 1;
		}
		if (chiType == Meld.MELD_TYPE_CHI_H){
			impossible = (mCallCandidate.getFace() == '1' || mCallCandidate.getFace() == '2');
			desiredID1 = tID - 2;
			desiredID2 = tID - 1;
		}
		
		//return false if a chi cannot be made with the tile 
		if (impossible)
			return false;
		
		
		//search the hand for the desired chi partners (get the indices)
		int i;
		for (i = 0; i < mTiles.size(); i++){
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
	
	
	/*
	private method: __canPon
	checks if a pon can be made with the new tile
	populates the pon partner list, sets canPon flag, and returns true if pon is possible
	
	count = how many of this tile is in the hand
	if (there are already 2 of the tile in the hand)
		canPon = true
	end if
	return can
	*/
	private boolean __canPon(){
		boolean can = false;
		
		int count = __howManyOfThisTileInHand(mCallCandidate, mPartnerIndicesPon);
		if (count >= NUM_TILES_NEEDED_TO_PON)
			can = mCanPon = true;
		
		return can;
	}
	
	/*
	private method: __canKan
	checks if a kan can be made with the new tile
	populates the kan partner list, sets canKan flag, and returns true if kan is possible
	*/
	private boolean __canKan(){
		boolean can = false;
		
		int count = __howManyOfThisTileInHand(mCallCandidate, mPartnerIndicesKan);
		if (count >= NUM_TILES_NEEDED_TO_KAN)
			can = mCanKan = true;
		
		return can;
	}
	
	
	
	
	/*
	private method: __howManyOfThisTileInHand
	returns how many copies of tile t are in the hand
	populates a partner index list with the indices where t occurs, if one is provided
	
	input: t is the tile to look for, storeIndicesHere will hold the indices of the occurences of the tile
	
	count = 0
	for (each tile in the hand)
		if (hand tile == t)
			count++
			if (storeIndicesHere is a partner list): add the current index to the list
		end if
	end for
	return count
	*/
	private int __howManyOfThisTileInHand(Tile t, ArrayList<Integer> storeIndicesHere){
		
		int count = 0;
		int i;
		for (i = 0; i < mTiles.size(); i++)
		{
			if (mTiles.get(i).equals(t))
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
	//overloaded, omitting list argument simply returns the count, and doesn't populate any list
	@SuppressWarnings("unused")
	private int __howManyOfThisTileInHand(Tile t){
		return __howManyOfThisTileInHand(t, null);
	}
	
	//returns true if the player can call ron on the candidate tile
	public boolean __canRon(){
		return false;
	}
	
	
	
	/*
	method: checkCallableTile
	checks if a tile is callable
	if a call is possible, sets flag and populates partner index lists for that call
	
	input: candidate is the tile to check if callable
	returns true if a call (any call) can be made for the tile
	
	
	reset all call flags/lists
	mCallCandidate = candidate
	check if each type of call can be made (flags are set and lists are populated here)
	if any call can be made, return true. else, return false
	*/
	public boolean checkCallableTile(Tile candidate){
		
		//////reset flags
		__resetCallableFlags();
		mCallCandidate = candidate;
		
		//////runs checks, set flags to the check results
		//only allow chis from the player's kamicha, or from the player's own tiles
		if ((candidate.getOrignalOwner() == mOwnerSeatWind) || 
			(Player.findKamichaOf(mOwnerSeatWind) == candidate.getOrignalOwner())){
			mCanChiL = __canChiL();
			mCanChiM = __canChiM();
			mCanChiH = __canChiH();
		}
		
		//check pon. if can't pon, don't bother checking kan
		if (mCanPon = __canPon())
			mCanKan = __canKan();
		
		//check ron
		mCanRon = __canRon();
		
		//////return true if a call (any call) can be made
		return (mCanChiL || mCanChiM || mCanChiH || mCanPon || mCanKan || mCanRon);
	}
	
	
	//resets call flags to false, creates new empty partner index lists
	private void __resetCallableFlags(){
		mCanChiL = mCanChiM = mCanChiH = mCanPon = mCanKan = mCanRon = false;
		mPartnerIndicesChiL = new ArrayList<Integer>(NUM_TILES_NEEDED_TO_CHI);
		mPartnerIndicesChiM = new ArrayList<Integer>(NUM_TILES_NEEDED_TO_CHI);
		mPartnerIndicesChiH = new ArrayList<Integer>(NUM_TILES_NEEDED_TO_CHI);
		mPartnerIndicesPon = new ArrayList<Integer>(NUM_TILES_NEEDED_TO_PON);
		mPartnerIndicesKan = new ArrayList<Integer>(NUM_TILES_NEEDED_TO_KAN);
	}
	
	//takes a list and several integer indices, stores the indices in the list
	private void __storePartnerIndices(ArrayList<Integer> storeHere, int... partnerIndices){
		for (Integer i: partnerIndices)
			storeHere.add(i);
	}
	
	
	//returns true if a specific call can be made on mCallCandidate
	public boolean ableToChiL(){return mCanChiL;}
	public boolean ableToChiM(){return mCanChiM;}
	public boolean ableToChiH(){return mCanChiH;}
	public boolean ableToPon(){return mCanPon;}
	public boolean ableToKan(){return mCanKan;}
	public boolean ableToRon(){return mCanRon;}
	
	
	//////////////////////////////////////////////////////////////////////////////
	//////END MELD CHEKCERS
	//////////////////////////////////////////////////////////////////////////////
	
	
	
	
	
	
	
	
	
	
	
	
	/*
	method: makeMeld
	forms a meld of the given type with mCallCandidate
	
	input: meldType is the type of meld to form
	
	
	partnerIndices = choose which partner list to take indices from, based on meld type
	for (each index in partnerIndices)
		add hand(index) to tilesFromHand
	end for	
	
	form a new meld with the tiles
	remove the tiles from the hand
	numMeldsMade++
	*/
	public void makeMeld(int meldType){
		
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
		mMelds.add(new Meld(tilesFromHand, mCallCandidate, meldType));
		
		//remove the tiles from the hand
		while (partnerIndices.isEmpty() == false)
		{
			mTiles.remove(partnerIndices.get(partnerIndices.size() - 1).intValue());
			partnerIndices.remove(partnerIndices.size() - 1);
		}
		
		mNumMeldsMade++;
	}
	
	
	
	
	
	
	
	
	
	

	
	//returns a list of hot tile IDs for ALL tiles in the hand
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

	//returns a list of callable tile IDs for ALL tiles in the hand
	public ArrayList<Integer> findAllCallableTiles(){
		
		
		ArrayList<Integer> allCallableTileIds = new  ArrayList<Integer>(4);
		ArrayList<Integer> hotTileIds;
		
		
		final boolean TEST_ALL_TILES = false;
		
		if (TEST_ALL_TILES)
		{
			hotTileIds = new ArrayList<Integer>(34);
			for (int i = 1; i <= 34; i++)
				hotTileIds.add(i);
		}
		else
			hotTileIds = findAllHotTiles();
		
		
		//examine all hot tiles
		for (Integer i: hotTileIds)
			//if tile i is callable
			if (checkCallableTile(new Tile(i)))
				//add its id to the list of callable tiles
				allCallableTileIds.add(i);
		
		//return list of callable tile ids
		return allCallableTileIds;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

	
	////////////////////////////////////////////////////////////////////////////////////
	//////BEGIN DEMO METHODS
	////////////////////////////////////////////////////////////////////////////////////
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
	//true returns a string of indices (indices are +1 to match display)
	//false returns a string of actual tile values
	public String partnerIndicesString(int meldType, boolean actualTiles){
		
		String partnersString = "";
		ArrayList<Integer> wantedIndices = null;
		
		switch (meldType){
		case Meld.MELD_TYPE_CHI_L:
			wantedIndices = mPartnerIndicesChiL; break;
		case Meld.MELD_TYPE_CHI_M:
			wantedIndices = mPartnerIndicesChiM; break;
		case Meld.MELD_TYPE_CHI_H:
			wantedIndices = mPartnerIndicesChiH; break;
		case Meld.MELD_TYPE_PON:
			wantedIndices = mPartnerIndicesPon; break;
		case Meld.MELD_TYPE_KAN:
			wantedIndices = mPartnerIndicesKan; break;
		default: return "none";
		}
		
		for (Integer i: wantedIndices)
			if (actualTiles)
				partnersString += mTiles.get(i).toString() + ", ";
			else
				partnersString += Integer.toString(i+1) + ", ";
		
		return partnersString;
	}
	public String partnerIndicesString(int meldType){return partnerIndicesString(meldType, false);}
	////////////////////////////////////////////////////////////////////////////////////
	//////END DEMO METHODS
	////////////////////////////////////////////////////////////////////////////////////
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	//prints all melds to the screen
	public void showMelds(){
		for (int i = 0; i < mMelds.size(); i++)
			System.out.println("Meld " + (i+1) + ": " + mMelds.get(i).toString());
	}
	
	
	
	
	
	//returns string of all tiles in the hand, and their indices
	@Override
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
