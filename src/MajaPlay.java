import java.util.ArrayList;


public class MajaPlay {
	
	
	public static void main(String[] args) {
		
		//testHots();
		
		//testContains();
		
		testCalls();

	}
	
	
	public static void testCalls(){
		
		Tile q = null;
		Hand h = new Hand();
		
		
		h.addTile(new Tile(12));
		h.addTile(new Tile(10));
		
		
		q = new Tile(11);
		

		System.out.println(h.toString());
		System.out.println("\nDiscarded tile: " + q.toString() + "\n");
		

		System.out.println("Chi-L?: " + h.canChiL(q));
		System.out.println("Chi-M?: " + h.canChiM(q));
		System.out.println("Chi-H?: " + h.canChiH(q));
		System.out.println("Pon?  : " + h.canPon(q));
		System.out.println("Kan?  : " + h.canKan(q));
		
	}
	
	
	
	
	
	
	
	
	
	public static void testContains(){
		

		ArrayList<Tile> tiles = new ArrayList<Tile>(0);
		
		Tile t2 = new Tile(2);
		
		tiles.add(new Tile(1));
		tiles.add(t2);
		tiles.add(new Tile(3));
		
		
		
		System.out.println("\nHand contains M2?: ");
		System.out.println(tiles.contains(t2));
		
		
	}
	
	
	public static void testHots(){
		Hand h = new Hand();

		h.addTile(new Tile(1));
		h.addTile(new Tile(2));
		
		
		ArrayList<Integer> hots = new ArrayList<Integer>(0);
		hots = h.findAllHotTiles();
		
		System.out.println(h.toString());
		
		System.out.println("\nhots:");
		
		for (Integer i: hots)
		{
			System.out.println((new Tile(i)).toString());
		}
	}
}
