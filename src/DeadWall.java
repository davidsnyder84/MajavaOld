import java.util.ArrayList;


public class DeadWall {
	
	
	public static final int MAX_SIZE_DEAD_WALL = 14;
	
	//odd numbers are top row, even are bottom row
	public static final int POS_KANDRAW_1 = 13;
	public static final int POS_KANDRAW_2 = 12;
	public static final int POS_KANDRAW_3 = 11;
	public static final int POS_KANDRAW_4 = 10;
	
	//dora indicators
	public static final int POS_DORA_1 = 9;
	public static final int POS_URADORA_1 = 8;
	
	public static final int POS_DORA_2 = 7;
	public static final int POS_URADORA_2 = 6;
	
	public static final int POS_DORA_3 = 5;
	public static final int POS_URADORA_3 = 4;
	
	public static final int POS_DORA_4 = 3;
	public static final int POS_URADORA_4 = 2;
	
	//these are only used if a player makes 4 kans (making 5 dora indicators)
	public static final int POS_DORA_5 = 1;
	public static final int POS_URADORA_5 = 0;
	
	
	
	
	
	private ArrayList<Tile> mTiles;
	
	private int mNumKansMade;
	
	
	
	

	//Constructor, takes a list of 14 tiles, makes it the dead wall
	public DeadWall(ArrayList<Tile> tiles){
		
		//assign the list of tiles to the dead wall
		if (tiles.size() == 14)
			mTiles = tiles;
		else
			System.out.println("\nError: dead wall must be exactly 14 tiles");
		
		mNumKansMade = 0;
	}
	/*
	//Constructor, takes a list of 14 tiles, adds them to the dead wall
	public DeadWall(ArrayList<Tile> tiles){
		
		mTiles = new ArrayList<Tile>(14);
		
		//add the tiles to the dead wall
		if (tiles.size() == 14)
			for (Tile t: tiles)
				mTiles.add(t);
		else
			System.out.println("\nError: dead wall must be exactly 14 tiles");
		
	}
	*/
	
	
	
	
	public Tile takeTile(){
		
		//the tile will be drawn from the very end of the dead wall
		int drawPosition = mTiles.size() - 1;
		
		//draw the tile
		Tile drawnTile = mTiles.get(drawPosition);
		
		//remove that tile from the end of the dead wall
		mTiles.remove(drawPosition);
		
		return drawnTile;
	}
	
	
	
	
	
	
	
	
	
	public int numKansMade(){
		//return MAX_SIZE_DEAD_WALL - mTiles.size();
		return mNumKansMade;
	}
	
	
	
	
	public int getSize(){
		return mTiles.size();
	}
	
	
	
	
	/*
	 method: getDoraIndicators
	 returns the dora indicators, as a list of Tiles
	 
	 input: if getUraDora is true, the list will also contain the ura dora indicators
	 
	 
	 decide the exact size of the list
	 
	 add first dora indicator
	 if kans have been made,  add more indicators to the list
	 if (getUraDora)
	 	add first ura dora indicator
	 	if kans have been made,  add more ura indicators to the list
	 
	 return the list
	*/
	public ArrayList<Tile> getDoraIndicators(boolean getUraDora){
		
		int size = mNumKansMade + 1;
		if (getUraDora)
			size *= 2;
		ArrayList<Tile> indicators = new ArrayList<Tile>(size);
		
		//add the first dora indicator
		indicators.add(mTiles.get(POS_DORA_1));
		
		//add other indicators if kans have been made
		if (mNumKansMade >= 1)
			indicators.add(mTiles.get(POS_DORA_2));
		if (mNumKansMade >= 2)
			indicators.add(mTiles.get(POS_DORA_3));
		if (mNumKansMade >= 3)
			indicators.add(mTiles.get(POS_DORA_4));
		if (mNumKansMade == 4)
			indicators.add(mTiles.get(POS_DORA_5));
		
		
		//add ura dora indicators, if specified
		if (getUraDora)
		{
			//add the first ura dora indicator
			indicators.add(mTiles.get(POS_URADORA_1));

			//add other ura indicators if kans have been made
			if (mNumKansMade >= 1)
				indicators.add(mTiles.get(POS_URADORA_2));
			if (mNumKansMade >= 2)
				indicators.add(mTiles.get(POS_URADORA_3));
			if (mNumKansMade >= 3)
				indicators.add(mTiles.get(POS_URADORA_4));
			if (mNumKansMade == 4)
				indicators.add(mTiles.get(POS_URADORA_5));
		}
		
		return indicators;
	}
	//Overloaded with no args, gets just top dora (no ura)
	public ArrayList<Tile> getDoraIndicators(){
		return getDoraIndicators(false);
	}
	
	
	
	
	
	
	
	public String toString()
	{
		
		int i, j;
		String wallString = "";
		
		final int TILES_PER_LINE = 2;
		for (i = 0; i < (mTiles.size() / TILES_PER_LINE) + 1; i++)
		{
			for (j = 0; j < TILES_PER_LINE && (j + TILES_PER_LINE*i < mTiles.size()); j++)
			{
				wallString += mTiles.get(TILES_PER_LINE*i + j).toString() + " ";
			}
			if (TILES_PER_LINE*i < mTiles.size())
				wallString += "\n";
		}
		
		return wallString;
	}
	/*
	public String toString()
	{
		
		int i, j;
		String wallString = "";
		
		final int TILES_PER_LINE = 12;
		for (i = 0; i < (mTiles.size() / TILES_PER_LINE) + 1; i++)
		{
			for (j = 0; j < TILES_PER_LINE && (j + TILES_PER_LINE*i < mTiles.size()); j++)
			{
				wallString += mTiles.get(TILES_PER_LINE*i + j).toString() + " ";
			}
			if (TILES_PER_LINE*i < mTiles.size())
				wallString += "\n";
		}
		
		return wallString;
	}
	 */
	
	
	
}
