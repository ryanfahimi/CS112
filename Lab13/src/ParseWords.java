/**
 * filename: ParseWords.java
 * Contains word parser that splits input by colons
 * Author: Ryan Fahimi
 */

/**
 * This is a class called ParseWords that contains a main method which splits
 * input string into words using colons as separators.
 */
public class ParseWords {
    private static final String COLON = ":";

    /**
     * The main method takes an input string from the command line and splits it
     * into words based on colons.
     * If no input is provided, it prints an error message and returns.
     * If the input starts or ends with a colon, it prints "BLANK".
     * For each word in the input, it prints the word unless the word itself is
     * empty, in which case it prints "BLANK".
     * 
     * @param args The command-line arguments passed to the program.
     */
    public static void main(String[] args) {
        if (args.length == 0) {
            System.err.println("Please provide input with colons as separators");
            return;
        }

        String input = args[0];
        String[] words = input.split(COLON);

        if (input.startsWith(COLON)) {
            System.out.println("BLANK");
        }

        for (String word : words) {
            if (word.isEmpty()) {
                System.out.println("BLANK");
            } else {
                System.out.println(word);
            }
        }

        if (input.endsWith(COLON)) {
            System.out.println("BLANK");
        }
    }
}
