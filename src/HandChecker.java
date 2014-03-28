import java.util.ArrayList;

import utility.MahList;


/*
Class: HandChecker
runs various checks on a player's hand

data:
	mHand - the hand that this object will be checking
	mHandTiles - list of mHand's tiles
	mHandMelds - list of mHand's melds
	
	mClosed - is true if the hand is fully concealed (no calls made on other discards)
	mTenpaiStatus - is true if the hand is in tenpai (one tile away from winning)
	
	mCallCandidate - the most recently discarded tile. checks are run on it to see if it can be called.
	mCanChiL, mCanChiM, mCanChiH, mCanPon, mCanKan, mCanRon - flags, set to true if a call can be made on mCallCandidate
	mPartnerIndicesChiL, etc - hold the hand indices of meld partners for mCallCandidate, if it can be called
	
methods:
	
	constructors:
	takes a hand, a list of the hand's tiles, and a list of the hand's melds (creates a LINK between this and the hand's tiles/melds)
 	
 	accessors:
	getClosedStatus - returns true if the hand is fully concealed, false if an open meld has been made
	getTenpaiStatus - returns true if the hand is in tenpai
	findAllHotTiles - returns a list of hot tile IDs for ALL tiles in the hand
	findAllCallableTiles - returns a list of callable tile IDs for ALL tiles in the hand
	ableToChiL, ableToChiM, ableToChiH, ableToPon, ableToKan, ableToRon - return true if a call can be made on mCallCandidate
	numberOfCallsPossible - returns the number of different calls possible for mCallCandidate
	getCallCandidate - returns mCallCandidate
	getPartnerIndices - returns the partner indices list for a given meld type
	
	
	private:
	__resetCallableFlags - resets call flags to false, creates new empty partner index lists
	__storePartnerIndices - takes a partner list and a list integer indices, stores the indices in the partner list
	__canRon - returns true if the player can call ron on the candidate tile
	
	__canChiType - checks if a candidate can make a chiL/M/H. populates chiL/M/H partner list, returns true if chiL/M/H is possible
	__canChiL - checks if a candidate can make a Chi-L
	__canChiM - checks if a candidate can make a Chi-M
	__canChiH - checks if a candidate can make a Chi-H
	
	__canMultiType - checks if a candidate can make a pair/pon/kan. populates pair/pon/kan partner list and returns true if a pair/pon/kan possible
	__canPair - checks if a candidate can make a pair
	__canPon - checks if a candidate can make a pon
	__canKan - checks if a candidate can make a kan
	__howManyOfThisTileInHand - returns how many copies of a tile are in the hand, can fill a list with the indices where the tile is found
	
	__checkIfTenpai - checks if the hand is in tenpai, sets mTenpaiStaus flag if it is, and returns true
	kokushiMusouInTenpai - returns true if the hand is in tenpai for kokushi musou
	kokushiMusouIsComplete - returns true if a 14-tile hand is a complete kokushi musou
	kokushiMusouWaits - returns a list of the hand's waits, if it is in tenpai for kokushi musou
	
	
	public:
	checkCallableTile - checks if a tile is callable. if it is callable, sets flag and populates partner index lists for that call
	updateClosedStatus - checks if the hand is closed or open, sets flag accordingly
	updateTenpaiStatus - checks if the hand is in tenpai, sets flag accordingly
*/
public class HandChecker {
	
	
	//private static final int NOT_FOUND = -1;
	public static final boolean DEFAULT_CLOSED_STATUS = true;
	
	public static final int NUM_TILES_NEEDED_TO_CHI = 2;
	public static final int NUM_TILES_NEEDED_TO_PON = 2;
	public static final int NUM_TILES_NEEDED_TO_KAN = 3;
	public static final int NUM_TILES_NEEDED_TO_PAIR = 1;
	
	private static final int OFFSET_CHI_L1 = 1;
	private static final int OFFSET_CHI_L2 = 2;
	private static final int OFFSET_CHI_M1 = -1;
	private static final int OFFSET_CHI_M2 = 1;
	private static final int OFFSET_CHI_H1 = -2;
	private static final int OFFSET_CHI_H2 = -1;
	
	
	
	
	
	
	private Hand mHand;
	private ArrayList<Tile> mHandTiles;
	private ArrayList<Meld> mHandMelds;
	
