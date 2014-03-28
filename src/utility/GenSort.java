package utility;

import java.util.ArrayList;
import java.util.Random;


public class GenSort <T extends Comparable<T> > {
	
	public static int INVALID_LIST_LENGTH = -1;
	
	
	private ArrayList<T> mListToSort;
	private int mListLength = INVALID_LIST_LENGTH;
	
	//1-arg constructor
	//takes an arraylist
	public GenSort(ArrayList<T> list){
		
		if (list != null)
		{
			mListToSort = list;
			mListLength = list.size();
		}
		else
		{
			mListToSort = null;
			mListLength = INVALID_LIST_LENGTH;
			System.out.println("-----Error: unsuitable list received by GenSort");
		}
	}
	
	
	
	
	//sorts mListToSort with a shitty selection sort
	public void sort(){
			
		int current, walker, smallest;
		T temp;
		
		for (current = 0; current < mListLength; current++)
		{
			smallest = current;
			for (walker = current + 1; walker < mListLength; walker++)
				if (mListToSort.get(walker).compareTo(mListToSort.get(smallest)) < 0)
					smallest = walker;
			
			//swap the current element and the smallest element
			temp = mListToSort.get(smallest);
			mListToSort.set(smallest, mListToSort.get(current));
			mListToSort.set(current, temp);
		}
		
	}
	
	
	
	
	//shuffles the elements of mListToSort in a random order
	public void shuffle(){
		
		/*
		int[] nums = new int[mListLength];
		
		int i;
		for (i = 0; i < mListLength; i++)
				;
		*/
				
		Random random = new Random();
		int swapIndex = -1;
		T temp = null;
		
		int curIndex;
		for (curIndex = 0; curIndex < mListLength; curIndex++)
		{
			//decide the swap index randomly
			swapIndex = random.nextInt(mListLength);
			
			//swap the items at the current index and the swap index
			temp = mListToSort.get(curIndex);
			mListToSort.set(curIndex, mListToSort.get(swapIndex));
			mListToSort.set(swapIndex, temp);
		}
		
	}
	
	
	
	
}
