



/*
Class: RoundInfo

data:
	
	mGameType - length of game being played (single, tonpuusen, or hanchan)
	mRoundWind - the prevailing wind of the current round ('E' or 'S')
	mGameIsOver - will be true if the game is over, false if not
	mGameResult - the specific result of the game (reason for a draw game, or who won), is UNDECIDED if game is not over
	
methods:
	mutators:
	
 	accessors:
	
	other:
*/
public class RoundInfo {
	

	private char mRoundWind;
	private int mGameType;
	
	
	
	public RoundInfo(){
	}
	
	
	
	
	
	
	
	
	
	
	public int getGameType(){
		return mGameType;
	}
	
	public char getRoundWind(){
		return mRoundWind;
	}
	
	
	
	
	
	
}
