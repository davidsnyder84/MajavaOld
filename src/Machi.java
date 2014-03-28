import java.util.ArrayList;


public class Machi {
	
	
	

	public static final int WAIT_TYPE_CHI_L =  Meld.MELD_TYPE_CHI_M;
	public static final int WAIT_TYPE_CHI_M =  Meld.MELD_TYPE_CHI_H;
	public static final int WAIT_TYPE_CHI_H =  Meld.MELD_TYPE_PON;
	public static final int WAIT_TYPE_PON =  Meld.MELD_TYPE_KAN;
	public static final int WAIT_TYPE_KAN =  Meld.MELD_TYPE_PAIR;
	public static final int WAIT_TYPE_PAIR =  Meld.MELD_TYPE_CHI_L;
	public static final int WAIT_TYPE_UNKNOWN =  Meld.MELD_TYPE_UNKNOWN;
	public static final int WAIT_TYPE_DEFAULT = WAIT_TYPE_UNKNOWN;
	
	
	
	
	private int mWaitType;
	private Tile mCallCandidate;
	private ArrayList<Integer> mPartnerIndices;
	
	
	public Machi(ArrayList<Integer> partnerIndices, Tile candidate, int waitType){
		
		mWaitType = waitType;
		mCallCandidate = candidate;
		
		//make a copy of the partner index list
		mPartnerIndices = new ArrayList<Integer>(partnerIndices);
	}
	public Machi(int waitType){
		this(null, null, waitType);
	}
	public Machi(){
		this(WAIT_TYPE_DEFAULT);
	}
	
	
	
	
	
	
	
	//accessors
	public int getWaitType(){
		return mWaitType;
	}
	public Tile getCallCandidate(){
		return mCallCandidate;
	}
	public ArrayList<Integer> getPartnerIndices(){
		return mPartnerIndices;
	}
	
	
	
	
	
	
	
}
