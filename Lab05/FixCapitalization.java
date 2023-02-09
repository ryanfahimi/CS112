public class FixCapitalization {
    public static void main(String[] args) {
        // Check if exactly one argument is provided, if not, print an error message and
        // exit the program
        if (args.length != 1) {
            System.out.println("Please provide exactly one argument.");
            return;
        }

        // Convert the provided argument to lowercase
        String input = args[0].toLowerCase();

        // Initialize the output string
        String output = "";

        // Create a constant representing the difference between the ASCII value of a
        // capital letter and a lowercase letter
        final int caseDiff = 'A' - 'a';

        // Initialize a boolean to track whether the next character should be
        // capitalized
        boolean capitalizeNext = true;

        // Loop through the input string
        for (int i = 0; i < input.length(); i++) {
            char currentChar = input.charAt(i);

            // Check if the current character is a period, exclamation point, or question
            // mark
            if (currentChar == '.' || currentChar == '!' || currentChar == '?') {
                // If so, set the capitalizeNext flag to true
                capitalizeNext = true;
                // Add the current character to the output string
                output += currentChar;
            } else if (('a' <= currentChar) && (currentChar <= 'z')) {
                // Check if the next character should be capitalized
                if (capitalizeNext) {
                    // If so, convert the current character to its capital equivalent and add it to
                    // the output string
                    output += (char) (currentChar + caseDiff);
                    // Set the capitalizeNext flag to false
                    capitalizeNext = false;
                } else {
                    // If not, add the current character to the output string as is
                    output += currentChar;
                }
            } else {
                // If the current character is not a lowercase letter or punctuation, add it to
                // the output string as is
                output += currentChar;
            }
        }

        // Print the output string
        System.out.println(output);
    }
}
