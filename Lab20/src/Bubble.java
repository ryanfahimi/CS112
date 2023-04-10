/***********************************************************
 * Bubble.java
 * 
 * Generate random data, sort it, and print the time
 * required to perform the sort.  Save the sorted data
 * to a textfile "sort.txt".
 */

//////
////// Imports
//////
import java.util.Random;
import java.io.*;

//////
////// Class definitions
//////
/**
 * class Bubble
 *
 * A class that generates random data, performs a Java bubble sort,
 * and outputs the sorted data to a file "sort.txt".
 * The time required for the sort is printed out.
 *
 * @author phaskell
 *
 */
public class Bubble {
	final static int Length = 10000;

	/**
	 * SetValues()
	 *
	 * Assigns random integer values to the provided int array.
	 * 
	 * @param values - the array to be filled in
	 */
	private static void SetValues(int[] values) {
		final int L = values.length;
		Random randGen = new Random();
		for (int i = 0; i < L; i++) {
			values[i] = randGen.nextInt(4 * L);
		}
	}

	/**
	 * doSort()
	 *
	 * Perform an in-place bubble sort on 'values'.
	 * 
	 * @param values - the data to sort
	 */
	private static void doSort(int[] values) {
		for (int i = values.length - 1; i > 0; --i) {
			for (int j = 1; j <= i; j++) {
				if (values[j - 1] > values[j]) {
					final int tmp = values[j - 1];
					values[j - 1] = values[j];
					values[j] = tmp;
				}
			}
		}
	}

	public static void main(String[] args) {
		// Create a list of integers
		int values[] = new int[Length];

		// Assign random values to values[]
		SetValues(values);

		// Note the time just before starting to sort
		final long startTime = System.currentTimeMillis();

		// Bubble sort the data
		doSort(values);

		// Note end time and print difference in seconds
		final long endTime = System.currentTimeMillis();
		System.out.printf("%f\n", (endTime - startTime) / 1000.0);

		// Print the sorted values to file "sort.txt".
		try {
			File theFile = new File("sort.txt");
			PrintWriter pw = new PrintWriter(theFile);
			for (int i = 0; i < values.length; i++) {
				pw.printf("%d ", values[i]);
			}
			pw.println();
			pw.close();
		} catch (IOException e) {
			System.err.println("IOError: " + e);
		}
	} // end main()

} // end class Bubble
