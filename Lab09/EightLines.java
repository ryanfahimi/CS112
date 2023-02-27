import java.util.NoSuchElementException;
import java.util.Scanner;

public class EightLines {

    // Declare constants for the program
    private static final int NUM_INPUTS = 8;
    private static final String WORD_DELIMITER = "\\s+"; // Initialize to regex expression that represents all whitespace

    public static void main(String[] args) {
    
        // Utilize try-with-resources block to automatically close the scanner after use
        try (Scanner scanner = new Scanner(System.in)) {

            // Loop through each of the eight expected inputs
            for (int lineCounter = 0; lineCounter < NUM_INPUTS; lineCounter++) {

                // Read in a line of input from the user
                String line = scanner.nextLine();

                // Split the input line into an array of words
                String[] words = line.split(WORD_DELIMITER);

                // Loop through each word in the array and print it to a new line
                for (String word : words) {
                    System.out.println(word);
                }
            }        

        } catch (NoSuchElementException exception) {
            // If the user does not enter eight lines of input, catch the exception and print an error message
            System.err.println("ERROR: Please enter eight lines");
        }

    }
}
