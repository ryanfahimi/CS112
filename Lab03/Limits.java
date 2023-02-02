// This program finds the maximum and minimum byte, short, and int values
class Limits {
    public static void main(String[] args) {
        // Initialize a long variable to store the test values
        long longTest = 0;
        
        // Initialize a byte variable to store the casted value of the long variable
        byte byteTest = 0;
        
        // Use a while loop to check if the casted value is equal to the original value
        // Increment the long variable by 1 until the equality no longer holds
        while (longTest == byteTest) {
            byteTest = (byte) ++longTest;
        }
        // Print the maximum value that can be represented by a byte by subtracting the long variable by 1
        System.out.println("Maximum byte value is " + (longTest-1));
        // Print the minimum value that can be represented by a byte by using the overflowed byte variable
        System.out.println("Minimum byte value is " + (byteTest));

        // Cast the long variable to a short and store it in a short variable
        short shortTest = (short) longTest;
        // Use a while loop to check if the casted value is equal to the original value
        // Increment the long variable by 1 until the equality no longer holds
        while (longTest == shortTest) {
            shortTest = (short) ++longTest;
        }
        // Print the maximum value that can be represented by a short by subtracting the long variable by 1
        System.out.println("Maximum short value is " + (longTest-1));
        // Print the minimum value that can be represented by a short by using the overflowed short variable
        System.out.println("Minimum short value is " + (shortTest));
        
        // Cast the long variable to an int and store it in an int variable
        int intTest = (int) longTest;
        // Use a while loop to check if the casted value is equal to the original value
        // Increment the long variable by 1 until the equality no longer holds
        while (longTest == intTest) {
            intTest = (int) ++longTest;
        }
        // Print the maximum value that can be represented by an int by subtracting the long variable by 1
        System.out.println("Maximum int value is " + (longTest-1));
        // Print the minimum value that can be represented by an int by using the overflowed int variable
        System.out.println("Minimum int value is " + (intTest));
    }
}
