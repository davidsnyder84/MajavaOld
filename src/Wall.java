import java.util.ArrayList;

import utility.GenSort;


public class Wall {
	

	public static final int MAX_SIZE_WALL = 136;
	public static final int FIRST_TILE_IN_WALL = 0;
	
	
	
	
	private ArrayList<Tile> mTiles;
	private DeadWall mDeadWall;
	
	
	
	
	public Wall(){
		
		mTiles = new ArrayList<Tile>(MAX_SIZE_WALL);
		
		//fill and shuffle the wall
		initialize();
		
		//split off the dead wall
		makeDeadWall();
	}
	
	
	
	
	/*
    East takes 4 tiles
    South takes 4 tiles
    West takes 4 tiles
    North takes 4 tiles
    
    East takes 4 tiles
    South takes 4 tiles
    West takes 4 tiles
    North takes 4 tiles
    
    East takes 4 tiles
    South takes 4 tiles
    West takes 4 tiles
    North takes 4 tiles
    
    East takes 2 tiles
    South takes 1
    West takes 1
    North takes 1
	*/
	public void dealHands(Player p1, Player p2, Player p3, Player p4)
	{
		ArrayList<Tile> tilesE = new ArrayList<Tile>(14);
		ArrayList<Tile> tilesS = new ArrayList<Tile>(13);
		ArrayList<Tile> tilesW = new ArrayList<Tile>(13);
		ArrayList<Tile> tilesN = new ArrayList<Tile>(13);
		
		int i, j;
		//each player takes 4, 3 times
		for (i = 0; i < 3; i++)
		{
			//east takes 4
			for (j = 0; j < 4; j++)
			{
				tilesE.add(mTiles.get(FIRST_TILE_IN_WALL));
				mTiles.remove(FIRST_TILE_IN_WALL);
			}
			//south takes 4
			for (j = 0; j < 4; j++)
			{
				tilesS.add(mTiles.get(FIRST_TILE_IN_WALL));
				mTiles.remove(FIRST_TILE_IN_WALL);
			}
			//west takes 4
			for (j = 0; j < 4; j++)
			{
				tilesW.add(mTiles.get(FIRST_TILE_IN_WALL));
				mTiles.remove(FIRST_TILE_IN_WALL);
			}
			//north takes 4
			for (j = 0; j < 4; j++)
			{
				tilesN.add(mTiles.get(FIRST_TILE_IN_WALL));
				mTiles.remove(FIRST_TILE_IN_WALL);
			}
		}
		
		
		//east takes 2
		for (j = 0; j < 2; j++)
		{
			tilesE.add(mTiles.get(FIRST_TILE_IN_WALL));
			mTiles.remove(FIRST_TILE_IN_WALL);
		}
		
		//south takes 1
		tilesS.add(mTiles.get(FIRST_TILE_IN_WALL));
		mTiles.remove(FIRST_TILE_IN_WALL);
		
		//west takes 1
		tilesW.add(mTiles.get(FIRST_TILE_IN_WALL));
		mTiles.remove(FIRST_TILE_IN_WALL);
		
		//north takes 1
		tilesN.add(mTiles.get(FIRST_TILE_IN_WALL));
		mTiles.remove(FIRST_TILE_IN_WALL);
		
		

		//add the tiles to the hands
		for(Tile t: tilesE)
			p1.addTileToHand(t);
		
		for(Tile t: tilesS)
			p2.addTileToHand(t);
		
		for(Tile t: tilesW)
			p3.addTileToHand(t);
		
		for(Tile t: tilesN)
			p4.addTileToHand(t);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	//removes the last 14 tiles from the wall, and creates the dead wall with them
	private void makeDeadWall()
	{
		ArrayList<Tile> deadWallTiles = new ArrayList<Tile>(DeadWall.MAX_SIZE_DEAD_WALL);
		int startingPos = MAX_SIZE_WALL - DeadWall.MAX_SIZE_DEAD_WALL;

		//remove the last 14 tiles in wall, create the dead wall with them
		int i;
		for (i = 0; i < DeadWall.MAX_SIZE_DEAD_WALL; i++)
		{
			//add a tile to the dead wall
			deadWallTiles.add(mTiles.get(startingPos));
			
			//remove that tile from the wall
			mTiles.remove(startingPos);
		}
		
		//initialize the dead wall with the tiles
		mDeadWall = new DeadWall(deadWallTiles);
	}
	
	
	//fills and shuffles the wall
	private void initialize()
	{
		//fill the wall with 4 of each tile
		int i;
		for (i = 1; i <= Tile.NUMBER_OF_DIFFERENT_TILES; i++)
		{
			mTiles.add(new Tile(i));
			mTiles.add(new Tile(i));
			mTiles.add(new Tile(i));
			mTiles.add(new Tile(i));
		}
		
		//shuffle the wall
		GenSort<Tile> sorter = new GenSort<Tile>(mTiles);
		sorter.shuffle();
	}
	
	
	
	
	public ArrayList<Tile> getDoraIndicators(boolean getUraDora){
		return mDeadWall.getDoraIndicators(getUraDora);
	}
	public ArrayList<Tile> getDoraIndicators(){
		return getDoraIndicators(false);
	}
	
	
	
	
	
	
	//removes a tile from the beginning of the wall and returns it
	public Tile takeTile(){
		
		//return null if the wall is empty
		if (isEmpty())
		{
			System.out.println("-----End of wall reached. Cannot draw tile.");
			return null;
		}
		
		//draw the first tile from the wall
		Tile drawnTile = mTiles.get(FIRST_TILE_IN_WALL);
		
		//remove that tile from the wall
		mTiles.remove(FIRST_TILE_IN_WALL);
		
		return drawnTile;
	}
	
	//removes a tile from the end of the dead wall and returns it
	public Tile takeTileFromDeadWall(){
		return mDeadWall.takeTile();
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	//returns true if the wall is empty (has no tiles left)
	public boolean isEmpty(){
		if (tilesLeftInWall() == 0) 
			return true;
		else
			return false;
	}
	
	//returns the number of tiles left in the wall (not including dead wall)
	public int tilesLeftInWall(){
		return mTiles.size();
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
		
		
		String dWallString = "DeadWall: " + mDeadWall.getSize() + "\n" + mDeadWall.toString();
		
		return ("Wall: " + getSize() + "\n" + wallString + "\n\n" + dWallString);
	}
	
	
	
}
