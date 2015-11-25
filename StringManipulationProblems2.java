package stringprob;

public class StringManipulationProblems2 {
	
	
	
	public static boolean strCompare(char[] str1, char[] str2, int str1Pos, int str2Pos){
		return str1[str1Pos] == str2[str2Pos];
		
	}
	
	// Boyer-Moore algorithm for matching a string within a string
	// Explanation from:
	// http://stackoverflow.com/questions/6207819/boyer-moore-algorithm-understanding-and-example
	// EXAMPLE
	// STEP 1
	//---------------------------------------------------------
	// We start by comparing the right-most char of pattern
	// against the corresponding char of text
	// text:        h e l l o
	//                ^
	//                |
	// pattern:     l o 
	//                ^
	//                |
	//             compare
	// STEP 2
	//----------------------------------------------------------
	// o != e and e is not in pattern, so skip by p.length chars
	// If e would have been found in pattern then we can skip m chars
	// to the right, m being the index of the last occurrence of e in p
	// If chars were to match continue comparing to the left until we find
	// an non-match or succeed.
	// text:        h e l l o
	//                    ^
	//                    |
	// pattern:         l o 
	//                    ^
	//                    |
	//                 compare
	// STEP 3
	//----------------------------------------------------------
	// o != l but l is found in p, because l is at the 0th position in
	// p then we can skip by 0, meaning we dont skip at all
	// text:        h e l l o
	//                      ^
	//                      |
	// pattern:           l o 
	//                      ^
	//                      |
	//                   compare
	// STEP 4
	//----------------------------------------------------------
	// o == o so keep comparing to the left
	// text:        h e l l o
	//                    ^
	//                    |
	// pattern:           l o 
	//                    ^
	//                    |
	//                 compare
	// STEP 5
    //----------------------------------------------------------
	// We found a match!
			
	public static boolean isSubstring (char[] pattern, char[] text){
		int patternLength = pattern.length;
		if(patternLength == 0 || text.length == 0 ) return false;
		
		// This map stores what position chars are found in 
		// the pattern. Since by default ints are set to 0,
		// we will offset the positions by 1, so if the
		// char is found at position 0 we will store 1 to avoid
		// getting confused a char that is not found with a char
		// that is at position 0
		int[] patternMap = new int[256];
		
		// Pre processing
		// Save the positions
		for(int i = 0 ; i < patternLength; i++){			
			patternMap[pattern[i]] = i+1;  
		}
		
		int patternPos = pattern.length-1;
		int skip = 0;
		
		for(int textPos = patternPos ; textPos < text.length ; textPos = textPos + skip){
			// If we try to match a char that is not in pattern
			// then we can skip by pattern.length chars 
			// If the aligned chars dont match AND the char in text isnt in
			// pattern then skip by pattern.length char
			if ( !strCompare(pattern, text, patternPos, textPos)){	
				if ( patternMap[text[textPos]] < 1 ){
					// Since the char isnt found in pattern
					// we can skip by the whole length of pattern
					skip = pattern.length;
				}else{		
					// We found the char in pattern so we will
					// skip up to the position of that char
					// so that we can continue checking from then on
					// (the intention is to reuse the comparison already done)
					skip = patternMap[text[textPos]]-1;
				}				
			}else{// char at last index in pattern matched, keep checking backwards
				int textPos2 = textPos-1;
				int matches = 1;
				for(int j = patternPos-1; j >= 0; j--, textPos2--){
					if ( !strCompare(pattern, text, j, textPos2)){	
						
						if ( patternMap[text[textPos2]] < 1 ){
							skip = patternMap[text[textPos2]]-1;
						}else{					
							skip = pattern.length;
						}	
						break;
					}
					matches++;					
				}
				
				if (matches == patternLength){
					return true;
				}
			}			
		}
		return false;		
	}
	
	
	
	// <b>Problem 3:</b> Determine if a pattern is found in a text O(nm)
	public static boolean isSubstringNaive (char[] pattern, char[] text){
		int patternLength = pattern.length;
		if(patternLength == 0 || text.length == 0 ) return false;

		// Iterating over text
		for(int i = 0 ; i < text.length && text.length -i >= patternLength; i++){
			
			int numMatch = 0;
			// Iterating over the pattern
			for (int j = 0; j < patternLength; j++){
				
				if( pattern[j] != text[i+j]){
					break;
				}
				numMatch++;
			}
			if( numMatch == patternLength){
				return true;
			}
		}				
		return false;		
	}
	// Run all problems
	public static void main (String[] args){
				
		// Naive
		System.out.println("Naive");
		System.out.println(isSubstringNaive("Hi".toCharArray(),"Hi sunshine!".toCharArray()));
		System.out.println(isSubstringNaive("Hi".toCharArray(),"Hello sunshine!".toCharArray()));		
		System.out.println(isSubstringNaive("aaaaaa".toCharArray(),"aa".toCharArray()));
		System.out.println(isSubstringNaive("aa".toCharArray(),"aaaaaaaa".toCharArray()));
		System.out.println(isSubstringNaive("cd".toCharArray(),"efcd".toCharArray()));
		System.out.println(isSubstringNaive("".toCharArray(),"".toCharArray()));
		System.out.println(isSubstringNaive("aa".toCharArray(),"caad".toCharArray()));
		
		// Boyer-Moore
		System.out.println("Boyer-Moore");
		System.out.println(isSubstring("Hi".toCharArray(),"Hi sunshine!".toCharArray()));
		System.out.println(isSubstring("Hi".toCharArray(),"Hello sunshine!".toCharArray()));		
		System.out.println(isSubstring("aaaaaa".toCharArray(),"aa".toCharArray()));
		System.out.println(isSubstring("aa".toCharArray(),"aaaaaaaa".toCharArray()));
		System.out.println(isSubstring("cd".toCharArray(),"efcd".toCharArray()));
		System.out.println(isSubstring("".toCharArray(),"".toCharArray()));
		System.out.println(isSubstring("aa".toCharArray(),"caad".toCharArray()));

	}

}



