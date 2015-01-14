package bioinformatika;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;
import java.util.Map.Entry;

public class SaisStep {

	private String[] S;
	private boolean[] t;
	private ArrayList<Integer> lmsPointersArray;
	private TreeMap<String, ArrayList<Integer>> bucketHash;
	private Object[] keySet;
	private HashMap<String, Integer> bucketPointers;
	private String[] arrayWithNewNames;
	private ArrayList<Integer> SA;
	
	public ArrayList<Integer> getSA() {
		return SA;
	}

	public void setSA(ArrayList<Integer> sA) {
		SA = sA;
	}

	public SaisStep(String[] s, boolean[] t,
			ArrayList<Integer> lmsPointersArray,
			TreeMap<String, ArrayList<Integer>> bucketHash, Object[] keySet,
			HashMap<String, Integer> bucketPointers, String[] arrayWithNewNames) {
		super();
		this.S = s;
		this.t = t;
		this.lmsPointersArray = lmsPointersArray;
		this.bucketHash = bucketHash;
		this.keySet = keySet;
		this.bucketPointers = bucketPointers;
		this.arrayWithNewNames = arrayWithNewNames;
	}

	public SaisStep() {
		// TODO Auto-generated constructor stub
	}

	public String[] getArrayWithNewNames() {
		return arrayWithNewNames;
	}

	public void setArrayWithNewNames(String[] arrayWithNewNames) {
		this.arrayWithNewNames = arrayWithNewNames;
	}

	public String[] getS() {
		return S;
	}

	public void setS(String[] s) {
		S = s;
	}

	public boolean[] getT() {
		return t;
	}

	public void setT(boolean[] t) {
		this.t = t;
	}

	public ArrayList<Integer> getLmsPointersArray() {
		return lmsPointersArray;
	}

	public void setLmsPointersArray(ArrayList<Integer> lmsPointersArray) {
		this.lmsPointersArray = lmsPointersArray;
	}

	public TreeMap<String, ArrayList<Integer>> getBucketHash() {
		return bucketHash;
	}

	public void setBucketHash(TreeMap<String, ArrayList<Integer>> bucketHash) {
		this.bucketHash = bucketHash;
	}

	public Object[] getKeySet() {
		return keySet;
	}

	public void setKeySet(Object[] keySet) {
		this.keySet = keySet;
	}

	public HashMap<String, Integer> getBucketPointers() {
		return bucketPointers;
	}

	public void setBucketPointers(HashMap<String, Integer> bucketPointers) {
		this.bucketPointers = bucketPointers;
	}


	public String[] getArrayWithNewNamesForEachLMSSubstring(ArrayList<Integer> lmsPointersArray,
			TreeMap<String, ArrayList<Integer>> bucketHash, String[] S, boolean[] t) {
	
		printArray(S, "Entered getArrayWithNewNames...");
		
		String[] S1 = new String[lmsPointersArray.size()];
 		ArrayList<Integer> SA = new ArrayList<>();
 	
		for (Entry<String, ArrayList<Integer>> entry : bucketHash.entrySet()) {
			for (Integer integerInBucket : entry.getValue()) {
				SA.add(integerInBucket);
			}
		}
	
		int newName = 0;
		System.out.println("lmsPointers: " + lmsPointersArray);
		
		int lastKnownIndexOfLMSSubstringInSA = 0;
		for (int i = 0; i < SA.size(); i++) {
			if (lmsPointersArray.contains(SA.get(i))) {
				lastKnownIndexOfLMSSubstringInSA = i;
				break;
			}
		}
		S1[lmsPointersArray.indexOf(SA.get(lastKnownIndexOfLMSSubstringInSA))] = Integer.toString(newName);
		System.out.println("na mjesto " + lastKnownIndexOfLMSSubstringInSA + " stavljamo " + newName);
		
		for (int i = lastKnownIndexOfLMSSubstringInSA + 1; i < SA.size(); i++) {
			if (lmsPointersArray.contains(SA.get(i)) == true) {
				
				if(compareLMSSubstrings(SA.get(i), SA.get(lastKnownIndexOfLMSSubstringInSA), S, t) == true) {
					S1[lmsPointersArray.indexOf(SA.get(i))] = Integer.toString(newName);
				} else {
					newName++;
					S1[lmsPointersArray.indexOf(SA.get(i))] = Integer.toString(newName);
				}
				lastKnownIndexOfLMSSubstringInSA = i;
			} 
		}
		System.out.println("AHHHHHHHHHHHAAAAAAAAAAAAA");
		System.out.println("ovo je SA: " + SA);
		this.printArray(S1, "UNUTAR STVARANJA ARRAYA");
		
		this.SA = SA;
		return S1;
	
	}


