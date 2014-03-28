import java.util.Scanner;


@SuppressWarnings("unused")
public class Majava {

	public static void main(String[] args) {
		
		System.out.println("Welcome to Majava!");
		
		
		boolean keepgoing = true;
		
		/*
		Tile t1 = new Tile();
		Tile t2 = new Tile(34, 'd', 'r');
		Tile t3 = new Tile(11, 'b', '2');
		
		System.out.println(t1.toString());
		System.out.println(t2.toString());
		System.out.println(t3.toString());
		
		Hand h1 = new Hand();
		h1.fill();
		System.out.println(h1.toString());
		*/

		if (keepgoing)
		{
			System.out.println("\n\n\n\n");
			
			Table table = new Table();
			table.play();
		}
		
	}

}