	private boolean mTenpaiStatus;
	private boolean mClosed;
	
	
	//call flags and partner index lists
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
	
	
	
	
	//creates a LINK between this and the hand's tiles/melds
	public HandChecker(Hand hand, ArrayList<Tile> handTiles, ArrayList<Meld> handMelds){
		mHand = hand;
		mHandTiles = handTiles;
		mHandMelds = handMelds;

		mTenpaiStatus = false;
		mClosed = DEFAULT_CLOSED_STATUS;
		
		//reset callable flags
		__resetCallableFlags();
		mCallCandidate = null;
	}
	//copy constructor, makes another checker for the hand
	//creates a COPY OF the other checker tiles/melds lists
	public HandChecker(HandChecker other){

		for (Tile t: other.mHandTiles) mHandTiles.add(t);
		for (Meld m: other.mHandMelds) mHandMelds.add(new Meld(m));

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
		
		mTenpaiStatus = other.mTenpaiStatus;
		mClosed = other.mClosed;
	}
	/*
	//creates a COPY of the hand's tiles/melds to check (don't use this unless you NEED a copy)
	public HandChecker(Hand hand){
		mHand = hand;
		mHandTiles = new ArrayList<Tile>(mHand.getSize());
		for (Tile t: mHand) mHandTiles.add(t);	//copy tiles into this checker's lsit
		//copy melds (not done this yet)
	}
	*/
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	//~~~~BEGIN MELD CHEKCERS
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/*
	private method: __canChiType
	checks if a candidate tile can make a chi with other tiles in the hand
	
	input: candidate is the tile to search for chi partners for
		   storePartnersHere is the list that will hold the partner indices, if chi is possible
	 	   offset1 and offset2 are the offsets of mCallCandidate's ID to look for
	
	if chi type is possible: populates the meld partner list and returns true
	
	
	tempPartnerIndices = search hand for (candidate's ID + offset1/2), take first occurence of each
	if (indexes were found for both partners)
		store partner indices in the received list
		return true
	end if
	return false
	*/
	private boolean __canChiType(Tile candidate, ArrayList<Integer> storePartnersHere, int offset1, int offset2){
		
		//decide who the chi partners should be (offset is decided based on chi type)
		//search the hand for the desired chi partners (get the indices)
		MahList<Integer> tempPartnerIndices = new MahList<Integer>(NUM_TILES_NEEDED_TO_CHI);
		tempPartnerIndices.add(mHandTiles.indexOf(new Tile(candidate.getId() + offset1)));
		tempPartnerIndices.add(mHandTiles.indexOf(new Tile(candidate.getId() + offset2)));
		
		//if both parters were found in the hand
		if (tempPartnerIndices.getFirst() != MahList.NOT_FOUND && tempPartnerIndices.getLast() != MahList.NOT_FOUND){
			
			//sore the indices of the partners in a partner list
			__storePartnerIndices(storePartnersHere, tempPartnerIndices);
			
			return true;
		}
		return false;
	}
	private boolean __canChiL(Tile candidate){
		if (candidate.getFace() == '8' || candidate.getFace() == '9') return false;
		return __canChiType(candidate, mPartnerIndicesChiL, OFFSET_CHI_L1, OFFSET_CHI_L2);
	}
	private boolean __canChiM(Tile candidate){
		if (candidate.getFace() == '1' || candidate.getFace() == '9') return false;
		return __canChiType(candidate, mPartnerIndicesChiM, OFFSET_CHI_M1, OFFSET_CHI_M2);
	}
	private boolean __canChiH(Tile candidate){
		if (candidate.getFace() == '1' || candidate.getFace() == '2') return false;
		return __canChiType(candidate, mPartnerIndicesChiH, OFFSET_CHI_H1, OFFSET_CHI_H2);
	}
	//overloaded. if no tile argument given, candidate = mCallCandidate is passsed
	private boolean __canChiL(){return  __canChiL(mCallCandidate);}
	private boolean __canChiM(){return  __canChiM(mCallCandidate);}
	private boolean __canChiH(){return  __canChiH(mCallCandidate);}
	
	
	
