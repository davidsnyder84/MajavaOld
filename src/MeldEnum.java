import java.util.Scanner;


@SuppressWarnings("unused")
public class MeldEnum {
	
	
	
	public enum MeldType {
		NONE, CHI, CHI_L, CHI_M, CHI_H, PON, KAN, PAIR;
		
		
		
		@Override
		public String toString(){
			switch (this){
//			case CHI: case CHI_L: case CHI_M: case CHI_H: return "Chi";
			case NONE: return "None";
			case CHI: return "Chi";
			case CHI_L: return "Chi-L";
			case CHI_M: return "Chi-M";
			case CHI_H: return "Chi-H";
			case PON: return "Pon";
			case KAN: return "Kan";
			case PAIR: return "Pair";
			default: return "how...";
			}
		}
	};
	
	
	
	
	
	public static void main(String[] args){
		
		
		
		
		
		System.out.println("Well, enums.\n");
		
		
		
		
		
		
		MeldType call = MeldType.CHI_M;
		
		int want = 5;
		
		
//		Scanner keyboard = new Scanner(System.in);
//		System.out.println("Enter want: ");
//		want = keyboard.nextInt();
//		keyboard.close();
		
		
		MeldType wantCall = MeldType.values()[want];
		
		System.out.println("Call: " + call + ", " + call.ordinal());
		System.out.println("Want: " + wantCall);

		System.out.println("Call == Want?: " + (call == wantCall));
		System.out.println("Call > Want?: " + (call.compareTo(wantCall) > 0));
		
		
		
		
		
	}
	
	
	
	
	
}
