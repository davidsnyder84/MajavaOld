
public class Table {
	
	
	public static final char DEFAULT_ROUND_WIND = 'E';
	
	
	
	private Player p1, p2, p3, p4;
	
	private Wall mWall;
	
	private char mRoundWind;
	private int mTurnNumber;
	
	
	
	
	
	public Table(){
		p1 = new Player(Player.SEAT_EAST, Player.CONTROLLER_HUMAN);
		p2 = new Player(Player.SEAT_SOUTH, Player.CONTROLLER_COM);
		p3 = new Player(Player.SEAT_WEST, Player.CONTROLLER_COM);
		p4 = new Player(Player.SEAT_NORTH, Player.CONTROLLER_COM);
		
		mWall = new Wall();
		
		mRoundWind = DEFAULT_ROUND_WIND;
		mTurnNumber = 0;
	}
	
	
	
	
	
	public void play()
	{
		
		boolean gameIsOver = false;
		
		Tile recentDiscard = null;
		
		
		//DEBUG INFO
		System.out.println(mWall.toString() + "\n\n\n");
		
		
		//deal hands
		mWall.dealHands(p1, p2, p3, p4);
		

		//DEBUG INFO
		System.out.println(mWall.toString());
		p1.showHand();p2.showHand();p3.showHand();p4.showHand();
		System.out.println("\n\n\n");
		
		
		while (gameIsOver == false)
		{
			mTurnNumber++;
			System.out.println(mRoundWind + " Round, Turn number: " + mTurnNumber);
			
			//take turn, show pond
			recentDiscard = p1.takeTurn();
			p1.showPond();
			
			recentDiscard.toString();
			
			/*
			p2.takeTurn();
			
			
			p3.takeTurn();
			
			
			p4.takeTurn();
			*/
		}
	}
	
	
	
	
	
	
	
	
	
	public char getRoundWind(){
		return mRoundWind;
	}
	
	
	
	
	
	
	
	
	
	
	

}
