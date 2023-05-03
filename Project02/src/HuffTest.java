
/**
 * This program compares two text files to check if they are identical after filtering disallowed characters.
 * It is mainly used to test the Huffman coding and decoding process.
 * @filename HuffTest.java
 * @author Ryan Fahimi
 */
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * A class that compares an input file and a decoded file character
 * by character
 * and reports if they match or not.
 */
public class HuffTest {

    /**
     * A helper class to manage characters and positions while reading files.
     */
    private static class Char {
        private int temp;
        private int current;
        private int position;

        /**
         * Constructor initializes Char with default values.
         */
        public Char() {
            this.temp = 0;
            this.current = 0;
            this.position = -1;
        }

        /**
         * Checks if the temp character is a disallowed character.
         * 
         * @return true if the character is disallowed, false otherwise.
         */
        private boolean isDisallowedChar() {
            return !(this.temp >= '\u0007' && this.temp <= '\u00FE');
        }

        /**
         * Checks if the end of the file has been reached.
         * 
         * @return true if the end of the file has been reached, false otherwise.
         */
        private boolean isEOT() {
            return (this.temp == -1);
        }

        /**
         * Reads the next allowed character from the file.
         * 
         * @param reader the BufferedReader to read the character from.
         * @throws IOException if there's an issue with reading from the file.
         */
        private void readNextAllowedChar(BufferedReader reader) throws IOException {
            boolean eot;
            do {
                this.temp = reader.read();
                eot = isEOT();
                if (eot) {
                    break;
                }
                this.position++;
            } while (isDisallowedChar());
            if (!eot) {
                this.current = this.temp;
            }
        }
    }

    /**
     * Main method for running the program.
     * 
     * @param args command line arguments: <input filename> <decoded filename>
     */
    public static void main(String[] args) {
        if (args.length != 2) {
            System.err.println("ERROR: Incorrect number of arguments. Expected: <input filename> <decoded filename>");
            return;
        }

        String inputFilename = args[0];
        String decodedFilename = args[1];

        try {
            if (compareFiles(inputFilename, decodedFilename)) {
                System.out.println("PASS");
            }
        } catch (IOException e) {
            System.err.println("ERROR: " + e.getMessage());
        }
    }

    /**
     * Compares the contents of two files character by character after filtering
     * disallowed characters.
     * 
     * @param inputFilename   the name of the input file.
     * @param decodedFilename the name of the decoded file.
     * @return true if the contents of the files are identical, false otherwise.
     * @throws IOException if there's an issue with reading from the files.
     */
    private static boolean compareFiles(String inputFilename, String decodedFilename) throws IOException {
        try (BufferedReader inputReader = Files.newBufferedReader(Paths.get(inputFilename), StandardCharsets.UTF_8);
                BufferedReader decodedReader = Files.newBufferedReader(Paths.get(decodedFilename),
                        StandardCharsets.UTF_8)) {

            Char inputChar = new Char();
            Char decodedChar = new Char();

            while (!inputChar.isEOT() && !decodedChar.isEOT()) {
                inputChar.readNextAllowedChar(inputReader);

                decodedChar.readNextAllowedChar(decodedReader);

                if (inputChar.temp != decodedChar.temp) {
                    System.out.printf("FAIL input %c @ %d output %c @ %d%n", (char) inputChar.current,
                            inputChar.position,
                            (char) decodedChar.current, decodedChar.position);
                    return false;
                }
            }

            return true;
        }
    }
}
