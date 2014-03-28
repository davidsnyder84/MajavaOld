import java.util.ArrayList;

import utility.GenSort;


public class Hand {
	
	
	public static final int MAX_HAND_SIZE = 14;
	

	public static final boolean DEFAULT_CLOSED_STATUS = true;
	
	
	
	
	private ArrayList<Tile> mTiles;
	private ArrayList<Meld> mMelds;
	
	private boolean mClosed;
	
	private char mPlayerSeatWind;
	
	
	
	
	public Hand(char playerWind){
		mTiles = new ArrayList<Tile>(0);
		mMelds = new ArrayList<Meld>(0);
		
		mClosed = DEFAULT_CLOSED_STATUS;
		
		mPlayerSeatWind = playerWind;
	}
	public Hand(){
		this(Player.SEAT_UNDECIDED);
	}
	
	
	
	public void fill(){
		/*
		mTiles.add(new Tile('m', '1'));
		mTiles.add(new Tile('m', '1'));
		mTiles.add(new Tile('m', '1'));
		mTiles.add(new Tile('m', '2'));
		mTiles.add(new Tile('m', '3'));
		mTiles.add(new Tile('m', '4'));
		mTiles.add(new Tile('m', '5'));
		mTiles.add(new Tile('m', '6'));
		mTiles.add(new Tile('m', '7'));
		mTiles.add(new Tile('m', '8'));
		mTiles.add(new Tile('m', '9'));
		mTiles.add(new Tile('m', '9'));
		mTiles.add(new Tile('m', '9'));
		mTiles.add(new Tile('m', '7'));
		*/
		
		/*
		mTiles.add(new Tile(1, 'm', '1'));
		mTiles.add(new Tile(1, 'm', '1'));
		mTiles.add(new Tile(1, 'm', '1'));
		mTiles.add(new Tile(2, 'm', '2'));
		mTiles.add(new Tile(3, 'm', '3'));
		mTiles.add(new Tile(4, 'm', '4'));
		mTiles.add(new Tile(5, 'm', '5'));
		mTiles.add(new Tile(6, 'm', '6'));
		mTiles.add(new Tile(7, 'm', '7'));
		mTiles.add(new Tile(8, 'm', '8'));
		mTiles.add(new Tile(9, 'm', '9'));
		mTiles.add(new Tile(9, 'm', '9'));
		mTiles.add(new Tile(9, 'm', '9'));
		mTiles.add(new Tile(7, 'm', '7'));
		*/

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
