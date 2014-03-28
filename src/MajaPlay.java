import java.util.ArrayList;


public class MajaPlay {
	
	
	public static void main(String[] args) {
		
		//testHots();
		
		//testContains();
		
		//testCalls();
		
		
		//testCallPartners();
		
		
		//testPlayerCall();
		
		//subListPlay();
		
		
		testMeldMaking();
		
		
		
		System.out.println();
	}

	public static void testMeldMaking(){
		
		Player p = new Player(Player.SEAT_EAST);

		p.addTileToHand(2);
		p.addTileToHand(3);
		p.addTileToHand(7);
		p.addTileToHand(7);
		p.addTileToHand(10);
		p.addTileToHand(11);
		p.addTileToHand(15);
		p.addTileToHand(17);
		p.addTileToHand(25);
		p.addTileToHand(25);
		p.addTileToHand(33);
		p.addTileToHand(34);
		p.addTileToHand(34);
		
		
		Tile q = null;
		q = new Tile(4);
		
		ArrayList<Tile> discards = new ArrayList<Tile>(10);
		discards.add(new Tile(12));
		discards.add(new Tile(34));
		discards.add(new Tile(7));
		discards.add(new Tile(25));
		discards.add(new Tile(1));
		discards.add(new Tile(16));
		
		discards.get(1).setOwner(p.getSeatWind());
		
		
		
		while (discards.isEmpty() == false)
		{
			q = discards.get(0);
			discards.remove(0);

			System.out.println();
			p.showHand();
			p.showMelds();
			System.out.println("\nDiscarded tile: " + q.toString() + "\n");
			
			
			p.reactToDiscard(q);
			p.makeMeld(q);
			
			
			String whatCalled = "nooooo";
			int status = p.checkCallStatus();
			if (status == Player.CALLED_NONE)
				whatCalled = "None";
			if (status == Player.CALLED_CHI_L)
				whatCalled = "Chi-L";
			else if (status == Player.CALLED_CHI_M)
				whatCalled = "Chi-M";
			else if (status == Player.CALLED_CHI_H)
				whatCalled = "Chi-H";
			else if (status == Player.CALLED_PON)
				whatCalled = "Pon";
			else if (status == Player.CALLED_KAN)
				whatCalled = "Kan";
			else if (status == Player.CALLED_RON)
				whatCalled = "Ron";
			System.out.println("Player's call status: " + whatCalled + " (status: " + status + ")");
			
			
			p.takeTurn();
			
		}

		System.out.println();
		p.showHand();
		p.showMelds();

		
	}
	

	
	
	
	public static void testPlayerCall(){
		
		Player p = new Player(Player.SEAT_EAST);

		p.addTileToHand(2);
		p.addTileToHand(2);
		p.addTileToHand(3);
		p.addTileToHand(4);
		p.addTileToHand(4);
		p.addTileToHand(4);
		p.addTileToHand(5);
		p.addTileToHand(6);
		
		
		Tile q = null;
		q = new Tile(4);
		
		p.showHand();
		System.out.println("\nDiscarded tile: " + q.toString() + "\n");
		
		p.reactToDiscard(q);
		
		String whatCalled = "nooooo";
		int status = p.checkCallStatus();

		if (status == Player.CALLED_NONE)
			whatCalled = "None";
		if (status == Player.CALLED_CHI_L)
			whatCalled = "Chi-L";
		else if (status == Player.CALLED_CHI_M)
			whatCalled = "Chi-M";
		else if (status == Player.CALLED_CHI_H)
			whatCalled = "Chi-H";
		else if (status == Player.CALLED_PON)
			whatCalled = "Pon";
		else if (status == Player.CALLED_KAN)
			whatCalled = "Kan";
		else if (status == Player.CALLED_RON)
			whatCalled = "Ron";
		
		System.out.println("Player's call status: " + whatCalled + " (status: " + status + ")");
		
	}
	
	
	
	
	
	public static void testCallPartners(){
		
		Tile q = null;
		Hand h = new Hand();
		

		h.addTile(2);
		h.addTile(2);
		h.addTile(3);
		h.addTile(4);
		h.addTile(4);
		h.addTile(4);
		h.addTile(5);
		h.addTile(6);
		
		
		q = new Tile(5);
		
		
		h.checkCallableTile(q);
		

		System.out.println(h.toString());
		System.out.println("\nDiscarded tile: " + q.toString());
		
		System.out.print("\nChi-L?: " + h.ableToChiL());
		if (h.ableToChiL())
			System.out.print(", Partners: " + h.partnerIndicesString(Meld.MELD_TYPE_CHI_L, true) + ", Ind: " + h.partnerIndicesString(Meld.MELD_TYPE_CHI_L));
		
		System.out.print("\nChi-M?: " + h.ableToChiM());
		if (h.ableToChiM())
			System.out.print(", Partners: " + h.partnerIndicesString(Meld.MELD_TYPE_CHI_M, true) + ", Ind: " + h.partnerIndicesString(Meld.MELD_TYPE_CHI_M));
		
		System.out.print("\nChi-H?: " + h.ableToChiH());
		if (h.ableToChiH())
			System.out.print(", Partners: " + h.partnerIndicesString(Meld.MELD_TYPE_CHI_H, true) + ", Ind: " + h.partnerIndicesString(Meld.MELD_TYPE_CHI_H));
		
		System.out.print("\nPon?  : " + h.ableToPon());
		if (h.ableToPon())
			System.out.print(", Partners: " + h.partnerIndicesString(Meld.MELD_TYPE_PON, true) + ", Ind: " + h.partnerIndicesString(Meld.MELD_TYPE_PON));
		
		System.out.print("\nKan?  : " + h.ableToKan());
		if (h.ableToKan())
			System.out.print(", Partners: " + h.partnerIndicesString(Meld.MELD_TYPE_KAN, true) + ", Ind: " + h.partnerIndicesString(Meld.MELD_TYPE_KAN));
		
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
		tiles.add(new Tile(2));
		tiles.add(new Tile(3));
		
		//System.out.println("\nt2 == M2?: ");
		//System.out.println(t2.equals(new Tile(2)));
		
		System.out.println(t2.toStringAllInfo());
		
		System.out.println("\nHand contains t22?: ");
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
	
	
	
	
	
	
	

	public static void subListPlay(){
		
		ArrayList<Integer> list = new ArrayList<Integer>(9);
		list.add(1);
		list.add(2);
		list.add(3);
		list.add(4);
		list.add(5);
		list.add(6);
		list.add(7);
		list.add(8);
		list.add(9);

//		ArrayList<Integer> sublist = (ArrayList<Integer>)list.subList(0, list.size());
		ArrayList<Integer> sublist = new ArrayList<Integer>(list.subList(1, 4));
		
		for (Integer i: sublist)
			System.out.println(i);
	}
	
	
	
	
	
}
