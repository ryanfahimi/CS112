/*************************************************************************
 * WordSearch.java
 * 
 * Implements a Word Search game by reading in a puzzle from System.in and
 * reading a "search word" from args[0].
 */

////// Imports
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * class WordSearch
 *
 * This class implements a Word Search game.
 * The main() method reads in a puzzle from System.in .
 * main() also reads a "search word" from args[0] .
 *
 * The class implements several static methods that search for
 * the "search word" throughout the puzzle and that handle
 * successful and unsuccessful searches.
 *
 * There are several methods below that must be completed by the student.
 */
public class WordSearch {

	static char ToLower(char x) {
		if (('A' <= x) && (x <= 'Z')) {
			x += 'a' - 'A';
		}
		return x;
	}

	static char ToUpper(char x) {
		if (('a' <= x) && (x <= 'z')) {
			x += 'A' - 'a';
		}
		return x;
	}

	/**
	 * GetPuzzle()
	 *
	 * Read in a puzzle from an InputStream (System.in or a FileInputStream).
	 * Return the result as a two-dimensional array of chars.
	 * 
	 * @param inp - the input stream
	 * @returns - two-dimensional array of chars with the puzzle read from 'inp'
	 */
	static char[][] GetPuzzle(InputStream inp) {
		char[][] retval = null;
		try {
			Scanner scan = new Scanner(inp);

			// Read first line and see how long it is.
			String txtLine = scan.nextLine().trim();
			final int D = txtLine.length();

			// Create output two-dimensional array.
			retval = new char[D][D];

			// Copy already-read first line to our output array.
			for (int n = 0; n < D; n++) {
				retval[0][n] = txtLine.charAt(n);
			}

			// Get and save the rest of the lines.
			for (int lineNum = 1; lineNum < D; lineNum++) {
				txtLine = scan.nextLine();
				for (int n = 0; n < D; n++) {
					retval[lineNum][n] = txtLine.charAt(n);
				}
			}
			scan.close();
		} catch (NoSuchElementException e) {
			System.err.println("ERROR: not enough input data: " + e);
			retval = null;
		} catch (IndexOutOfBoundsException f) {
			System.err.println("ERROR: line too short: " + f);
			retval = null;
		}

		// Convert the entire output array to lowercase.
		final int D = retval.length;
		for (int lineNum = 0; lineNum < D; lineNum++) {
			for (int colNum = 0; colNum < D; colNum++) {
				retval[lineNum][colNum] = ToLower(retval[lineNum][colNum]);
			}
		}

		// Finally return the resulting puzzle
		return retval;
	} // end GetPuzzle()

	/**
	 * SearchFwd()
	 *
	 * Search through 'puzzle' for occurrences of 'word' that happen
	 * in a forward left-to-right order.
	 * If we find 'word', convert it to uppercase in the puzzle and
	 * return true.
	 * Otherwise return false.
	 * 
	 * @param puzzle - the word search puzzle stored as a two-dimensional char array
	 * @param word   - the "search word" i.e. the word to search for in 'puzzle'
	 * @returns - true if 'word' is found in 'puzzle', else false
	 */
	static boolean SearchFwd(char[][] puzzle, String word) {
		final int D = puzzle.length;
		for (int row = 0; row < D; row++) {
			String thisRow = new String(puzzle[row]);
			final int indx = thisRow.indexOf(word);
			if (indx >= 0) {
				// convert the chars to uppercase
				ConvertRowToUpper(puzzle, row, indx, indx + word.length());
				return true;
			}
		}
		return false;
	}

	/**
	 * ConvertRowToUpper()
	 *
	 * Convert the chars in 'row' of 'puzzle' to UPPERCASE, from position 'start' to
	 * 'end'-1.
	 * 
	 * @param puzzle - the word search puzzle
	 * @param row    - which row of the puzzle to convert
	 * @param start  - which column of 'row' to start converting
	 * @param end    - which column of 'row' to stop converting (last converted cell
	 *               is end-1)
	 */
	static void ConvertRowToUpper(char[][] puzzle, int row, int start, int end) {
		for (int n = start; n < end; n++) {
			puzzle[row][n] = ToUpper(puzzle[row][n]);
		}
	}

