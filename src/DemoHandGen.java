
import java.util.Random;

import utility.MahList;



public class DemoHandGen {
	

	public static final char ONWER_SEAT = Player.SEAT_SOUTH;
	
	public static Random random;
	
	
	public static void main(String[] args) {

		random = new Random();
		runSimulation(34);
		
	}
	
	
	
	public static void runSimulation(int howManyTimes){
		
		
		Hand currentHand = null;
		boolean success = true;
		int numFailures = 0;
		int totalNum = 0;
		
		
		
		for (int i = 0; i < howManyTimes; i++){
			
			currentHand = generateCompleteHand();
			
			System.out.println(currentHand.toString() + "\n");
			
			success = currentHand.mChecker.isNormalComplete();
			System.out.println("Hand is complete normal?: " + success);
			
			if (success == false){
				numFailures++;
				System.out.println("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
			}
			
			System.out.println("\n\n");
			totalNum++;
		}
		
		

		System.out.println("Total number of trials: " + totalNum);
		System.out.println("Total number of failures: " + numFailures);
	}
	
	
	
	
	public static void runSimulationNoDisplay(int howManyTimes){
		
		Hand currentHand = null;
		int numFailures = 0;
		int totalNum = 0;
		
		
		
		for (int i = 0; i < howManyTimes; i++){
			if (!generateCompleteHand().mChecker.isNormalComplete()) numFailures++;
		}
		
		

		System.out.println("Total number of trials: " + howManyTimes);
		System.out.println("Total number of failures: " + numFailures);
	}
	
	
	
	
	
	
	
	
	
	public static Hand generateCompleteHand(){
		
		Hand hand = new Hand(ONWER_SEAT);
		
		
		TileList tiles = new TileList();
		Tile currentTile = null;
		
		
		int id = 0;
		
		int meldWhich = -1;
		MeldType chosenMeld;
		MahList<MeldType> validMelds = null;
		
		int howManyOfThisInHand = -1;
		
		TileList currentMeldTiles = null;
		
		
		int numSuccessfulMelds = 0;
		boolean tooMany = false;
		
		
		int numAlreadyCompletedMelds = 0;
		numAlreadyCompletedMelds = random.nextInt(5);
		
		//add 4 melds
		while (numSuccessfulMelds < numAlreadyCompletedMelds){
			
			//generate a random tile
			id = random.nextInt(34) + 1;
			currentTile = new Tile(id);
			currentTile.setOwner(ONWER_SEAT);
			
			howManyOfThisInHand = tiles.findHowManyOf(currentTile);
			if (howManyOfThisInHand < 4){
				
				validMelds = new MahList<MeldType>(5);
				if (!currentTile.isHonor()){
					if (currentTile.getFace() != '8' && currentTile.getFace() != '9') validMelds.add(MeldType.CHI_L);
					if (currentTile.getFace() != '1' && currentTile.getFace() != '9') validMelds.add(MeldType.CHI_M);
					if (currentTile.getFace() != '1' && currentTile.getFace() != '2') validMelds.add(MeldType.CHI_H);
				}
				if (howManyOfThisInHand <= 1) validMelds.add(MeldType.PON);
				
				meldWhich = random.nextInt(validMelds.size());
				chosenMeld = validMelds.get(meldWhich);
				
				
				//add the tiles to the meld list
				currentMeldTiles = new TileList(3);
				switch (chosenMeld){
				case CHI_L: currentMeldTiles.add(id); currentMeldTiles.add(id + 1); currentMeldTiles.add(id + 2); break;
				case CHI_M: currentMeldTiles.add(id - 1); currentMeldTiles.add(id); currentMeldTiles.add(id + 1); break;
				case CHI_H: currentMeldTiles.add(id - 2); currentMeldTiles.add(id - 1); currentMeldTiles.add(id); break;
				case PON: currentMeldTiles.add(id); currentMeldTiles.add(id); currentMeldTiles.add(id); break;
				default: break;
				}
				
				
				
				tooMany = false;
				for (Tile t: currentMeldTiles) if (tiles.findHowManyOf(t) + 1 > 4) tooMany = true;
				
				if (tooMany == false){
					//add the tiles to the hand
					for (Tile t: currentMeldTiles){
						t.setOwner(ONWER_SEAT);
						hand.addTile(t);
					}
					numSuccessfulMelds++;
				}
				
				
				
			}
		}
		
		//add a pair
		
		do{
			//generate a random tile
			id = random.nextInt(34) + 1;
			currentTile = new Tile(id);
			currentTile.setOwner(ONWER_SEAT);
			howManyOfThisInHand = tiles.findHowManyOf(currentTile);
		}
		while(howManyOfThisInHand > 2);

		currentMeldTiles = new TileList(2);
		currentMeldTiles.add(id); currentMeldTiles.add(id);
		
		//add the tiles to the hand
		for (Tile t: currentMeldTiles){
			t.setOwner(ONWER_SEAT);
			hand.addTile(t);
		}
		
		hand.sortHand();
		return hand;
		
	}
	
	
	
	
	

}