	/*
	private method: __canMultiType
	checks if a multi (pair/pon/kan) can be made with the new tile
	
	input: candidate is the tile to search for chi partners for
		   storePartnersHere is the list that will hold the partner indices, if chi is possible
	 	   offset1 and offset2 are the offsets of mCallCandidate's ID to look for
	 	   
	if the multi type is possible: populates the meld partner list and returns true
	
	
	if (there are enough partner tile in the hand to form the multi)
		store the partner indices in the storePartnersHere list
		return true
	end if
	else return false
	*/
	private boolean __canMultiType(Tile candidate, ArrayList<Integer> storePartnersHere, int numPartnersNeeded){
		
		//count how many occurences of the tile, and store the indices of the occurences in tempPartnerIndices
		MahList<Integer> tempPartnerIndices = new MahList<Integer>(numPartnersNeeded);
		
		//meld is possible if there are enough partners in the hand to form the meld
		if (howManyOfThisTileInHand(candidate, tempPartnerIndices.getArrayList()) >= numPartnersNeeded){

			//store the partner indices in the pon partner index list
			__storePartnerIndices(storePartnersHere, tempPartnerIndices.subList(0, numPartnersNeeded));
			
			return true;
		}
		return false;
	}
	private boolean __canPair(Tile candidate){
		return __canMultiType(candidate, mPartnerIndicesPair, NUM_TILES_NEEDED_TO_PAIR);
	}
	private boolean __canPon(Tile candidate){
		return __canMultiType(candidate, mPartnerIndicesPon, NUM_TILES_NEEDED_TO_PON);
	}
	private boolean __canKan(Tile candidate){
		return __canMultiType(candidate, mPartnerIndicesKan, NUM_TILES_NEEDED_TO_KAN);
	}
	//overloaded. if no tile argument given, candidate = mCallCandidate is passsed
	private boolean __canPair(){return __canPair(mCallCandidate);}
	private boolean __canPon(){return __canPon(mCallCandidate);}
	private boolean __canKan(){return __canKan(mCallCandidate);}
	
	
	
