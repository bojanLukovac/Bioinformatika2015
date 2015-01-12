package bioinformatika;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Set;
import java.util.TreeMap;

public class SAIS {

	public static void main(String[] args) {
		
		String input = "ATTAGCGAGCG$";
		
		String[] S = input.split("(?!^)");
		

		System.out.println(Arrays.toString(S));

		boolean[] t = getT(S);
		System.out.println(Arrays.toString(t));
			
		System.out.println(getLMSPointersArray(S, t));
		
		
		/*
		 * Create SA buckets and insert -1 for each value
		 * */
		TreeMap<String, ArrayList<Integer>> bucketHash = new TreeMap<>();
 		for (int i = 0; i < S.length; i++) {
 			if (bucketHash.containsKey(S[i])) {
 				bucketHash.get(S[i]).add(-1);
 			} else {
 				ArrayList<Integer> values = new ArrayList<>();
 				values.add(-1);
 				bucketHash.put(S[i], values);
 			}
		}
 		
 		Object[] keySet = bucketHash.keySet().toArray();
 		System.out.println(Arrays.toString(keySet));
 		
 		

	}
	
	/*	Returns array of booleans that tells us the types of characters
	 * 	L = true
	 * 	S = false
	 * */
	public static boolean[] getT(String[] S) {
		
		boolean[] t = new boolean[S.length];
		t[S.length - 1] = false;
		t[S.length - 2] = true;
		
		for (int i = 0; i < S.length - 2; i++) {
			int comparation = S[i].compareTo(S[i + 1]); 

			if (comparation > 0) {
				t[i] = true;
			} else if (comparation < 0) {
				t[i] = false;
			} else {
				int j = i + 1;
				
				while (S[j].compareTo(S[j + 1]) == 0) {
					j++;
				}

				if (S[j].compareTo(S[j + 1]) > 0) {
					setArray(t, i, j, true);
				} else {
					setArray(t, i, j, true);
				}
				i = j;
			}
		}
		return t;
	}

	/*
	 * 	Set array from "i" to "j" to booleanValue
	 * 
	 * */
	private static void setArray(boolean[] t, int i, int j, boolean booleanValue) {
		for (; i <= j; i++) {
			t[i] = booleanValue;
		}
	}

	/*
	 * Returns array holding pointers to LMS-substrings
	 * String[] S is the input
	 * boolean[] t keeps track of S-types and L-types
	 * S type = false
	 * L type = true
	 * */
	public static ArrayList<Integer> getLMSPointersArray(String[] S, boolean[] t) {
		ArrayList<Integer> lmsPointers = new ArrayList<>();
		for (int i = 1; i < S.length; i++) {
			if (t[i - 1] == true && t[i] == false) {
				lmsPointers.add(i);
			}
		}
		return lmsPointers;
	}
	
	
	/*
	 * Class that's used for "bucket" part of the algorithm
	 * */
	public static class Bucket {
		
		
		
	}
 	
}
