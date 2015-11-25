package stringprob;

public class StringManipulationProblems1 {

	// <b>Problem 1:</b> Determine if a string has all unique chars
	//            O(n)
	public static boolean isUnique(String chars){
		// Example: h e l l o <br>
		// Includes extended ASCII chars.<br>
		// This boolean array serves as a hashmap.
		// Each char has a corresponding ascii value that
		// we use to hash into the map.<br>
		// In this approach we might be wasting space but
		// it is better than using a heavy weight HashMap.
		boolean[] map = new boolean[256];		
		for(int i = 0; i < chars.length(); i++){
			// ascii value of this char
			int charValue = chars.charAt(i);
			System.out.println(charValue);
			// If we find a match it means we have seen this char
			// already, so our string contains repeated chars
			if( map[charValue-1]){
				return false;
			}
		}		
		return true;		
	}
	
	// <b>Problem 2:</b> Remove dup chars from a string 
	public static char[] removeDups(char[] myString){
		// Example: h e l l o s<br>
		// Basic cases
		if( myString == null) return myString;		
		if( myString.length < 2 ) return myString;
		
		// Instead of literally deleting the dups we will
		// move the dups to the end of the string by swapping them
		int swapIndex = myString.length-1;
				
		for( int i = 0; i < myString.length; i++){ // 0, 1, 2, 3, 4, 5
			char currChar = myString[i];
			int j;
			for(j = i + 1; j < swapIndex; j++){ // 1, 2, 
				
				if(currChar == myString[j]){
					// If our dup is equal to the char at the current
					// swapping position at the end of the string,
					// then decrease the swapping position so that we
					// find another .<br>
					// Make sure we dont go too far left
					while( currChar == myString[swapIndex] &&
							                     swapIndex > j +1){
						swapIndex --;						
					}
					
					// We have found a suitable swapping position.
					// Move this dup there
					char temp = myString[swapIndex];
					myString[swapIndex] = currChar;
					myString[j] = temp;
				}
			}	
		}		
		return myString;		
	}
	
	// <b>Problem 3:</b> Determine if a pattern is found in a text
	//              O(nm)
	public static boolean isSubstring (char[] pattern, char[] text){
		int patternLength = pattern.length;
		if(patternLength == 0 || text.length == 0 ) return false;

		// Iterating over text
		for(int i = 0 ; i < text.length && text.length -i >= patternLength; i++){
			System.out.println("i:" + i);
			
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
	
	public static boolean strCompare(char[] str1, char[] str2, int str1Pos, int str2Pos){
		return str1[str1Pos] == str2[str2Pos];
		
	}
	
	// Boyer-Moore algorithm for matching a string within a string
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
	//       		compare
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
	//       		   compare
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
	//       		     compare
	// STEP 4
	//----------------------------------------------------------
	// o == o so keep comparing to the left
	// text:        h e l l o
	//                    ^
	//                    |
	// pattern:           l o 
	//                    ^
	//                    |
	//       		     compare
	// STEP 5
    //----------------------------------------------------------
	// We found a match!
			
	public static boolean isSubstring2 (char[] pattern, char[] text){
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
					skip = pattern.length-1;
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
							skip = pattern.length-1;
						}else{					
							skip = patternMap[text[textPos2]]-1;
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
	
	public static boolean areEqual(Character[] word1, Character[] word2){
		
		if(word1.length != word2.length) return false;
		
		int[] word1Map = new int[256];
		for(int i=0; i < word1.length; i++){
			word1Map[word1[i]]++;
		}
		
		for(int j=0; j < word2.length; j++){
			if(word1Map[word2[j]] <=0) return false;
			word1Map[word2[j]]--;
		}
		
		return true;
		
	}
	
	public static void reverse(char[] word){
		
		for(int i =0; i < word.length/2; i++){
			
			char tmp = word[i];
			word[i] = word[word.length-i-1];
			word[word.length-i-1] = tmp;
		}
	
	}
	
	
	// Run all problems
	public static void main (String[] args){
		
		// Problem 1: Determine if a string has all unique chars
		isUnique("hello");
		// Problem 2: Remove dup chars from a string 
		System.out.println(removeDups(("helllos").toCharArray()));
		System.out.println(removeDups(("hellos").toCharArray()));
		System.out.println(removeDups(("helos").toCharArray()));
		System.out.println(removeDups(("hhellos").toCharArray()));
		System.out.println(removeDups(("lll").toCharArray()));
		System.out.println(removeDups(("lloll").toCharArray()));		
		// Problem 3: Find if a pattern is found in a text
		//System.out.println(isSubstring("Hi".toCharArray(),"Hi sunshine!".toCharArray()));
		//System.out.println(isSubstring("Hi".toCharArray(),"Hello sunshine!".toCharArray()));		
		//System.out.println(isSubstring("aaaaaa".toCharArray(),"aa".toCharArray()));
		//System.out.println(isSubstring("aa".toCharArray(),"aaaaaaaa".toCharArray()));
		//System.out.println(isSubstring("cd".toCharArray(),"efcd".toCharArray()));
		//System.out.println(isSubstring("".toCharArray(),"".toCharArray()));
		System.out.println(isSubstring2("aa".toCharArray(),"caad".toCharArray()));
		System.out.println(isSubstring2("Hi".toCharArray(),"Hi sunshine!".toCharArray()));
		System.out.println(isSubstring2("Hi".toCharArray(),"Hello sunshine!".toCharArray()));
		
		Character[] word1 = {'r','a','t'};
		Character[] word2 = {'a','r','t'};
		System.out.println(areEqual(word1, word2));
		Character[] word3 = {'a','a','t'};
		Character[] word4 = {'a','r','t'};
		System.out.println(areEqual(word3, word4));
		
		char[] word5 = {'h','e','l','l','o'};
	    reverse(word5);
	    System.out.println(word5);
	    char[] word6 = {'h','e','l','o'};
	    reverse(word6);
	    System.out.println(word6);
	    char[] word7 = {};
	    reverse(word7);
	    System.out.println(word7);

	}

}
