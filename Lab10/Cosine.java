/** filename: Cosine.java
* Calculates the cosine of a given input angle in radians
* Author: Ryan Fahimi
*/
public class Cosine {

    public static void main(String[] args) {
        try {
            double input = Double.parseDouble(args[0]);
            double cosine = Cosine(input);

            System.out.println("Cosine of " + input + " is " + cosine);

        } catch (ArrayIndexOutOfBoundsException e) {
            System.err.println("ERROR: Zero arguments");

        } catch (NumberFormatException e) {
            System.err.println("ERROR: Input is not a number");
        }

    }

    // Calculates the factorial of a given number
    private static long factorial(long n) {
        if (n < 1) {
            return 1;
        }

        long result = 1;
        for (long i = n; i > 0; i--) {
            result *= i;
        }

        return result;
    }

    // Calculates the power of a given number x to the exponent p
    private static double power(double x, int p) {
        double result = 1;
        for (int i = 0; i < p; i++) {
            result *= x;
        }

        return result;
    }

    // Calculates the cosine of a given input angle in radians using a Taylor series approximation
    public static double Cosine(double input) {
        while (input < -1 * Math.PI) {
            input += 2.0 * Math.PI;
        }

        while (input > Math.PI) {
            input -= 2.0 * Math.PI;
        }

        double result = 1.0;

        for (int n = 1; n <= 5; n++) {
            result -= power(input, 4 * n - 2) / factorial(4 * n - 2);
            result += power(input, 4 * n) / factorial(4 * n);
        }

        return result;
    }

}