	/*
	private method: howManyOfThisTileInHand
	returns how many copies of tile t are in the hand
	
	input: t is the tile to look for. if a list is provided, storeIndicesHere will be populated with the indices where t occurs 
	
	current = find first index of t in the hand, return 0 if not found
	while ((current < hand size) && (hand(current) and t have the same id))
		if (hand(current) and t are not the same physical tile): add the current index to the list
		current++
	end while
	return (number of indices found)
	*/
	public int howManyOfThisTileInHand(Tile t, ArrayList<Integer> storeIndicesHere){
		//finds the first occurence of t. if t is not found, returns 0
		int current = mHandTiles.indexOf(t);
		if (current == MahList.NOT_FOUND) return 0;
		if (storeIndicesHere == null) storeIndicesHere = new ArrayList<Integer>();
		
		//store the current (first) index in the list, then move current the next index
		storeIndicesHere.add(current++);
		
		//loops until it finds a tile that is not t (assumes the hand is sorted)
		while (current < mHandTiles.size() && mHandTiles.get(current).equals(t)){
			//if handtile and t aren't the same phsyical tile... (basically, don't count t as its own partner)
			if (mHandTiles.get(current) != t) storeIndicesHere.add(current);
			current++;
		}
		return storeIndicesHere.size();
	}
	//overloaded, omitting list argument simply returns the count, and doesn't populate any list
	public int howManyOfThisTileInHand(Tile t){
		return howManyOfThisTileInHand(t, null);
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
		
		//~~~~reset flags
		__resetCallableFlags();
		//set mCallCandidate = candidate
		mCallCandidate = candidate;
		
		//~~~~runs checks, set flags to the check results
		//only allow chis from the player's kamicha, or from the player's own tiles. don't check chi if candidate is an honor tile
		if (!candidate.isHonor() && (
			(candidate.getOrignalOwner() == mHand.getOwnerSeatWind()) || 
			(candidate.getOrignalOwner() == Player.findKamichaOf(mHand.getOwnerSeatWind()))) ){
			mCanChiL = __canChiL();
			mCanChiM = __canChiM();
			mCanChiH = __canChiH();
		}

		//check pair. if can't pair, don't bother checking pon. check pon. if can't pon, don't bother checking kan.
		if (mCanPair = __canPair())
			if (mCanPon = __canPon())
				mCanKan = __canKan();
		
		//if in tenpai, check ron
		if (mTenpaiStatus == true)
			mCanRon = __canRon();
		
		//~~~~return true if a call (any call) can be made
		return (mCanChiL || mCanChiM || mCanChiH || mCanPon || mCanKan || mCanRon);
	}
	
	
	
	
	//takes a list and several integer indices, stores the indices in the list
	private void __storePartnerIndices(ArrayList<Integer> storeHere, MahList<Integer> partnerIndices){
		for (int i: partnerIndices)
			storeHere.add(i);
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
	
	
	//returns the number of different calls possible for mCallCandidate
	public int numberOfCallsPossible(){
		int count = 0;
		if (mCanChiL) count++;
		if (mCanChiM) count++;
		if (mCanChiH) count++;
		if (mCanPon) count++;
		if (mCanKan) count++;
		if (mCanRon) count++;
		return count;
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	//~~~~END MELD CHEKCERS
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	//---------------------------------------------------------------------------------------------------
	//----BEGIN FINDERS
	//---------------------------------------------------------------------------------------------------
	
	//returns a list of hot tile IDs for ALL tiles in the hand
	public ArrayList<Integer> findAllHotTiles(){

		ArrayList<Integer> allHotTileIds = new ArrayList<Integer>(16);
		ArrayList<Integer> singleTileHotTiles = null;
		
		//get hot tiles for each tile in the hand
		for (Tile t: mHandTiles)
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
	
	//---------------------------------------------------------------------------------------------------
	//----END FINDERS
	//---------------------------------------------------------------------------------------------------
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	//***************************************************************************************************
	//****BEGIN TENPAI CHECKERS
	//***************************************************************************************************
	
	//returns true if the hand is in tenpai for kokushi musou
	//if this is true, it means that: handsize >= 13, hand has at least 12 different TYC tiles
	public boolean kokushiMusouInTenpai(){
		
		boolean couldBeKokushi = true;
		
		//if any melds have been made, kokushi musou is impossible
		if (mHand.getNumMeldsMade() > 0) return false;
		
		
		//couldBeKokushi will be set to false if the hand contains a non-honor tile
		for (Tile t: mHandTiles) couldBeKokushi = (couldBeKokushi && t.isYaochuu());
		if (couldBeKokushi == false) return false;
		
		//check if the hand contains at least 12 different TYC tiles
		ArrayList<Tile> listTYC = Tile.listOfYaochuuTiles();
		int countTYC = 0;
		for (int i = 0; i < Tile.NUMBER_OF_YAOCHUU_TILES; i++)
			if (mHandTiles.contains(listTYC.get(i)))
				countTYC++;

		//return false if the hand doesn't contain at least 12 different TYC tiles
		if (countTYC < Tile.NUMBER_OF_YAOCHUU_TILES - 1) return false;
		
		
		return true;
	}
	
	//returns true if a 14-tile hand is a complete kokushi musou
	public boolean kokushiMusouIsComplete(){
		
		if ((mHandTiles.size() == Hand.MAX_HAND_SIZE) &&
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
				if (mHandTiles.contains(t) == false)
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
	private boolean __checkIfTenpai(){
		
		boolean isTenpai = false;
		
		isTenpai = (isTenpai || kokushiMusouInTenpai());
		
		mTenpaiStatus = isTenpai;
		return mTenpaiStatus;
	}
	public boolean updateTenpaiStatus(){return __checkIfTenpai();}
	

	//***************************************************************************************************
	//****END TENPAI CHECKERS
	//***************************************************************************************************
	
	
	
	
	
	
	
	
	
	//checks if the hand is closed or open, ajusts flag accordingly
	public boolean updateClosedStatus(){
		boolean meldsAreAllClosed = true;
		//if all the melds are closed, then the hand is closed
		for (Meld m: mHandMelds) meldsAreAllClosed = (meldsAreAllClosed && m.isClosed());
		
		return mClosed = meldsAreAllClosed;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	//returns the partner indices list for a given meld type
	public ArrayList<Integer> getPartnerIndices(int meldType){
		
		ArrayList<Integer> wantedIndices = null;
		switch (meldType){
		case Meld.MELD_TYPE_CHI_L: wantedIndices = mPartnerIndicesChiL; break;
		case Meld.MELD_TYPE_CHI_M: wantedIndices = mPartnerIndicesChiM; break;
		case Meld.MELD_TYPE_CHI_H: wantedIndices = mPartnerIndicesChiH; break;
		case Meld.MELD_TYPE_PON: wantedIndices = mPartnerIndicesPon; break;
		case Meld.MELD_TYPE_KAN: wantedIndices = mPartnerIndicesKan; break;
		case Meld.MELD_TYPE_PAIR: wantedIndices = mPartnerIndicesPair; break;
		default: break;
		}
		return wantedIndices;
	}
	
	//returns mCallCandidate
	public Tile getCallCandidate(){return mCallCandidate;}
	
	
	//returns true if a specific call can be made on mCallCandidate
	public boolean ableToChiL(){return mCanChiL;}
	public boolean ableToChiM(){return mCanChiM;}
	public boolean ableToChiH(){return mCanChiH;}
	public boolean ableToPon(){return mCanPon;}
	public boolean ableToKan(){return mCanKan;}
	public boolean ableToRon(){return mCanRon;}
	public boolean ableToPair(){return mCanPair;}
	
	
	
	//returns true if the hand is in tenpai
	public boolean getTenpaiStatus(){return mTenpaiStatus;}
	
	//returns true if the hand is fully concealed, false if an open meld has been made
	public boolean getClosedStatus(){return mClosed;}
	
	
	
	//satisfy compiler whining
	public void lookAtMelds(){mHandMelds.size();}//yep, i'm looking at them
	
	
	
}
