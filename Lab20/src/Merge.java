
/*****************************************************************
 * Merge.java
 * 
 * Contains a class that reads three sorted input files and merges
 * them into a single sorted list without performing any sort operation.
 */

//////
////// Imports
//////
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

//////
////// Class definitions
//////
/**
 * Merge class reads three sorted input files and merges them into a single
 * sorted list without performing any sort operation.
 *
 * @author Ryan Fahimi
 */
public class Merge {

    /**
     * Main method that processes input arguments and calls the merge process.
     *
     * @param args command line arguments containing file names
     */
    public static void main(String[] args) {
        if (args.length != 3) {
            System.err.println("ERROR: Please provide exactly 3 input file names.");
            return;
        }

        ArrayList<Integer> list1, list2, list3;

        try {
            list1 = readFile(args[0]);
            list2 = readFile(args[1]);
            list3 = readFile(args[2]);
        } catch (FileNotFoundException e) {
            System.err.println("ERROR: " + e.getMessage());
            return;
        }

        ArrayList<Integer> mergedList = mergeSortedLists(list1, list2, list3);
        System.out.println(mergedList);
    }

    /**
     * Reads a file and returns its contents as an ArrayList of integers.
     *
     * @param filename the name of the input file
     * @return an ArrayList of integers from the input file
     * @throws FileNotFoundException if the file is not found
     */
    public static ArrayList<Integer> readFile(String filename) throws FileNotFoundException {
        File file = new File(filename);
        if (!file.exists()) {
            throw new FileNotFoundException("File '" + filename + "' not found.");
        }

        ArrayList<Integer> list = new ArrayList<>();
        Scanner scanner = new Scanner(file);
        while (scanner.hasNextInt()) {
            list.add(scanner.nextInt());
        }
        scanner.close();

        return list;
    }

    /**
     * Merges three sorted ArrayLists into a single sorted ArrayList.
     *
     * @param list1 the first sorted ArrayList
     * @param list2 the second sorted ArrayList
     * @param list3 the third sorted ArrayList
     * @return a new merged and sorted ArrayList
     */
    public static ArrayList<Integer> mergeSortedLists(ArrayList<Integer> list1, ArrayList<Integer> list2,
            ArrayList<Integer> list3) {
        ArrayList<Integer> mergedList = new ArrayList<>();
        int i = 0, j = 0, k = 0;

        while (i < list1.size() || j < list2.size() || k < list3.size()) {
            int min = Integer.MAX_VALUE;

            if (i < list1.size() && list1.get(i) < min) {
                min = list1.get(i);
            }
            if (j < list2.size() && list2.get(j) < min) {
                min = list2.get(j);
            }
            if (k < list3.size() && list3.get(k) < min) {
                min = list3.get(k);
            }

            mergedList.add(min);

            if (i < list1.size() && list1.get(i) == min) {
                i++;
            }
            if (j < list2.size() && list2.get(j) == min) {
                j++;
            }
            if (k < list3.size() && list3.get(k) == min) {
                k++;
            }
        }

        return mergedList;
    }
}
