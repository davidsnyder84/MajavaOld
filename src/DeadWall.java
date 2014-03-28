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
	
	//these tiles won't ever be used
	public static final int POS_UNUSED_TILE_1 = 1;
	public static final int POS_UNUSED_TILE_2 = 0;
	
	
	
	
	
	private ArrayList<Tile> mTiles;
	
	
	

	//Constructor, takes a list of 14 tiles, makes it the dead wall
	public DeadWall(ArrayList<Tile> tiles){
		
		//assign the list of tiles to the dead wall
		if (tiles.size() == 14)
			mTiles = tiles;
		else
			System.out.println("\nError: dead wall must be exactly 14 tiles");
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
	
	
	
	
	
	
	public int numKansMade(){
		return MAX_SIZE_DEAD_WALL - mTiles.size();
	}
	
	
	
	
	public int getSize(){
		return mTiles.size();
	}
	
	
	
	
	
	
	
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
	
	
	
}
