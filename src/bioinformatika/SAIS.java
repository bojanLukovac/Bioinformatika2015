package bioinformatika;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.TreeMap;

public class SAIS {

	public static void main(String[] args) {
		
		ArrayList<SaisStep> saisSteps = new ArrayList<>();
		String input = "ATTAGCGAGCG$";
		
		// Do SA-IS steps until we reach array with all different names
		while (true) {
			SaisStep solution = doSAIS(input);
			saisSteps.add(solution);
			
			if (areAllElementsDifferent(solution.getArrayWithNewNames()) == false) {
				input = solution.joinArray(solution.getArrayWithNewNames());
			} else {
				System.out.println("All are different!");
				break;
			}
		}
		
		
		/*
		System.out.println("PRINTING IT");
		for (SaisStep step : saisSteps) {
			System.out.println(step.joinArray(step.getArrayWithNewNames()));
		}
		*/
		
		/*
		SaisStep firstSolution = doSAIS("ATTAGCGAGCG$");
		SaisStep secondSolution = doSAIS(firstSolution.joinArray(firstSolution.getArrayWithNewNames()));
		
		System.out.println("FIRST: " + firstSolution.joinArray(firstSolution.getArrayWithNewNames()));
		System.out.println("SECOND: " + secondSolution.joinArray(secondSolution.getArrayWithNewNames()));
		*/
		
	}
	
	/*
	 * Returns true if all elements in array are different	
	 * */
	public static boolean areAllElementsDifferent(int[] array) {
		for (int i = 0; i < array.length - 1; i++) {
			for (int j = i + 1; j < array.length; j++) {
				if (array[i] == array[j]) {
					return false;
				}
			}
		}
		return true;
	}
	
	public static SaisStep doSAIS(String inputString) {

		String input = inputString;
		String[] S = input.split("(?!^)");
		
		SaisStep saisStep = new SaisStep();
		saisStep.setS(S);
		
		boolean[] t = saisStep.getTFromS(S);
		saisStep.setT(t);
		ArrayList<Integer> lmsPointersArray = saisStep.getLMSPointersArray(S, t);
		saisStep.setLmsPointersArray(lmsPointersArray);
		
		System.out.println(Arrays.toString(saisStep.getS()));
		System.out.println(Arrays.toString(saisStep.getT()));
		System.out.println(saisStep.getLmsPointersArray());
		
		
		/*
		 * Create SA buckets and insert -1 for each value
		 * */
		TreeMap<String, ArrayList<Integer>> bucketHash = saisStep.getBucketHashFromSAndInitializeToMinusOne(S);
		saisStep.setBucketHash(bucketHash);
		
 		Object[] keySet = bucketHash.keySet().toArray();
 		saisStep.setKeySet(keySet);
 		
 		System.out.println(Arrays.toString(keySet));
 		
 		/*
 		 *	HashMap that stores pointers on bucket arrays
 		 * 	For example, this is how it's done in powerpoint presentations: 
 		 * 	B['$'] = 0; B['A'] = 3; B['C'] = 5; B['G'] = 9; B['T'] = 11;
 		 *	We do the similar thing here :)
 		 * */
 		HashMap<String, Integer> bucketPointers = new HashMap<>();
 		saisStep.setBucketPointers(bucketPointers);
 		saisStep.setAllPointersInBucketsToEnd(keySet, bucketPointers, bucketHash);
 		
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
 		 * Algorithm 1, second iteration
 		 * Set all pointers in buckets to beginning
 		 * */
 		saisStep.setAllPointersInBucketsToBeginning(keySet, bucketPointers);
 		saisStep.doSecondIteration(S, t, bucketHash, bucketPointers);
 		System.out.println("____Second iteration______");
 		for (Entry<String, ArrayList<Integer>> entry : bucketHash.entrySet()) {
 	        System.out.println("key ->" + entry.getKey() + ", value->" + entry.getValue());   
 	    }
 		
 		
 		/*
 		 * Algorithm 1, third iteration
 		 * Set all pointers in buckets to end
 		 * */
 		saisStep.setAllPointersInBucketsToEnd(keySet, bucketPointers, bucketHash);
 		saisStep.doThirdIteration(S, t, bucketHash, bucketPointers, keySet);
 		System.out.println("_____Third iteration__________");
 		for (Entry<String, ArrayList<Integer>> entry : bucketHash.entrySet()) {
 	        System.out.println("key ->" + entry.getKey() + ", value->" + entry.getValue());   
 	    }
 		
 		
 		// TEST
 		// boolean result = compareLMSSubstrings(3, 7, S, t);
 		 //System.out.println(result);
 		
 		// STEP 4,  Get new array with new name for each LMS substring
 		int[] S1 = saisStep.getArrayWithNewNamesForEachLMSSubstring(lmsPointersArray, bucketHash, S, t);
 		saisStep.setArrayWithNewNames(S1);
		System.out.println(Arrays.toString(S1));
		
		return saisStep;
	
	}
	
 	
}
