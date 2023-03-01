/** filename: Calculator.java
* Lets user run common math operations on a number
* Author: Ryan Fahimi
*/
import java.util.InputMismatchException;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Calculator {
    public static void main(String[] args) {

        try (Scanner scanner = new Scanner(System.in)) {
            // Get the user's input number
            double number = getInputNumber(scanner);
            // Get the user's selected math function
            String function = getMathFunction(scanner);
            // Calculate the result of the selected function applied to the input number
            double result = calculateResult(number, function);
            // Print the result
            System.out.println("Result: " + result);

        } catch (InputMismatchException invalidNumber) {
            System.err.println("ERROR: Input is not a number");

        } catch (NoSuchElementException noInput) {
            System.err.println("ERROR: No input");

        } catch (StringIndexOutOfBoundsException inputTooShort) {
            System.err.println("ERROR: Input is composed of less than three characters");

        } catch (ArithmeticException invalidLog) {
            System.err.println("ERROR: " + invalidLog.getMessage());

        } catch (IllegalArgumentException invalidFunction) {
            System.err.println("ERROR: " + invalidFunction.getMessage());
        }
    }


    private static double getInputNumber(Scanner scanner) throws InputMismatchException, NoSuchElementException {
        // Prompt the user to enter a number
        System.out.print("Enter a number: ");
        // Get the user's input number
        double number = scanner.nextDouble();
        return number;
    }

    private static String getMathFunction(Scanner scanner) throws NoSuchElementException, StringIndexOutOfBoundsException {
        // Prompt the user to enter a math function
        System.out.print("Enter a math function (sine, cosine, tangent, or logarithm): ");
        // Get the user's selected math function and convert it to lowercase
        String function = scanner.next().substring(0, 3).toLowerCase();
        return function;
    }

    private static double calculateResult(double number, String function) throws ArithmeticException, IllegalArgumentException {
        double result = 0;
        // Apply the selected math function to the input number
        switch (function) {
            case "sin":
                result = Math.sin(number);
                break;
            case "cos":
                result = Math.cos(number);
                break;
            case "tan":
                result = Math.tan(number);
                break;
            case "log":
                // Check if the input number is invalid for the logarithm function
                if (number == 0) {
                    throw new ArithmeticException("Logarithm of zero");
                } else if (number < 0) {
                    throw new ArithmeticException("Logarithm of a negative number");
                }
                // Calculate the result of the logarithm function applied to the input number
                result = Math.log(number);
                break;
            default:
                // Throw an exception if the selected function is unrecognized
                throw new IllegalArgumentException("Unrecognized function");
        }
        return result;
    }
}
