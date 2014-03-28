
public class MType {
	
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

}
