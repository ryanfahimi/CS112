/***********************************************************
 * SortTest.java
 * 
 * Reads the "sort.txt" file and ensures the values are in the correct order.
 */

//////
////// Imports
//////

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

//////
////// Class definitions
//////
/**
 * SortTest class reads the "sort.txt" file and checks if it contains the
 * expected number of values and ensures the values are in non-decreasing order.
 * If the test passes, it prints "PASS"; otherwise, it prints an appropriate
 * error message.
 * 
 * @author Ryan Fahimi
 */
public class SortTest {
    private static final int EXPECTED_LENGTH = 10000;
    private static final String FILE_NAME = "sort.txt";

    /**
     * The main method reads the "sort.txt" file and checks if it contains the
     * expected number of values and ensures the values are in non-decreasing order.
     * If the
     * test passes, it prints "PASS"; otherwise, it prints an appropriate error
     * message.
     * 
     * @param args command-line arguments (unused)
     */
    public static void main(String[] args) {
        ArrayList<Integer> values = new ArrayList<>();

        readValuesFromFile(FILE_NAME, values);

        validateElementCount(values);

        validateSortOrder(values);

        System.out.println("PASS");
    }

    /**
     * Reads integer values from the given file and adds them to the values list.
     * 
     * @param filename the name of the input file
     * @param values   an ArrayList of integers to store values from the input file
     */
    private static void readValuesFromFile(String filename, ArrayList<Integer> values) {
        try (Scanner scanner = new Scanner(new File(filename))) {
            while (scanner.hasNextInt()) {
                values.add(scanner.nextInt());
            }
        } catch (IOException e) {
            System.err.println("IOError: " + e);
            System.exit(1);
        }
    }

    /**
     * Validates that the values list contains the expected number of elements.
     * 
     * @param values an ArrayList of integers
     */
    private static void validateElementCount(ArrayList<Integer> values) {
        if (values.size() != EXPECTED_LENGTH) {
            System.err.println("FAIL incorrect element count");
            System.exit(1);
        }
    }

    /**
     * Validates that the values in the list are sorted in non-decreasing order.
     * 
     * @param values an ArrayList of integers
     */
    private static void validateSortOrder(ArrayList<Integer> values) {
        for (int i = 1; i < values.size(); i++) {
            if (values.get(i - 1) > values.get(i)) {
                System.err.println("FAIL incorrect sort");
                System.exit(1);
            }
        }
    }
}
