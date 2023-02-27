import java.util.NoSuchElementException;
import java.util.Scanner;

public class EightDoubles {

    // Define a constant for the number of inputs
    private static final int NUM_INPUTS = 8;   

    public static void main(String[] args) {

        // Declare variables for the sum, minimum, and maximum
        double sum = 0;
        double min = Double.MAX_VALUE; // Initialize to maximum double value so the next scanned double will be smaller or equal
        double max = Double.MIN_VALUE; // Initialize to minimum double value so the next scanned double will be larger or equal
        
        // Utilize try-with-resources block to automatically close the scanner after use
        try (Scanner scanner = new Scanner(System.in)) {
            // Loop through each input
            for (int doubleCounter = 0; doubleCounter < NUM_INPUTS; doubleCounter++) {
                // Prompt the user to enter a double and store it in the num variable
                double num = scanner.nextDouble();
                // Add the num variable to the sum variable
                sum += num;
                // Check if the num variable is less than the current minimum value
                if (num < min) {
                    // Update the minimum variable with the num variable
                    min = num;
                // Check if the num variable is greater than the current maximum value
                } else if (num > max) {
                    // Update the maximum variable with the num variable
                    max = num;
                }
            }

            // Compute the average of the inputs
            double average = sum / NUM_INPUTS;
            // Round the average to two decimal places
            double roundedAverage = ((int) (100 * average)) * 0.01;
        
            // Output the minimum, maximum, and rounded average values
            System.out.println("Minimum: " + min);
            System.out.println("Maximum: " + max);
            System.out.println("Average: " + roundedAverage);

        } catch (NoSuchElementException exception) {
            // Catch an error if the user inputs fewer than eight doubles
            System.err.println("ERROR: Please enter eight doubles");
        }
    }
}