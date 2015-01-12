package bioinformatika;

import java.util.Arrays;

public class SAIS {

	public static void main(String[] args) {
		
		String input = "ATTTAGCGAGCG$";
		
		String[] S = input.split("(?!^)");
		

		System.out.println(Arrays.toString(S));

		System.out.println(Arrays.toString(getT(S)));
			
	
		
		

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

}
