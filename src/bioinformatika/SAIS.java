package bioinformatika;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

public class SAIS {

	public static void main(String[] args) {
		
		String firstSolution = doSAIS("ATTAGCGAGCG$");
		String secondSolution = doSAIS(firstSolution);
		
		System.out.println("FIRST: " + firstSolution);
		System.out.println("SECOND: " + secondSolution);
		
	}
	
	
	public static String doSAIS(String inputString) {

		String input = inputString;
		
		String[] S = input.split("(?!^)");
		

		System.out.println(Arrays.toString(S));

		boolean[] t = getT(S);
		System.out.println(Arrays.toString(t));
		
		ArrayList<Integer> lmsPointersArray = getLMSPointersArray(S, t);
			
		System.out.println(lmsPointersArray);
		
		
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
 		
 		/*
 		 *	HashMap that stores pointers on bucket arrays
 		 * 	For example, this is how it's done in powerpoint presentations: 
 		 * 	B['$'] = 0; B['A'] = 3; B['C'] = 5; B['G'] = 9; B['T'] = 11;
 		 *	We do the similar thing here :)
 		 * */
 		HashMap<String, Integer> bucketPointers = new HashMap<>();
 		setAllPointersInBucketsToEnd(keySet, bucketPointers, bucketHash);
 		
 		/*
 		 * Algorithm 1, first iteration
 		 * */
 		for (Integer lmsPointer : lmsPointersArray) {
			String lms = S[lmsPointer];
			bucketHash.get(lms).set(bucketPointers.get(lms), lmsPointer);
			bucketPointers.put(lms, bucketPointers.get(lms) - 1);
		}
 		
 		/*
 		 * Values after first iteration
 		 * */
 		System.out.println("____First iteration set LMS indexes__________");
 		for (Entry<String, ArrayList<Integer>> entry : bucketHash.entrySet()) {
 	        System.out.println("key ->" + entry.getKey() + ", value->" + entry.getValue());   
 	    }
 		
 		/*
 		 * Algorithm 2, second iteration
 		 * */
 		//Set all pointers in buckets to beginning
 		setAllPointersInBucketsToBeginning(keySet, bucketPointers);
 		
 		// Iterate over all buckets
 		for (Entry<String, ArrayList<Integer>> entry : bucketHash.entrySet()) {
 	        for (Integer integerInBucket : entry.getValue()) {
 	        	if (integerInBucket > 0 && t[integerInBucket - 1] == true) {
 	        		bucketHash.get(S[integerInBucket - 1]).set(bucketPointers.get(S[integerInBucket - 1]), integerInBucket - 1);
 	        		bucketPointers.put(S[integerInBucket - 1], bucketPointers.get(S[integerInBucket - 1]) + 1);		
 	        	}
 	        }
 	    }

 		System.out.println("____Second iteration______");
 		for (Entry<String, ArrayList<Integer>> entry : bucketHash.entrySet()) {
 	        System.out.println("key ->" + entry.getKey() + ", value->" + entry.getValue());   
 	    }
 		
 		/*
 		 * Algorithm 3, third iteration
 		 * */
 		setAllPointersInBucketsToEnd(keySet, bucketPointers, bucketHash);
 		
 		// Iterate from the end to the beginning
 		for (int i = keySet.length - 1; i >= 0; i--) { 	        
 	        for (int j = bucketHash.get(keySet[i]).size() - 1; j >= 0; j--) {
 	        	int integerInBucket = bucketHash.get(keySet[i]).get(j);
				if (integerInBucket > 0 && t[integerInBucket - 1] == false) {
					bucketHash.get(S[integerInBucket - 1]).set(bucketPointers.get(S[integerInBucket - 1]), integerInBucket - 1);
					bucketPointers.put(S[integerInBucket - 1], bucketPointers.get(S[integerInBucket - 1]) - 1);
				}
			}
 	    }

 		System.out.println("_____Third iteration__________");
 		for (Entry<String, ArrayList<Integer>> entry : bucketHash.entrySet()) {
 	        System.out.println("key ->" + entry.getKey() + ", value->" + entry.getValue());   
 	    }
 		
 		
 		// TEST
 		// boolean result = compareLMSSubstrings(3, 7, S, t);
 		 //System.out.println(result);
 		
 		// STEP 4,  Give new names to each LMS substring
 		int[] S1 = new int[lmsPointersArray.size()];
 		ArrayList<Integer> SA = new ArrayList<>();
 	
		for (Entry<String, ArrayList<Integer>> entry : bucketHash.entrySet()) {
			for (Integer integerInBucket : entry.getValue()) {
				SA.add(integerInBucket);
			}
		}
	
		int newName = 0;
		
		int lastKnownIndexOfLMSSubstringInSA = 0;
		for (int i = 0; i < SA.size(); i++) {
			if (lmsPointersArray.contains(SA.get(i))) {
				lastKnownIndexOfLMSSubstringInSA = i;
				System.out.println(lastKnownIndexOfLMSSubstringInSA);
				break;
			}
		}
		S1[lastKnownIndexOfLMSSubstringInSA] = newName;
		
		for (int i = lastKnownIndexOfLMSSubstringInSA + 1; i < SA.size(); i++) {
			if (lmsPointersArray.contains(SA.get(i)) == true) {
				
				if(compareLMSSubstrings(SA.get(i), SA.get(lastKnownIndexOfLMSSubstringInSA), S, t) == true) {
					S1[lmsPointersArray.indexOf(SA.get(i))] = newName;
				} else {
					newName++;
					S1[lmsPointersArray.indexOf(SA.get(i))] = newName;
				}
				lastKnownIndexOfLMSSubstringInSA = i;
			} 
		}

		System.out.println(Arrays.toString(S1));
		
		return joinArray(S1);
	
	}
	
	public static String joinArray(int[] aArr) {
	    StringBuilder sbStr = new StringBuilder();
	    for (int i = 0, il = aArr.length; i < il; i++) {
	        sbStr.append(aArr[i]);
	    }
	    return sbStr.toString();
	}
	
	public static boolean compareLMSSubstrings(int firstLMS, int secondLMS, String[] S, boolean[] t) {
		boolean isFirstIteration = true;
		while (S[firstLMS].equals(S[secondLMS])) {

			// Do only once
			if (isFirstIteration == true) {
				if (t[firstLMS] == false && t[secondLMS] == false) {
					firstLMS++;
					secondLMS++;
					// Check if we're comparing last lms strings
					if (firstLMS == secondLMS && firstLMS >= S.length) {
						return true;
					}
					isFirstIteration = false;
					continue;
				}
			}
			
			
			if (t[firstLMS] == false && t[secondLMS] == false) {
				return true;
			} else if (t[firstLMS] == true && t[secondLMS] == true) {
				firstLMS++;
				secondLMS++;
			} else {
				return false;
			}
		
		}
		
		return false;
		
	}
	
	public static void setAllPointersInBucketsToBeginning(Object[] keySet, HashMap<String, Integer> bucketPointers) {
		for (int i = 0; i < keySet.length; i++) {
			bucketPointers.put((String) keySet[i], 0);
		}
	}
	
	public static void setAllPointersInBucketsToEnd(Object[] keySet, HashMap<String, Integer> bucketPointers, TreeMap<String, ArrayList<Integer>> bucketHash) {
		for (int i = 0; i < keySet.length; i++) {
			bucketPointers.put((String) keySet[i], bucketHash.get((String) keySet[i]).size() - 1);
		}
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
