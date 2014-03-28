import java.util.ArrayList;

import utility.GenSort;


public class Hand {
	
	
	public static final int MAX_HAND_SIZE = 14;
	

	public static final boolean DEFAULT_CLOSED_STATUS = true;
	
	
	
	
	private ArrayList<Tile> mTiles;
	private ArrayList<Meld> mMelds;
	
	private boolean mClosed;
	private int mNumMeldsMade; 
	
	private char mPlayerSeatWind;
	
	
	private ArrayList<Integer> mChiLPartnerIndices;
	private ArrayList<Integer> mChiMPartnerIndices;
	private ArrayList<Integer> mChiHPartnerIndices;
	private ArrayList<Integer> mPonPartnerIndices;
	private ArrayList<Integer> mKanPartnerIndices;
	
	
	
	
	public Hand(char playerWind){
		mTiles = new ArrayList<Tile>(14);
		mMelds = new ArrayList<Meld>(5);
		
		mClosed = DEFAULT_CLOSED_STATUS;
		mNumMeldsMade = 0;
		
		mPlayerSeatWind = playerWind;
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
	
	public boolean canChiType(Tile t, int chiType){
		
		boolean can = false;
		boolean impossible = false;

		ArrayList<Tile> tempMeldList = null;
		
		
		//can't chi on a wind or dragon
		if (t.getSuit() == Tile.SUIT_WIND || t.getSuit() == Tile.SUIT_DRAGON)
			return false;
		
		
		
		Tile partner1 = null, partner2 = null;
		int desired1 = 0, desired2 = 0;
		int tID = t.getId();
		
		//decide who the partners should be, based on chi type
		if (chiType == Meld.MELD_TYPE_CHI_L)
		{
			impossible = (t.getFace() == '8' || t.getFace() == '9');
			desired1 = tID + 1;
			desired2 = tID + 2;
		}
		if (chiType == Meld.MELD_TYPE_CHI_M)
		{
			impossible = (t.getFace() == '1' || t.getFace() == '9');
			desired1 = tID - 1;
			desired2 = tID + 1;
		}
		if (chiType == Meld.MELD_TYPE_CHI_H)
		{
			impossible = (t.getFace() == '1' || t.getFace() == '2');
			desired1 = tID - 2;
			desired2 = tID - 1;
		}
		
		//return false if a chi cannot be made with the tile 
		if (impossible)
			return false;
		
		
		//search the hand for the desired chi partners
		for (Tile handTile: mTiles)
		{
			if (handTile.getId() == desired1)
				partner1 = handTile;
			else if (handTile.getId() == desired2)
				partner2 = handTile;
		}
		
		
		if (partner1 != null && partner2 != null)
		{
			//add the tiles to a temp meld list
			tempMeldList = new ArrayList<Tile>(3);
			tempMeldList.add(t);
			tempMeldList.add(partner1);
			tempMeldList.add(partner2);
			//sort the meld list
			GenSort<Tile> sorter = new GenSort<Tile>(tempMeldList);
			sorter.sort();
			
			//return true, the player can chi
			can = true;
		}
		
		return can;
	}
	
	
	//done
	public boolean canChiL(Tile t){
		return canChiType(t, Meld.MELD_TYPE_CHI_L);
	}
	public boolean canChiM(Tile t){
		return canChiType(t, Meld.MELD_TYPE_CHI_M);
	}
	public boolean canChiH(Tile t){
		return canChiType(t, Meld.MELD_TYPE_CHI_H);
	}
	
	//done
	public boolean canPon(Tile t){
		boolean can = false;
		
		final int NUM_TILES_NEEDED_TO_PON = 2;
		int count = howManyOfThisTileInHand(t);
		if (count >= NUM_TILES_NEEDED_TO_PON)
			can = true;
		
		return can;
	}
	
	//done
	public boolean canKan(Tile t){
		boolean can = false;
		
		final int NUM_TILES_NEEDED_TO_KAN = 3;
		int count = howManyOfThisTileInHand(t);
		if (count >= NUM_TILES_NEEDED_TO_KAN)
			can = true;
		
		return can;
	}
	
	//returns the number of copies of this tiles that are already in the hand
	private int howManyOfThisTileInHand(Tile t){
		
		int count = 0;
		for (Tile handTile: mTiles)
			if (handTile.getId() == t.getId())
				count++;
		
		return count;
	}
	
	public boolean canRon(Tile t){
		return false;
	}
	
	//////////////////////////////////////////////////////////////////////////////
	//////END MELD CHEKCERS
	//////////////////////////////////////////////////////////////////////////////
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	public void showMelds(){
		for (Meld m: mMelds)
			System.out.println(m.toString());
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
