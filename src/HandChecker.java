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