	/**
	 * SearchBwd()
	 *
	 * Search through 'puzzle' for occurrences of 'word' that happen
	 * in a backward right-to-left order.
	 * If we find 'word', convert it to uppercase in the puzzle and
	 * return true.
	 * Otherwise return false.
	 * 
	 * @param puzzle - the word search puzzle stored as a two-dimensional char array
	 * @param word   - the "search word" i.e. the word to search for in 'puzzle'
	 * @returns - true if 'word' is found in 'puzzle', else false
	 */
	static boolean SearchBwd(char[][] puzzle, String word) {
		String reversedWord = new StringBuilder(word).reverse().toString();
		return SearchFwd(puzzle, reversedWord);
	}

	/**
	 * SearchDown()
	 *
	 * Search through 'puzzle' for occurrences of 'word' that happen
	 * in a downward top-to-bottom order.
	 * If we find 'word', convert it to uppercase in the puzzle and
	 * return true.
	 * Otherwise return false.
	 * 
	 * @param puzzle - the word search puzzle stored as a two-dimensional char array
	 * @param word   - the "search word" i.e. the word to search for in 'puzzle'
	 * @returns - true if 'word' is found in 'puzzle', else false
	 */
	static boolean SearchDown(char[][] puzzle, String word) {
		final int D = puzzle.length;
		for (int col = 0; col < D; col++) {
			StringBuilder thisCol = new StringBuilder();
			for (int row = 0; row < D; row++) {
				thisCol.append(puzzle[row][col]);
			}
			final int indx = thisCol.indexOf(word);
			if (indx >= 0) {
				ConvertColToUpper(puzzle, col, indx, indx + word.length());
				return true;
			}
		}
		return false;
	}

	/**
	 * ConvertColToUpper()
	 *
	 * Convert the chars in 'col' of 'puzzle' to UPPERCASE, from position 'indx' to
	 * 'indx' + 'word'.length()-1.
	 * 
	 * @param puzzle - the word search puzzle
	 * @param word   - the word that was found in 'puzzle'
	 * @param col    - which column of the puzzle to convert
	 * @param indx   - which row of 'col' to start converting
	 */
	private static void ConvertColToUpper(char[][] puzzle, int col, int start, int end) {
		for (int n = start; n < end; n++) {
			puzzle[n][col] = ToUpper(puzzle[n][col]);
		}
	}

	/**
	 * SearchUp()
	 *
	 * Search through 'puzzle' for occurrences of 'word' that happen
	 * in an upward bottom-to-top order.
	 * If we find 'word', convert it to uppercase in the puzzle and
	 * return true.
	 * Otherwise return false.
	 * 
	 * @param puzzle - the word search puzzle stored as a two-dimensional char array
	 * @param word   - the "search word" i.e. the word to search for in 'puzzle'
	 * @returns - true if 'word' is found in 'puzzle', else false
	 */
	static boolean SearchUp(char[][] puzzle, String word) {
		String reversedWord = new StringBuilder(word).reverse().toString();
		return SearchDown(puzzle, reversedWord);
	}

	/**
	 * PrintPuzzle()
	 *
	 * Print out a Word Search puzzle line by line.
	 * 
	 * @param puzzle - the puzzle to be printed
	 */
	static void PrintPuzzle(char[][] puzzle) {
		final int D = puzzle.length;
		for (int row = 0; row < D; row++) {
			for (int col = 0; col < D; col++) {
				System.out.print(puzzle[row][col]);
			}
			System.out.println();
		}
	}

	public static void main(String[] args) {
		// Verify inputs.
		if (args.length < 1) {
			System.err.println("ERROR: no command line argument");
			return;
		}

		// Fetch inputs.
		String searchWord = args[0].toLowerCase();
		char[][] puzzle = null;
		puzzle = GetPuzzle(System.in);
		/*
		 * Here is some code for testing with a file "test.txt"
		 * try {
		 * FileInputStream fis = new FileInputStream("test.txt");
		 * puzzle = GetPuzzle(fis);
		 * } catch (FileNotFoundException fnfe) {
		 * System.err.println("test.txt not found"); }
		 */

		// Now start searching 'puzzle' for 'searchWord'.
		boolean success = SearchFwd(puzzle, searchWord);
		success |= SearchBwd(puzzle, searchWord);
		success |= SearchUp(puzzle, searchWord);
		success |= SearchDown(puzzle, searchWord);

		// Print the results
		if (success) {
			PrintPuzzle(puzzle);
		} else {
			System.out.println("NO MATCH");
		}
	} // end main()

} // end class WordSearch