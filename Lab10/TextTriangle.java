/** filename: TextTriangle.java
* Uses nested for loop to create a text triangle from the letters of the alphabet
* Author: Ryan Fahimi
*/
public class TextTriangle {
    public static void main(String[] args) {
        // Define a string variable containing all the letters of the alphabet
        String alphabet = "abcdefghijklmnopqrstuvwxyz";
        
        // Loop through every other letter in the alphabet string starting from index 0
        for (int i = 0; i < alphabet.length(); i+=2) {
            // Loop through the remaining letters in the alphabet string from the current position of the outer loop
            for (int j = i; j < alphabet.length(); j++) {
                // Print the letter at the current index of the inner loop
                System.out.print(alphabet.charAt(j));
            }
            // Print a newline character to start a new row in the triangle
            System.out.println();
        }
    }
}
