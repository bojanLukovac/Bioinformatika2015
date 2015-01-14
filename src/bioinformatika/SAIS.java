package bioinformatika;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.TreeMap;

public class SAIS {

	public static void main(String[] args) throws IOException {
		
		ArrayList<SaisStep> saisSteps = new ArrayList<>();
		
		String input = readFile("/home/drago/Java/bioinformatika/input_data/eserihija.txt", Charset.defaultCharset()) + "$";
		String copiedOriginal = input;
		
		// Do SA-IS steps until we reach array with all different names
		while (true) {
			System.out.println("Entered SA-IS iteration");
			System.out.println("________________________");
			SaisStep solution = doSAIS(input);
			saisSteps.add(solution);
			
			if (areAllElementsDifferent(solution.getArrayWithNewNames()) == false) {
				input = solution.joinArray(solution.getArrayWithNewNames());
			} else {
				break;
			}
		}
		SaisStep methods = saisSteps.get(0);
	
		// GET last SA from last S
		String[] lastS = saisSteps.get(saisSteps.size() - 1).getArrayWithNewNames();
		ArrayList<String> lastSA = new ArrayList<>();
		for (int i = 0; i < lastS.length; i++) {
			lastSA.add("0");
		}
		
		for (int i = 0; i < lastS.length; i++) {
			lastSA.set(Integer.valueOf(lastS[i]), String.valueOf(i));
		}
		
		System.out.println("KREĆEMO S RECOVERIJEM");
		for (int i = saisSteps.size() - 1; i >= 0; i--) {
			System.out.println("______ULAZAK U ITERACIJU _________");
			
			SaisStep step = saisSteps.get(i);
			TreeMap<String, ArrayList<Integer>> bucketHash = step.getBucketHash();
			HashMap<String, Integer> bucketPointers = step.getBucketPointers();
			// Do the first step in recovery
			
			step.setSAToMinusOne(bucketHash);
			
			step.setAllPointersInBucketsToEnd(step.getKeySet(), bucketPointers, bucketHash);
		
			ArrayList<Integer> lmsPointers = step.getLmsPointersArray();
			
			if (i == 0) {
				lastS = copiedOriginal.split("(?!^)");
				step.printArray(lastS, "0 POSLJEDNJI S");
			} else {
				lastS = saisSteps.get(i - 1).getArrayWithNewNames();
				step.printArray(lastS, "POSLJEDNJI S");
			}
			
			
			System.out.println("bucketHash: " + bucketHash);
			System.out.println("bucketPointers: " + bucketPointers);
			System.out.println("lastSA: " + lastSA);
			System.out.println("lastS: " + step.joinArray(lastS));
			
			for (int j = lastSA.size() - 1; j >= 0; j--) {
				String index = lastSA.get(j);
				int lmsPointer = lmsPointers.get(Integer.valueOf(index));
				String correspondingSuffix = lastS[lmsPointer];
				System.out.println("INDEX: " + index);
				System.out.println("Odgovarajuci suffix: " + String.valueOf(correspondingSuffix));
				System.out.println("LMS pointer: " + lmsPointer);
				
				bucketHash.get(String.valueOf(correspondingSuffix))
					.set(bucketPointers.get(String.valueOf(correspondingSuffix)), lmsPointer);
				bucketPointers.put(String.valueOf(correspondingSuffix), bucketPointers.get(String.valueOf(correspondingSuffix)) - 1);
			}
			
			step.setAllPointersInBucketsToBeginning(step.getKeySet(), bucketPointers);
			methods.doSecondIteration(step.getS(), step.getT(), bucketHash, bucketPointers);
			
			step.setAllPointersInBucketsToEnd(step.getKeySet(), bucketPointers, bucketHash);
			methods.doThirdIteration(step.getS(), step.getT(), bucketHash, bucketPointers, step.getKeySet());
			
			System.out.println("BUKETHASH");
	 		for (Entry<String, ArrayList<Integer>> entry : bucketHash.entrySet()) {
	 	        System.out.println("key ->" + entry.getKey() + ", value->" + entry.getValue());   
	 	        
	 	    }
	 	    
	 		lastSA = new ArrayList<>();
	 		
	 		for (Entry<String, ArrayList<Integer>> entry : bucketHash.entrySet()) {
	 	        for (int counter = 0; counter < entry.getValue().size(); counter++) {
	 	        	lastSA.add(String.valueOf(entry.getValue().get(counter)));
	 	        }
	 	    }
	 		
	 		System.out.println("lastSA: " + lastSA);
	 		
	 		/*
	 		System.out.println("BUKETPOJNTER");
	 		for (Entry<String, Integer> entry : bucketPointers.entrySet()) {
	 	        System.out.println("key ->" + entry.getKey() + ", value->" + entry.getValue());   
	 	    }
	 	    */
			
			/*
			for (Integer lmsPointer : lmsPointersArray) {
				String lms = S[lmsPointer];
				bucketHash.get(lms).set(bucketPointers.get(lms), lmsPointer);
				bucketPointers.put(lms, bucketPointers.get(lms) - 1);
			}
			*/
			//System.exit(0);
		}
		
	/*
		System.out.println("PRINTING IT");
		for (SaisStep step : saisSteps) {
			for (Integer number : step.getSA()) {
				System.out.print(number + " ");
			}
			System.out.println();
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
	public static boolean areAllElementsDifferent(String[] array) {
		for (int i = 0; i < array.length - 1; i++) {
			for (int j = i + 1; j < array.length; j++) {
				if (array[i].equals(array[j])) {
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
		
		System.out.println("S = " + inputString);
		for (int i = 0; i < t.length; i++) {
			System.out.print(t[i] + " ");
		}
		System.out.println();
		
		/*
		 * Create SA buckets and insert -1 for each value
		 * */
		TreeMap<String, ArrayList<Integer>> bucketHash = saisStep.getBucketHashFromSAndInitializeToMinusOne(S);
		saisStep.setBucketHash(bucketHash);
		
 		Object[] keySet = bucketHash.keySet().toArray();
 		saisStep.setKeySet(keySet);
 		
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
 		String[] S1 = saisStep.getArrayWithNewNamesForEachLMSSubstring(lmsPointersArray, bucketHash, S, t);
 		SaisStep.printArray(S1, "TESTIRAMO");
 		saisStep.setArrayWithNewNames(S1);
		System.out.println("array with new name: " + Arrays.toString(S1));
		
		return saisStep;
	
	}
	
	static String readFile(String path, Charset encoding) throws IOException {
		byte[] encoded = Files.readAllBytes(Paths.get(path));
		return new String(encoded, encoding);
	}
 	
}
