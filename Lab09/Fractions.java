public class Fractions {
    // Constants for the program
    private static final int EXPECTED_ARGS_COUNT = 1;
    private static final String WHITESPACE_DELIMITER = "\\s+"; // Initialize to regular expression that represents all whitespace
    private static final String SLASH_DELIMITER = "/";

    public static void main(String[] args) {
        // Check that there is exactly one argument
        if (args.length != EXPECTED_ARGS_COUNT) {
            System.err.println("ERROR: Expected one argument");
            return;
        }

        String input = args[0].trim();  // Remove leading/trailing spaces

        // Check if the input is a whole number
        try {
            printWholeNumber(input);
            return;
        } catch (NumberFormatException nfe) {
            // Invalid whole number format, so continue
        }

        // Split the input into parts using whitespace delimiter
        String[] inputParts = input.split(WHITESPACE_DELIMITER);

        // Check if the input is a simple or improper fraction
        if (inputParts.length == 1) {
            String[] fractionParts = inputParts[0].split(SLASH_DELIMITER);
            if (fractionParts.length == 2) {
                try {
                    printFraction(fractionParts);
                    return;
                } catch (NumberFormatException nfe) {
                    // Invalid simple or improper fraction format, so continue
                }
            }
        // Check if the input is a mixed number
        } else if (inputParts.length == 2) {
            String[] mixedNumberFractionParts = inputParts[1].split(SLASH_DELIMITER);
            if (mixedNumberFractionParts.length == 2) {
                try {
                    printMixedNumber(inputParts, mixedNumberFractionParts);
                    return;
                } catch (NumberFormatException nfe) {
                    // Invalid mixed number format, so continue
                }
            }
        }

        // If we haven't returned yet, then the input is invalid
        System.err.println("ERROR: Invalid fraction format");
    }

    // Method to print a whole number as a decimal
    private static void printWholeNumber(String wholeNumberString) {
        double wholeNumber = Double.parseDouble(wholeNumberString);
        double decimalValue = wholeNumber;
        System.out.println(decimalValue);
    }

    // Method to print a simple or improper fraction as a decimal
    private static void printFraction(String[] fractionParts) {
        double numerator = Double.parseDouble(fractionParts[0]);
        double denominator = Double.parseDouble(fractionParts[1]);
        double decimalValue = numerator / denominator;
        System.out.println(decimalValue);
    }

    // Method to print a mixed number as a decimal
    private static void printMixedNumber(String[] mixedNumberParts, String[] mixedNumberFractionParts) {
        double wholeNumber = Double.parseDouble(mixedNumberParts[0]);
        double numerator = Double.parseDouble(mixedNumberFractionParts[0]);
        double denominator = Double.parseDouble(mixedNumberFractionParts[1]);
        double decimalValue = numerator / denominator;
        // Convert mixed number to improper fraction and calculate decimal value
        decimalValue = (wholeNumber < 0) ? (wholeNumber - decimalValue) : (wholeNumber + decimalValue);
        System.out.println(decimalValue);
    }
}
