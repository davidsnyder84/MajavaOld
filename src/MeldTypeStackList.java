import java.util.ArrayList;

import utility.MahStack;


//a list of stacks of meldtypes
public class MeldTypeStackList {
	
	private static final int SIZE_DEFAULT = 14;
	
	
	private ArrayList<MahStack<MeldType>> mStackList;
	
	
	
	//constructors, only take size
	public MeldTypeStackList(int size){mStackList = new ArrayList<MahStack<MeldType>>(size);}
	public MeldTypeStackList(){this(SIZE_DEFAULT);}
	
	
	
	//returns a new MeldTypeStackList with an independent COPY of each stack in the list
	public MeldTypeStackList makeCopy(){
		MeldTypeStackList copy = new MeldTypeStackList(3);
		
		for (MahStack<MeldType> s: mStackList) copy.add(new MahStack<MeldType>(s));
		return copy;
	}
	
	
	
	//get the first stack in the list
	public MahStack<MeldType> first(){return mStackList.get(0);}
	
	//stack functions for the first stack in the list
	public boolean firstPush(MeldType meldType){return mStackList.get(0).push(meldType);}
	public MeldType firstPop(){return mStackList.get(0).pop();}
	public MeldType firstTop(){return mStackList.get(0).top();}
	public boolean firstIsEmpty(){return mStackList.get(0).isEmpty();}
	
	
	
	//add
	public boolean add(MahStack<MeldType> s){return mStackList.add(s);}
	
	//remove
	public MahStack<MeldType> remove(int index){return mStackList.remove(index);}
	
	//remove multiple
	public void removeMultiple(ArrayList<Integer> removeIndices){
		
	}
	
	public int size(){return mStackList.size();}
	
}
