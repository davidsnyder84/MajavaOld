import java.util.ArrayList;

import utility.GenSort;

/*
Class: Hand
represents a player's hand of tiles

data:
	mTiles - list of tiles in the hand
	mMelds - list of melds made from the hand
	
	mClosed - is true if the hand is fully concealed (no calls made on other discards)
	mTenpaiStatus - is true if the hand is in tenpai (one tile away from winning)
	mNumMeldsMade - the number of melds made
	mOwnerSeatWind - holds the wind of the player who owns the hand
	
	mCallCandidate - the most recently discarded tile. checks are run on it to see if it can be called.
	mCanChiL, mCanChiM, mCanChiH, mCanPon, mCanKan, mCanRon - flags, set to true if a call can be made on mCallCandidate
	mPartnerIndicesChiL, etc - hold the hand indices of meld partners for mCallCandidate, if it can be called
	
methods:
	
	constructors:
	1-arg, takes player's seat wind - creates list of tiles and melds, initializes flags and hand info
	copy constructor, makes an exact copy of a hand
	
	mutators:
	addTile - adds a tile to the hand (overloaded for actual tile, or an integer tile ID)
	removeTile - removes the tile at the given index, and checks if removing the tile puts the hand in tenpai
	sortHand - sorts the hand in ascending order
 	
 	accessors:
 	contains - returns true if the hand contains a copy of the given tile
	getTile - returns the tile at the given index in the hand
	getSize - returns the number of tiles in the hand
	isClosed - returns true if the hand is fully concealed, false if an open meld has been made
	getNumMeldsMade - returns the number of melds made
	findAllHotTiles - returns a list of hot tile IDs for ALL tiles in the hand
	ableToChiL, ableToChiM, ableToChiH, ableToPon, ableToKan, ableToRon - return true if a call can be made on mCallCandidate
	getTenpaiStatus - returns true if the hand is in tenpai
	
	private:
	__resetCallableFlags - resets call flags to false, creates new empty partner index lists
	__canChiL - checks if new tile can make a Chi-L
	__canChiM - checks if new tile can make a Chi-M
	__canChiH - checks if new tile can make a Chi-H
	__canChiType - checks if mCallCandidate can make a chi. populates chi partner list, sets canChi flag, returns true if chi is possible
	__canPon - checks if mCallCandidate can make a pon, populates pon partner list, sets canPon flag, returns true if pon is possible
	__canKan - checks if mCallCandidate can make a kan, populates kan partner list, sets canKan flag, returns true if kan is possible
	__canPair - checks if mCallCandidate can make a pair, populates pair partner list, sets canPair flag, returns true if pair is possible
	__howManyOfThisTileInHand - returns how many copies of a tile are in the hand, can fill a list with the indexes where the tile is found
	__canRon - returns true if the player can call ron on the candidate tile
	__storePartnerIndices - takes a list and several integer indices, stores the indices in the list
	__updateClosedStatus - checks if the hand is closed or open, ajusts flag accordingly
	__updateTenpaiStatus - checks if the hand is in tenpai, adjusts flag accordingly
	
	
	other:
	checkCallableTile - checks if a tile is callable. if it is callable, sets flag and populates partner index lists for that call
	makeMeld - forms a meld of the given type with mCallCandidate
	showMelds - prints all melds to the screen
	toString - returns string of all tiles in the hand, and their indices
*/
public class Hand {
	

	public static final int MAX_HAND_SIZE = 14;
	public static final int MAX_NUM_MELDS = 5;
	public static final int MIN_NUM_TILES_PER_MELD = 3;
	
	
	public static final int NUM_TILES_NEEDED_TO_CHI = 2;
	public static final int NUM_TILES_NEEDED_TO_PON = 2;
	public static final int NUM_TILES_NEEDED_TO_KAN = 3;
	public static final int NUM_TILES_NEEDED_TO_PAIR = 1;
	
	public static final boolean DEFAULT_CLOSED_STATUS = true;
	
	private static final int NOT_FOUND = -1;
	
	
	
	
	
