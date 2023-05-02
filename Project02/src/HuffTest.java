import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * HuffTest is a class that compares an input file and a decoded file character
 * by character
 * and reports if they match or not.
 */
public class HuffTest {

    /**
     * The main method that takes two command line arguments: input filename and
     * decoded filename.
     * It compares the files and outputs "PASS" if they match or "FAIL" if they
     * don't.
     *
     * @param args command line arguments
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
     * Checks if the given character is allowed or not.
     *
     * @param c character to check
     * @return true if the character is not allowed, false otherwise
     */
    private static boolean isDisallowedCharacter(char c) {
        return !(c >= '\u0007' && c <= '\u00FE');
    }

    /**
     * Reads the next allowed character from the given reader.
     *
     * @param reader      the reader to read from
     * @param tempChar    the next character to read
     * @param currentChar the current character
     * @param position    the position of the current character
     * @return an array of integers containing the next character to read, the
     *         current character, and the position of the current character
     * @throws IOException if there's an error reading the file
     */
    private static int[] readNextAllowedChar(BufferedReader reader, int tempChar, int currentChar, int position)
            throws IOException {
        tempChar = reader.read();
        if (tempChar != -1) {
            position++;
            currentChar = tempChar;
            while (tempChar != -1 && isDisallowedCharacter((char) currentChar)) {
                int[] result = readNextAllowedChar(reader, tempChar, currentChar, position);
                tempChar = result[0];
                currentChar = result[1];
                position = result[2];
            }
        }
        return new int[] { tempChar, currentChar, position };
    }

    /**
     * Compares the given input file and decoded file character by character.
     *
     * @param inputFilename   the path of the input file
     * @param decodedFilename the path of the decoded file
     * @return true if the files match, false otherwise
     * @throws IOException if there's an error reading the files
     */
    private static boolean compareFiles(String inputFilename, String decodedFilename) throws IOException {
        try (BufferedReader inputReader = Files.newBufferedReader(Paths.get(inputFilename), StandardCharsets.UTF_8);
                BufferedReader decodedReader = Files.newBufferedReader(Paths.get(decodedFilename),
                        StandardCharsets.UTF_8)) {

            int[] inputResult;
            int[] decodedResult;
            int tempInputChar = 0, tempDecodedChar = 0;
            int inputChar = 0, decodedChar = 0;
            int inputPosition = -1, decodedPosition = -1;

            while (tempInputChar != -1 || tempDecodedChar != -1) {
                inputResult = readNextAllowedChar(inputReader, tempInputChar, inputChar,
                        inputPosition);
                tempInputChar = inputResult[0];
                inputChar = inputResult[1];
                inputPosition = inputResult[2];

                decodedResult = readNextAllowedChar(decodedReader, tempDecodedChar, decodedChar,
                        decodedPosition);
                tempDecodedChar = decodedResult[0];
                decodedChar = decodedResult[1];
                decodedPosition = decodedResult[2];

                if (tempInputChar != tempDecodedChar) {
                    System.out.printf("FAIL input %c @ %d output %c @ %d%n", (char) inputChar, inputPosition,
                            (char) decodedChar, decodedPosition);
                    return false;
                }
            }

            return true;
        }
    }
}
