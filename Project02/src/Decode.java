import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * HuffmanTree is a class that represents a Huffman tree and provides
 * functionality to build the tree and decode characters using the tree.
 */
class HuffmanTree {
    /**
     * Node is a class that represents a node in the Huffman tree.
     */
    class Node {
        private static final char DEFAULT = (char) 0; // default character
        final char character;
        private Node left;
        private Node right;

        /**
         * Constructor for creating a new Node with default values.
         */
        Node() {
            this.character = DEFAULT;
            this.left = null;
            this.right = null;
        }

        /**
         * Constructor for creating a new Node with the given character.
         *
         * @param character the character to be stored in the node
         */
        Node(char character) {
            this.character = character;
            this.left = null;
            this.right = null;
        }
    }

    Node root;

    /**
     * Constructor for creating a new HuffmanTree and initializing the root node.
     */
    HuffmanTree() {
        root = new Node();
        buildTree();
    }

    /**
     * Builds the Huffman tree using the codebook file.
     */
    void buildTree() {
        try (BufferedReader reader = Files.newBufferedReader(Paths.get("codebook"), StandardCharsets.UTF_8)) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(":");
                char character = (char) Integer.parseInt(parts[0]);
                String huffmanCode = parts[1];

                Node currentNode = root;
                for (char bit : huffmanCode.toCharArray()) {
                    if (bit == '0') {
                        if (currentNode.left == null) {
                            currentNode.left = new Node(character);
                        }
                        currentNode = currentNode.left;
                    } else if (bit == '1') {
                        if (currentNode.right == null) {
                            currentNode.right = new Node(character);
                        }
                        currentNode = currentNode.right;
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("ERROR: Failed to read codebook file - " + e.getMessage());
        }
    }

    /**
     * Decodes the next character in the input stream using the Huffman tree.
     *
     * @param reader the BufferedReader used to read the input stream
     * @return the decoded character
     * @throws IOException if an I/O error occurs while reading from the input
     *                     stream
     */
    char decodeNextCharacter(BufferedReader reader) throws IOException {
        Node currentNode = root;
        int c;
        while (currentNode.left != null || currentNode.right != null) {
            c = reader.read();
            char bit = (char) c;
            if (bit == '0') {
                currentNode = currentNode.left;
            } else if (bit == '1') {
                currentNode = currentNode.right;
            }
        }
        return currentNode.character;
    }
}

/**
 * Decode is a class that reads an encoded file, decodes it using a Huffman
 * tree,
 * and writes the decoded data to an output file.
 */
public class Decode {

    /**
     * The main method that takes two command line arguments: encoded filename and
     * decoded filename. It reads the encoded file, decodes it using a Huffman tree,
     * and writes the decoded data to the output file.
     *
     * @param args command line arguments
     */
    public static void main(String[] args) {
        final char EOT = (char) 4; // EOT (End of Transmission) character
        if (args.length != 2) {
            System.err.println("ERROR: Incorrect number of arguments. Expected: <encoded filename> <decoded filename>");
            return;
        }

        String encodedFilename = args[0];
        String decodedFilename = args[1];

        HuffmanTree huffmanTree = new HuffmanTree();

        try (BufferedReader reader = Files.newBufferedReader(Paths.get(encodedFilename), StandardCharsets.UTF_8);
                BufferedWriter writer = Files.newBufferedWriter(Paths.get(decodedFilename), StandardCharsets.UTF_8)) {

            char decodedChar;
            while ((decodedChar = huffmanTree.decodeNextCharacter(reader)) != EOT) {
                writer.write(decodedChar);
            }

        } catch (IOException e) {
            System.err.println("ERROR: " + e.getMessage());
        }
    }
}
