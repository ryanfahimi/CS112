import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;

/**
 * Encode is a class that reads an input text file and encodes it using a
 * Huffman codebook, then writes the encoded data to an output file.
 */
public class Encode {

    /**
     * The main method that takes two command line arguments: input filename and
     * encoded filename. It reads the input file, encodes it using a Huffman
     * codebook, and writes the encoded data to the output file.
     *
     * @param args command line arguments
     */
    public static void main(String[] args) {
        if (args.length != 2) {
            System.err.println("ERROR: Incorrect number of arguments. Expected: <input filename> <encoded filename>");
            return;
        }

        String inputFilename = args[0];
        String encodedFilename = args[1];

        HashMap<Character, String> codebook = new HashMap<>();
        readCodebook(codebook);

        try (BufferedReader reader = Files.newBufferedReader(Paths.get(inputFilename), StandardCharsets.UTF_8);
                BufferedWriter writer = Files.newBufferedWriter(Paths.get(encodedFilename), StandardCharsets.UTF_8)) {

            int c;
            while ((c = reader.read()) != -1) {
                char character = (char) c;
                String huffmanCode = codebook.get(character);
                if (huffmanCode != null) {
                    writer.write(huffmanCode);
                }
            }

            String eotCode = codebook.get((char) 4); // EOT character
            if (eotCode != null) {
                writer.write(eotCode);
            }

        } catch (IOException e) {
            System.err.println("ERROR: " + e.getMessage());
        }
    }

    /**
     * Reads the codebook file and populates the given HashMap with character to
     * Huffman code mappings.
     *
     * @param codebook the HashMap to populate with character to Huffman code
     *                 mappings
     */
    private static void readCodebook(HashMap<Character, String> codebook) {

        try (BufferedReader reader = Files.newBufferedReader(Paths.get("codebook"), StandardCharsets.UTF_8)) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(":");
                char character = (char) Integer.parseInt(parts[0]);
                String huffmanCode = parts[1];
                codebook.put(character, huffmanCode);
            }
        } catch (IOException e) {
            System.err.println("ERROR: Failed to read codebook file - " + e.getMessage());
        }
    }
}