	private ArrayList<Tile> mTiles;
	private ArrayList<Meld> mMelds;
	
	private boolean mClosed;
	private boolean mTenpaiStatus;
	private int mNumMeldsMade; 
	
	private char mOwnerSeatWind;
	

	private boolean mCanChiL;
	private boolean mCanChiM;
	private boolean mCanChiH;
	private boolean mCanPon;
	private boolean mCanKan;
	private boolean mCanRon;
	private boolean mCanPair;
	private ArrayList<Integer> mPartnerIndicesChiL;
	private ArrayList<Integer> mPartnerIndicesChiM;
	private ArrayList<Integer> mPartnerIndicesChiH;
	private ArrayList<Integer> mPartnerIndicesPon;
	private ArrayList<Integer> mPartnerIndicesKan;
	private ArrayList<Integer> mPartnerIndicesPair;
	private Tile mCallCandidate;
	
	
	
	
	//1-arg constructor, takes player's seat wind
	public Hand(char playerWind){
		mTiles = new ArrayList<Tile>(MAX_HAND_SIZE);
		mMelds = new ArrayList<Meld>(MAX_NUM_MELDS);
		
		mClosed = DEFAULT_CLOSED_STATUS;
		mTenpaiStatus = false;
		mNumMeldsMade = 0;
		mOwnerSeatWind = playerWind;
		
		//reset callable flags
		__resetCallableFlags();
		mCallCandidate = null;
		
	}
	public Hand(){
		this(Player.SEAT_UNDECIDED);
	}
	//copy constructor, makes an exact copy of a hand
	public Hand(Hand other){

		for (Tile t: other.mTiles) mTiles.add(t);
		for (Meld m: other.mMelds) mMelds.add(new Meld(m));

		this.__resetCallableFlags();
		mCallCandidate = other.mCallCandidate;
		mCanChiL = other.mCanChiL;
		mCanChiM = other.mCanChiM;
		mCanChiH = other.mCanChiH;
		mCanPon = other.mCanPon;
		mCanKan = other.mCanKan;
		mCanRon = other.mCanRon;
		mCanPair = other.mCanPair;
		for (Integer i: other.mPartnerIndicesChiL) mPartnerIndicesChiL.add(i);
		for (Integer i: other.mPartnerIndicesChiM) mPartnerIndicesChiM.add(i);
		for (Integer i: other.mPartnerIndicesChiH) mPartnerIndicesChiH.add(i);
		for (Integer i: other.mPartnerIndicesPon) mPartnerIndicesPon.add(i);
		for (Integer i: other.mPartnerIndicesKan) mPartnerIndicesKan.add(i);
		for (Integer i: other.mPartnerIndicesPair) mPartnerIndicesPair.add(i);
		
		mClosed = other.mClosed;
		mTenpaiStatus = other.mTenpaiStatus;
		mNumMeldsMade = other.mNumMeldsMade;
		mOwnerSeatWind = other.mOwnerSeatWind;
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
	public boolean isClosed(){return mClosed;}
	//returns the number of melds made
	public int getNumMeldsMade(){return mNumMeldsMade;}
	//returns the hand owner's seat wind
	public char getOwnerSeatWind(){return mOwnerSeatWind;}
	//returns true if the hand is in tenpai
	public boolean getTenpaiStatus(){return mTenpaiStatus;}
	
	
	
	
	
	
	
	//adds a tile to the hand (cannot add more than max hand size)
	public boolean addTile(Tile t)
	{
		if (mTiles.size() < MAX_HAND_SIZE - MIN_NUM_TILES_PER_MELD*mNumMeldsMade)
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
	
	
	
	
	/*
	method: removeTile
	removes the tile at the given index, and checks if removing the tile puts the hand in tenpai
	returns true if successful, returns false if index is outside of the hand's range
	
	remove the tile
	sort the hand
	check if removing the tile put the hand in tenpai (modifies mTenpaiStatus flag)
	*/
	public boolean removeTile(int index){
		if (index >= 0 && index < mTiles.size()){
			//remove the tile
			mTiles.remove(index);
			
			//sort hand
			sortHand();
			//check if removing the tile put the hand in tenpai
			checkIfTenpai();
			return true;
		}
		return false;
	}

	
	//returns true if the hand contains an occurence of tile t
	public boolean contains(Tile t){
		return mTiles.contains(t);
	}
	//overloaded to accept tile id instead of actual tile
	public boolean contains(int id){return contains(new Tile(id));}
	
	
	
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
	
	input: storePartnersHere is the list that will hold the partner indices, if chi is possible
	 	   offset1 and offset2 are the offsets of mCallCandidate's ID to look for
	
	returns true if chi type is possible, populates the received chi partner list, sets canChi flag
	
	
	partnerIndex1/2 = search hand for (mCallCandidate's ID + offset1/2), take first occurence of each
	if (indexes were found for both partners)
		can = true
		store partner indices in the received list
	end if
	return can
	*/
	private boolean __canChiType(ArrayList<Integer> storePartnersHere, int offset1, int offset2){
		
		//if this is true, the player can make a chi
		boolean can = false;
		
		//decide who the chi partners should be (offset is decided based on chi type)
		//search the hand for the desired chi partners (get the indices)
		int partnerIndex1 = NOT_FOUND, partnerIndex2 = NOT_FOUND;
		partnerIndex1 = mTiles.indexOf(new Tile(mCallCandidate.getId() + offset1));
		partnerIndex2 = mTiles.indexOf(new Tile(mCallCandidate.getId() + offset2));
		
		
		//if both parters were found in the hand
		if (partnerIndex1 != NOT_FOUND && partnerIndex2 != NOT_FOUND){
			can = true;
			
			//sore the indices of the partners in a partner list
			__storePartnerIndices(storePartnersHere, partnerIndex1, partnerIndex2);
		}
		return can;
	}
	
	//done
	private boolean __canChiL(){
		if (mCallCandidate.getFace() == '8' || mCallCandidate.getFace() == '9') return false;
		return mCanChiL = __canChiType(mPartnerIndicesChiL, 1, 2);
	}
	private boolean __canChiM(){
		if (mCallCandidate.getFace() == '1' || mCallCandidate.getFace() == '9') return false;
		return mCanChiM = __canChiType(mPartnerIndicesChiM, -1, 1);
	}
	private boolean __canChiH(){
		if (mCallCandidate.getFace() == '1' || mCallCandidate.getFace() == '2') return false;
		return mCanChiH = __canChiType(mPartnerIndicesChiH, -2, -1);
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
		ArrayList<Integer> tempPartnerIndices = new ArrayList<Integer>(NUM_TILES_NEEDED_TO_PON);
		
		//count how many occurences fo the tile, and store the indices of the occurences in tempPartnerIndices
		int count = __howManyOfThisTileInHand(mCallCandidate, tempPartnerIndices);
		
		//pon is possible if there are enough occurences of the tile in the hand to form a pon
		if (count >= NUM_TILES_NEEDED_TO_PON){
			
			//store the partner indices in the pon partner index list
			__storePartnerIndices(mPartnerIndicesPon, tempPartnerIndices.get(0), tempPartnerIndices.get(1));
			
			can = mCanPon = true;	//can = true
		}
		return can;
	}
	
	/*
	private method: __canKan
	checks if a kan can be made with the new tile
	populates the kan partner list, sets canKan flag, and returns true if kan is possible
	*/
	private boolean __canKan(){
		
		boolean can = false;
		ArrayList<Integer> tempPartnerIndices = new ArrayList<Integer>(NUM_TILES_NEEDED_TO_KAN);
		
		//count how many occurences fo the tile, and store the indices of the occurences in tempPartnerIndices
		int count = __howManyOfThisTileInHand(mCallCandidate, tempPartnerIndices);
		
		//kan is possible if there are enough occurences of the tile in the hand to form a kan
		if (count >= NUM_TILES_NEEDED_TO_KAN){
			
			//store the partner indices in the kan partner index list
			__storePartnerIndices(mPartnerIndicesKan, tempPartnerIndices.get(0), tempPartnerIndices.get(1), tempPartnerIndices.get(2));
			
			can = mCanKan = true;	//can = true
		}
		return can;
	}
	
	/*
	private method: __canPair
	checks if a pair can be made with the new tile
	populates the pair partner list, sets canPair flag, and returns true if pair is possible
	*/
	private boolean __canPair(){
		
		boolean can = false;
		ArrayList<Integer> tempPartnerIndices = new ArrayList<Integer>(NUM_TILES_NEEDED_TO_PAIR);
		
		//count how many occurences fo the tile, and store the indices of the occurences in tempPartnerIndices
		int count = __howManyOfThisTileInHand(mCallCandidate, tempPartnerIndices);
		
		//pair is possible if there are enough occurences of the tile in the hand to form a pair (need only one other copy of the tile in the hand)
		if (count >= NUM_TILES_NEEDED_TO_PAIR){
			
			//store the partner indices in the pair partner index list
			__storePartnerIndices(mPartnerIndicesPair, tempPartnerIndices.get(0));
			
			can = mCanPair = true;	//can = true
		}
		return can;
	}
	
	
	
	
	/*
	private method: __howManyOfThisTileInHand
	returns how many copies of tile t are in the hand
	populates a list with the indices where t occurs, if one is provided
	
	input: t is the tile to look for, storeIndicesHere will hold the indices of the occurences of the tile
	
	count = 0
	for (each tile in the hand)
		if (handtile and t have the same id)
			count++
			if (a non-null list was provided)
				if (t and handtile are not the same physical tile): add the current index to the list
		end if
	end for
	return count
	*/
	private int __howManyOfThisTileInHand(Tile t, ArrayList<Integer> storeIndicesHere){
		
		int count = 0;
		for (int i = 0; i < mTiles.size(); i++)
		{
			//if handtile and t have the same ID, but aren't the same phsyical tile
			//(basically, don't count t as its own partner)
			if (mTiles.get(i).equals(t) && mTiles.get(i) != t)	
			{
				count++;
				//store the index in the list, if one was provided
				if (storeIndicesHere != null)
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
		//don't check chi if mCallCandidate is an honor tile
		if (!mCallCandidate.isHonor() && (
			(candidate.getOrignalOwner() == mOwnerSeatWind) || 
			(candidate.getOrignalOwner() == Player.findKamichaOf(mOwnerSeatWind))) ){
			mCanChiL = __canChiL();
			mCanChiM = __canChiM();
			mCanChiH = __canChiH();
		}

		//check pair. if can't pair, don't bother checking pon. check pon. if can't pon, don't bother checking kan.
		if (mCanPair = __canPair())
			if (mCanPon = __canPon())
				mCanKan = __canKan();
		
		//check ron
		mCanRon = __canRon();
		
		//////return true if a call (any call) can be made
		return (mCanChiL || mCanChiM || mCanChiH || mCanPon || mCanKan || mCanRon);
	}
	
	
	//resets call flags to false, creates new empty partner index lists
	private void __resetCallableFlags(){
		mCanChiL = mCanChiM = mCanChiH = mCanPon = mCanKan = mCanRon = mCanPair = false;
		mPartnerIndicesChiL = new ArrayList<Integer>(NUM_TILES_NEEDED_TO_CHI);
		mPartnerIndicesChiM = new ArrayList<Integer>(NUM_TILES_NEEDED_TO_CHI);
		mPartnerIndicesChiH = new ArrayList<Integer>(NUM_TILES_NEEDED_TO_CHI);
		mPartnerIndicesPon = new ArrayList<Integer>(NUM_TILES_NEEDED_TO_PON);
		mPartnerIndicesKan = new ArrayList<Integer>(NUM_TILES_NEEDED_TO_KAN);
		mPartnerIndicesPair = new ArrayList<Integer>(NUM_TILES_NEEDED_TO_PAIR);
	}
	
	//takes a list and several integer indices, stores the indices in the list
	private void __storePartnerIndices(ArrayList<Integer> storeHere, int... partnerIndices){
		for (int i: partnerIndices)
			storeHere.add(i);
	}
	
	
	//returns true if a specific call can be made on mCallCandidate
	public boolean ableToChiL(){return mCanChiL;}
	public boolean ableToChiM(){return mCanChiM;}
	public boolean ableToChiH(){return mCanChiH;}
	public boolean ableToPon(){return mCanPon;}
	public boolean ableToKan(){return mCanKan;}
	public boolean ableToRon(){return mCanRon;}
	public boolean ableToPair(){return mCanPair;}
	//public boolean ableToPair(){return (mTiles.contains(mCallCandidate));}
	
	//returns the number of different calls possible for callCandidate
	public int numberOfCallsPossible(){
		int count = 0;
		if (mCanChiL) count++;
		if (mCanChiM) count++;
		if (mCanChiH) count++;
		if (mCanPon) count++;
		if (mCanKan) count++;
		//if (mCanRon) count++;
		return count;
	}
	
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
	update hand's closed status after making the meld
	*/
	public void makeMeld(int meldType){
		
		//list of tiles, will hold the tiles coming form the hand that will be in the meld
		ArrayList<Tile> tilesFromHand = new ArrayList<Tile>(4);
		
		//figure out which partner list to use for the indices, based on the call type
		ArrayList<Integer> partnerIndices = null;
		if (meldType == Meld.MELD_TYPE_CHI_L) partnerIndices = mPartnerIndicesChiL;
		else if (meldType == Meld.MELD_TYPE_CHI_M) partnerIndices = mPartnerIndicesChiM;
		else if (meldType == Meld.MELD_TYPE_CHI_H) partnerIndices = mPartnerIndicesChiH;
		else if (meldType == Meld.MELD_TYPE_PON) partnerIndices = mPartnerIndicesPon;
		else if (meldType == Meld.MELD_TYPE_KAN) partnerIndices = mPartnerIndicesKan;
		
		
		//add the tiles from the hand to the list
		for (Integer index: partnerIndices)
			tilesFromHand.add(mTiles.get(index));
		
		//make the meld, add to the list of melds
		mMelds.add(new Meld(tilesFromHand, mCallCandidate, meldType));
		
		//remove the tiles from the hand
		while (partnerIndices.isEmpty() == false){
			removeTile(partnerIndices.get(partnerIndices.size() - 1).intValue());
			partnerIndices.remove(partnerIndices.size() - 1);
		}
		mNumMeldsMade++;
		
		//update the hand's closed status after making the meld
		__updateClosedStatus();
		
	}
	
	
	//checks if the hand is closed or open, ajusts flag accordingly
	private boolean __updateClosedStatus(){
		boolean meldsAreAllClosed = true;
		//if all the melds are closed, then the hand is closed
		for (Meld m: mMelds) meldsAreAllClosed = (meldsAreAllClosed && m.isClosed());
		
		return mClosed = meldsAreAllClosed;
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
	
	
	
	
	public void findAllMachis(){
		
		
	}
	
	
	
	
	
	
	
	

	//***************************************************************************************************
	//****BEGIN TENPAI CHECKERS
	//***************************************************************************************************
	
	//returns true if the hand is in tenpai for kokushi musou
	//if this is true, it means that: handsize >= 13, hand has at least 12 different TYC tiles
	public boolean kokushiMusouInTenpai(){
		
		boolean couldBeKokushi = true;
		
		//if any melds have been made, kokushi musou is impossible
		if (mNumMeldsMade > 0) return false;
		
		
		//couldBeKokushi will be set to false if the hand contains a non-honor tile
		for (Tile t: mTiles) couldBeKokushi = (couldBeKokushi && t.isYaochuu());
		if (couldBeKokushi == false) return false;
		
		//check if the hand contains at least 12 different TYC tiles
		ArrayList<Tile> listTYC = Tile.listOfYaochuuTiles();
		int countTYC = 0;
		for (int i = 0; i < Tile.NUMBER_OF_YAOCHUU_TILES; i++)
			if (mTiles.contains(listTYC.get(i)))
				countTYC++;

		//return false if the hand doesn't contain at least 12 different TYC tiles
		if (countTYC < Tile.NUMBER_OF_YAOCHUU_TILES - 1) return false;
		
		
		return true;
	}
	
	//returns true if a 14-tile hand is a complete kokushi musou
	public boolean kokushiMusouIsComplete(){
		
		if ((mTiles.size() == MAX_HAND_SIZE) &&
			(kokushiMusouInTenpai() == true) &&
			(kokushiMusouWaits().size() == Tile.NUMBER_OF_YAOCHUU_TILES))
			return true;
		
		
		return false;
	}

	//returns a list of the hand's waits, if it is in tenpai for kokushi musou
	//returns an empty list if not in kokushi musou tenpai
	public ArrayList<Tile> kokushiMusouWaits(){
		
		ArrayList<Tile> waits = new ArrayList<Tile>(1);
		
		Tile missingTYC = null;
		if (kokushiMusouInTenpai() == true)
		{
			//look for a Yaochuu tile that the hand doesn't contain
			ArrayList<Tile> listTYC = Tile.listOfYaochuuTiles();
			for (Tile t: listTYC)
				if (mTiles.contains(t) == false)
					missingTYC = t;
			
			//if the hand contains exactly one of every Yaochuu tile, then it is a 13-sided wait for all Yaochuu tiles
			if (missingTYC == null)
				waits = listTYC;
			else
				//else, if the hand is missing a Yaochuu tile, that missing tile is the hand's wait
				waits.add(missingTYC);
		}
		
		return waits;
	}
	
	
	
	//checks if the hand is in tenpai
	//sets mTenpaiStaus flag if it is, and returns true
	public boolean checkIfTenpai(){
		
		boolean isTenpai = false;
		
		isTenpai = (isTenpai || kokushiMusouInTenpai());
		
		mTenpaiStatus = isTenpai;
		return mTenpaiStatus;
	}
	public boolean __updateTenpaiStatus(){return checkIfTenpai();}
	
	
	

	//***************************************************************************************************
	//****END TENPAI CHECKERS
	//***************************************************************************************************
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

	
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
	public String partnerIndicesString(int meldType, boolean wantActualTiles){
		
		String partnersString = "";
		ArrayList<Integer> wantedIndices = null;
		
		switch (meldType){
		case Meld.MELD_TYPE_CHI_L: wantedIndices = mPartnerIndicesChiL; break;
		case Meld.MELD_TYPE_CHI_M: wantedIndices = mPartnerIndicesChiM; break;
		case Meld.MELD_TYPE_CHI_H: wantedIndices = mPartnerIndicesChiH; break;
		case Meld.MELD_TYPE_PON: wantedIndices = mPartnerIndicesPon; break;
		case Meld.MELD_TYPE_KAN: wantedIndices = mPartnerIndicesKan; break;
		case Meld.MELD_TYPE_PAIR: wantedIndices = mPartnerIndicesPair; break;
		default: return "none";
		}
		
		for (Integer i: wantedIndices)
			if (wantActualTiles) partnersString += mTiles.get(i).toString() + ", ";
			else partnersString += Integer.toString(i+1) + ", ";
		
		if (partnersString != "") partnersString = partnersString.substring(0, partnersString.length() - 2);
		return partnersString;
	}
	public String partnerIndicesString(int meldType){return partnerIndicesString(meldType, false);}
	////////////////////////////////////////////////////////////////////////////////////
	//////END DEMO METHODS
	////////////////////////////////////////////////////////////////////////////////////
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	//prints all melds to the screen
	public void showMelds(){
		for (int i = 0; i < mMelds.size(); i++)
			System.out.println("+++Meld " + (i+1) + ": " + mMelds.get(i).toString());
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
