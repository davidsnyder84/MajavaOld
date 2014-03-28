import java.util.ArrayList;


public class Pond {
	
	
	public static final int SIZE_DEFAULT = 30;
	
	
	
	
	private ArrayList<Tile> mTiles;
	
	
	
	
	public Pond(){
		mTiles = new ArrayList<Tile>(SIZE_DEFAULT);
	}
	
	
	public void addTile(Tile t){
		mTiles.add(t);
	}
	
	
	
	
	//returns true if the player has made a nagashi mangan in their pond
	public boolean isNagashiMangan(){
		return true;
	}
	
	
	
	
	@Override
	public String toString()
	{
		
		int i, j;
		String pondString = "";
		
		final int TILES_PER_LINE = 6;
		for (i = 0; i < (mTiles.size() / TILES_PER_LINE) + 1; i++)
		{
			for (j = 0; j < TILES_PER_LINE && (j + TILES_PER_LINE*i < mTiles.size()); j++)
			{
				pondString += mTiles.get(TILES_PER_LINE*i + j).toString() + " ";
			}
			if (TILES_PER_LINE*i < mTiles.size())
				pondString += "\n";
		}
		
		return pondString;
	}
	
	
}
