/**
 * This class implements Euclid's algorithm for calculating the Greatest Common
 * Factor (GCF)
 * of two positive integers A and B using recursion.
 */
public class Euclid {

    /**
     * The main method that serves as the entry point for the program.
     * It takes two command-line arguments (A and B) and prints out the GCF of A and
     * B.
     * The method includes error handling for improper user input.
     *
     * @param args Command-line arguments, expecting two positive integers A and B.
     */
    public static void main(String[] args) {
        if (args.length != 2) {
            System.err.println("ERROR: Please provide exactly two command-line arguments.");
            return;
        }

        try {
            int A = Integer.parseInt(args[0]);
            int B = Integer.parseInt(args[1]);

            if (A < 1 || B < 1) {
                System.err.println("ERROR: Both input numbers must be positive integers.");
                return;
            }

            int gcf = calculateGCF(A, B);
            System.out.println(gcf);
        } catch (NumberFormatException e) {
            System.err.println("ERROR: Invalid input. Please enter positive integers only.");
        }
    }

    /**
     * This recursive method calculates the Greatest Common Factor (GCF) of two
     * positive integers A and B
     * using Euclid's algorithm.
     *
     * @param A The first positive integer.
     * @param B The second positive integer.
     * @return The Greatest Common Factor (GCF) of A and B.
     */
    private static int calculateGCF(int A, int B) {
        if (B == 0) {
            return A;
        } else {
            return calculateGCF(B, A % B);
        }
    }
}
