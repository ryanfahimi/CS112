// Class to represent the Collatz Conjecture
class Collatz {
    static int runSteps(int n) {
        // Counter to keep track of the number of steps required
        int numberOfSteps = 0;
        int remainder;
        // Repeat the loop until n equals 1
        while (n != 1) {
			// Remainer after dividing n by 2
            remainder = n % 2;
            // If n is even, divide it by 2
            if (remainder == 0) {
                n /= 2;
            } 
            // If n is odd, replace it with 3n + 1
            else {
                n = n * 3 + 1;
            }
            // Increment the step counter
            numberOfSteps++;
        }
        return numberOfSteps;
    }
    

    static public void main(String[] args) {
        // Starting number to perform the calculations on
        int currentNumber = 1;
        // Loop through numbers from 1 to 200
        while (currentNumber <= 200) {
            // Print the current number and the number of steps required to reach 1
            System.out.println(currentNumber + " " + runSteps(currentNumber));
            // Increment the current number
            currentNumber++;
        }
    }
} // end class Collatz
