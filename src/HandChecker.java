import java.util.ArrayList;


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
	private boolean __canChiType(ArrayList<Integer> storePartnersHere, int offset1, int offset2){
		
		//if this is true, the player can make a chi
		boolean can = false;
		
		//decide who the chi partners should be (offset is decided based on chi type)
		//search the hand for the desired chi partners (get the indices)
		int partnerIndex1 = NOT_FOUND, partnerIndex2 = NOT_FOUND;
		partnerIndex1 = mHandTiles.indexOf(new Tile(mCallCandidate.getId() + offset1));
		partnerIndex2 = mHandTiles.indexOf(new Tile(mCallCandidate.getId() + offset2));
		
		
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
		ArrayList<Integer> tempPartnerIndices = new ArrayList<Integer>(Hand.NUM_TILES_NEEDED_TO_PON);
		
		//count how many occurences fo the tile, and store the indices of the occurences in tempPartnerIndices
		int count = __howManyOfThisTileInHand(mCallCandidate, tempPartnerIndices);
		
		//pon is possible if there are enough occurences of the tile in the hand to form a pon
		if (count >= Hand.NUM_TILES_NEEDED_TO_PON){
			
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
		ArrayList<Integer> tempPartnerIndices = new ArrayList<Integer>(Hand.NUM_TILES_NEEDED_TO_KAN);
		
		//count how many occurences fo the tile, and store the indices of the occurences in tempPartnerIndices
		int count = __howManyOfThisTileInHand(mCallCandidate, tempPartnerIndices);
		
		//kan is possible if there are enough occurences of the tile in the hand to form a kan
		if (count >= Hand.NUM_TILES_NEEDED_TO_KAN){
			
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
		ArrayList<Integer> tempPartnerIndices = new ArrayList<Integer>(Hand.NUM_TILES_NEEDED_TO_PAIR);
		
		//count how many occurences fo the tile, and store the indices of the occurences in tempPartnerIndices
		int count = __howManyOfThisTileInHand(mCallCandidate, tempPartnerIndices);
		
		//pair is possible if there are enough occurences of the tile in the hand to form a pair (need only one other copy of the tile in the hand)
		if (count >= Hand.NUM_TILES_NEEDED_TO_PAIR){
			
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
		
		//~~~~return true if a call (any call) can be made
		return (mCanChiL || mCanChiM || mCanChiH || mCanPon || mCanKan || mCanRon);
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
	
	//takes a list and several integer indices, stores the indices in the list
	private void __storePartnerIndices(ArrayList<Integer> storeHere, int... partnerIndices){
		for (int i: partnerIndices)
			storeHere.add(i);
	}
	
	
	
	
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
	
	//returns true if a specific call can be made on mCallCandidate
	public boolean ableToChiL(){return mCanChiL;}
	public boolean ableToChiM(){return mCanChiM;}
	public boolean ableToChiH(){return mCanChiH;}
	public boolean ableToPon(){return mCanPon;}
	public boolean ableToKan(){return mCanKan;}
	public boolean ableToRon(){return mCanRon;}
	public boolean ableToPair(){return mCanPair;}
	//public boolean ableToPair(){return (mTiles.contains(mCallCandidate));}
	
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