	public void doThirdIteration(String[] S, boolean[] t,
			TreeMap<String, ArrayList<Integer>> bucketHash,
			HashMap<String, Integer> bucketPointers, Object[] keySet) {
		
		// Iterate from the end to the beginning
		for (int i = keySet.length - 1; i >= 0; i--) {
			for (int j = bucketHash.get(keySet[i]).size() - 1; j >= 0; j--) {
				int integerInBucket = bucketHash.get(keySet[i]).get(j);
				if (integerInBucket > 0 && t[integerInBucket - 1] == false) {
					bucketHash.get(S[integerInBucket - 1]).set(bucketPointers.get(S[integerInBucket - 1]), integerInBucket - 1);
					bucketPointers.put(S[integerInBucket - 1],
							bucketPointers.get(S[integerInBucket - 1]) - 1);
				}
			}
		}
		
	}


	public void doSecondIteration(String[] S, boolean[] t, TreeMap<String, ArrayList<Integer>> bucketHash, HashMap<String, Integer> bucketPointers) {
		// Iterate over all buckets
		for (Entry<String, ArrayList<Integer>> entry : bucketHash.entrySet()) {
			for (Integer integerInBucket : entry.getValue()) {
				if (integerInBucket > 0 && t[integerInBucket - 1] == true) {
					bucketHash.get(S[integerInBucket - 1]).set(
							bucketPointers.get(S[integerInBucket - 1]),
							integerInBucket - 1);
					bucketPointers.put(S[integerInBucket - 1],
							bucketPointers.get(S[integerInBucket - 1]) + 1);
				}
			}
		}

	}


	/*
	 * Create SA buckets and insert -1 for each value
	 * */
	public TreeMap<String, ArrayList<Integer>> getBucketHashFromSAndInitializeToMinusOne(String[] S) {

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
		return bucketHash;
	}
	
	public void setSAToMinusOne(TreeMap<String, ArrayList<Integer>> bucketHash) {
		
 		for (Entry<String, ArrayList<Integer>> entry : bucketHash.entrySet()) {
 	        for (int i = 0; i < entry.getValue().size(); i++) {
 	        	entry.getValue().set(i, -1);
 	        }   
 	    }
		
	}

	public String joinArray(String[] aArr) {
	    StringBuilder sbStr = new StringBuilder();
	    for (int i = 0, il = aArr.length; i < il; i++) {
	        sbStr.append(aArr[i]);
	    }
	    return sbStr.toString();
	}

	public String joinArray(int[] aArr) {
	    StringBuilder sbStr = new StringBuilder();
	    for (int i = 0, il = aArr.length; i < il; i++) {
	        sbStr.append(aArr[i]);
	    }
	    return sbStr.toString();
	}
	
	public boolean compareLMSSubstrings(int firstLMS, int secondLMS, String[] S, boolean[] t) {
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
	
	public void setAllPointersInBucketsToBeginning(Object[] keySet, HashMap<String, Integer> bucketPointers) {
		for (int i = 0; i < keySet.length; i++) {
			bucketPointers.put((String) keySet[i], 0);
		}
	}
	
	public void setAllPointersInBucketsToEnd(Object[] keySet, HashMap<String, Integer> bucketPointers, TreeMap<String, ArrayList<Integer>> bucketHash) {
		for (int i = 0; i < keySet.length; i++) {
			bucketPointers.put((String) keySet[i], bucketHash.get((String) keySet[i]).size() - 1);
		}
	}
	
	/*	Returns array of booleans that tells us the types of characters
	 * 	L = true
	 * 	S = false
	 * */
	public boolean[] getTFromS(String[] S) {
		
		boolean[] t = new boolean[S.length];
		t[S.length - 1] = false;
		t[S.length - 2] = true;
		
		for (int i = S.length - 3; i > 0; i--) {
			int comparation = S[i].compareTo(S[i + 1]);
			
			if (comparation < 0) {
				t[i] = false;
			} else if (comparation > 0) {
				t[i] = true;
			} else {
				t[i] = t[i + 1];
			}
		}
		
		return t;
	}

	/*
	 * 	Set array from "i" to "j" to booleanValue
	 * 
	 * */
	private void setArray(boolean[] t, int i, int j, boolean booleanValue) {
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
	public ArrayList<Integer> getLMSPointersArray(String[] S, boolean[] t) {
		ArrayList<Integer> lmsPointers = new ArrayList<>();
		for (int i = 1; i < S.length; i++) {
			if (t[i - 1] == true && t[i] == false) {
				lmsPointers.add(i);
			}
		}
		return lmsPointers;
	}

	public static void printArray(String[] arr, String message) {
		System.out.println(message);
		for (int i = 0; i < arr.length; i++) {
			System.out.print(arr[i]);
		}
		System.out.println("");
	}
	
}
