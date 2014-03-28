import java.util.ArrayList;

import utility.MahList;


public class HandChecker {
	
	
	private static final int NOT_FOUND = -1;
	
	
	
	
	
	
	
	
	
	
	
	
	private Hand mHand;
	private ArrayList<Tile> mHandTiles;
	
	private boolean mTenpaiStatus;
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
	
	
	
	
	//creates a LINK between this an the hand's tiles
	public HandChecker(Hand hand, ArrayList<Tile> handTiles){
		mHand = hand;
		mHandTiles = handTiles;
	}
	//creates a COPY of the hand's tiles to check (don't use this unless you NEED a copy)
	public HandChecker(Hand hand){
		mHand = hand;
		for (int i = 0; i < mHand.getSize(); i++) mHandTiles.add(mHand.getTile(i));
		
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
	private boolean __canChiType(Tile candidate, ArrayList<Integer> storePartnersHere, int offset1, int offset2){
		
		//if this is true, the player can make a chi
		boolean can = false;
		
		//decide who the chi partners should be (offset is decided based on chi type)
		//search the hand for the desired chi partners (get the indices)
		MahList<Integer> tempPartnerIndices = new MahList<Integer>(Hand.NUM_TILES_NEEDED_TO_CHI);
		tempPartnerIndices.add(mHandTiles.indexOf(new Tile(candidate.getId() + offset1)));
		tempPartnerIndices.add(mHandTiles.indexOf(new Tile(candidate.getId() + offset2)));
		
		//if both parters were found in the hand
		if (tempPartnerIndices.getFirst() != MahList.NOT_FOUND && tempPartnerIndices.getLast() != MahList.NOT_FOUND){
			can = true;
			
			//sore the indices of the partners in a partner list
			__storePartnerIndices(storePartnersHere, tempPartnerIndices);
		}
		return can;
	}
	private boolean __canChiL(Tile candidate){
		if (candidate.getFace() == '8' || candidate.getFace() == '9') return false;
		return mCanChiL = __canChiType(candidate, mPartnerIndicesChiL, 1, 2);
	}
	private boolean __canChiM(Tile candidate){
		if (candidate.getFace() == '1' || candidate.getFace() == '9') return false;
		return mCanChiM = __canChiType(candidate, mPartnerIndicesChiM, -1, 1);
	}
	private boolean __canChiH(Tile candidate){
		if (candidate.getFace() == '1' || candidate.getFace() == '2') return false;
		return mCanChiH = __canChiType(candidate, mPartnerIndicesChiH, -2, -1);
	}
	//overloaded. if no tile argument given, candidate = mCallCandidate is passsed
	private boolean __canChiL(){return  __canChiL(mCallCandidate);}
	private boolean __canChiM(){return  __canChiM(mCallCandidate);}
	private boolean __canChiH(){return  __canChiH(mCallCandidate);}
	
	
	
	/*
	private method: __canMultiType
	checks if a multi can be made with the new tile
	if the multi is possible: populates the meld partner list and returns true
	
	count = how many of this tile is in the hand
	if (there are enough partner tile in the hand to form the multi)
		store the partner indices in the storePartnersHere list
		can = true
	end if
	return can
	*/
	private boolean __canMultiType(Tile candidate, ArrayList<Integer> storePartnersHere, int numPartnersNeeded){
		boolean can = false;
		
		//count how many occurences of the tile, and store the indices of the occurences in tempPartnerIndices
		MahList<Integer> tempPartnerIndices = new MahList<Integer>(numPartnersNeeded);
		int count = __howManyOfThisTileInHand(candidate, tempPartnerIndices.getArrayList());
		
		//meld is possible if there are enough partners in the hand to form the meld
		if (count >= numPartnersNeeded){

			//store the partner indices in the pon partner index list
			__storePartnerIndices(storePartnersHere, tempPartnerIndices.subList(0, numPartnersNeeded));
			
			can = true;
		}
		return can;
	}
	private boolean __canPair(Tile candidate){
		return mCanPair = __canMultiType(candidate, mPartnerIndicesPair, Hand.NUM_TILES_NEEDED_TO_PAIR);
	}
	private boolean __canPon(Tile candidate){
		return mCanPon = __canMultiType(candidate, mPartnerIndicesPon, Hand.NUM_TILES_NEEDED_TO_PON);
	}
	private boolean __canKan(Tile candidate){
		return mCanKan = __canMultiType(candidate, mPartnerIndicesKan, Hand.NUM_TILES_NEEDED_TO_KAN);
	}
	//overloaded. if no tile argument given, candidate = mCallCandidate is passsed
	private boolean __canPair(){return __canPair(mCallCandidate);}
	private boolean __canPon(){return __canPon(mCallCandidate);}
	private boolean __canKan(){return __canKan(mCallCandidate);}
	
	
	
	/*
	private method: __howManyOfThisTileInHand
	returns how many copies of tile t are in the hand
	
	input: t is the tile to look for, storeIndicesHere will hold the indices of the occurences of the tile
	populates a list with the indices where t occurs, if one is provided
	
	count = 0
	for (each tile in the hand)
		if ((handtile and t have the same id) and (t and handtile are not the same physical tile))
			count++
			if (a non-null list was provided): add the current index to the list
		end if
	end for
	return count
	*/
	private int __howManyOfThisTileInHand(Tile t, ArrayList<Integer> storeIndicesHere){
		
		int count = 0;
		for (int i = 0; i < mHandTiles.size(); i++)
		{
			//if handtile and t have the same ID, but aren't the same phsyical tile
			//(basically, don't count t as its own partner)
			if (mHandTiles.get(i).equals(t) && mHandTiles.get(i) != t)	
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
		
		//~~~~reset flags
		__resetCallableFlags();
		mCallCandidate = candidate;
		
		//~~~~runs checks, set flags to the check results
		//only allow chis from the player's kamicha, or from the player's own tiles
		//don't check chi if candidate is an honor tile
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
		
		//if in tenpai, check ron
		if (mHand.getTenpaiStatus() == true)
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
		mPartnerIndicesChiL = new ArrayList<Integer>(Hand.NUM_TILES_NEEDED_TO_CHI);
		mPartnerIndicesChiM = new ArrayList<Integer>(Hand.NUM_TILES_NEEDED_TO_CHI);
		mPartnerIndicesChiH = new ArrayList<Integer>(Hand.NUM_TILES_NEEDED_TO_CHI);
		mPartnerIndicesPon = new ArrayList<Integer>(Hand.NUM_TILES_NEEDED_TO_PON);
		mPartnerIndicesKan = new ArrayList<Integer>(Hand.NUM_TILES_NEEDED_TO_KAN);
		mPartnerIndicesPair = new ArrayList<Integer>(Hand.NUM_TILES_NEEDED_TO_PAIR);
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
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
