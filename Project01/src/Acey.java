import java.net.Socket;
import java.io.IOException;
import java.io.DataInputStream;
import java.io.DataOutputStream;

/**
 * The Acey Hand class represents a hand of playing cards in Acey.
 */
class AceyHand {
    private Card[] hand;
    private int numCards;
    private int value;
    private int difference;

    /**
     * Constructs an empty Hand object.
     */
    public AceyHand() {
        hand = new Card[3];
        numCards = 0;
        value = 0;
    }

    /**
     * Adds a card to the hand.
     *
     * @param card The card to add
     */
    public void addCard(Card card) {
        hand[numCards] = card;
        value += card.rank.getInt();
        numCards++;
    }

    public int getDifference() {
        difference = Math.abs(hand[1].rank.getInt() - hand[0].rank.getInt());
        return difference;
    }

    // Getters

    public int getValue() {
        return value;
    }

    public int getNumCards() {
        return numCards;
    }

    public Card[] getHand() {
        return hand;
    }

}

public class Acey {
    private Socket socket;
    private DataInputStream dis;
    private DataOutputStream dos;

    public Acey(String ipAddress, int ipPort) {
        try {
            socket = new Socket(ipAddress, ipPort);
            dis = new DataInputStream(socket.getInputStream());
            dos = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            System.err.println("ERROR: Unable to connect to the server: " + e.getMessage());
            System.exit(1);
        }
    }

    private void write(String s) throws IOException {
        dos.writeUTF(s);
        dos.flush();
    }

    private String read() throws IOException {
        return dis.readUTF();
    }

    /**
     * Handles the login process with the provided message parts.
     *
     * @param commandParts The message parts containing the login information
     * @throws IOException              If an error occurs while writing the login
     *                                  message
     * @throws IllegalArgumentException If the login message format is invalid
     */
    private void handleLogin(String[] commandParts) throws IOException, IllegalArgumentException {
        if (commandParts.length < 2) {
            throw new IllegalArgumentException("Invalid login message format");
        }
        write("rfahimi:AceyDoesIt");
    }

    private void handlePlay(String[] commandParts) throws IOException {
        int bet = 0;
        String decision = (commandParts[4] == commandParts[5]) ? ("high") : ("mid");
        write(decision + ":" + bet);
    }

    private void handleStatus(String[] commandParts) throws IOException {

    }

    private void handleDone(String[] commandParts) throws IOException {

    }

    public void parseCommand() {
        String command;
        boolean done = false;
        while (!done) {
            try {
                command = read();
            } catch (IOException e) {
                System.err.println("ERROR: Unable to read from the server: " + e.getMessage());
                return;
            }
            String[] commandParts = command.split(":");
            command = commandParts[0];

            try {
                switch (command) {
                    case "login":
                        handleLogin(commandParts);
                        break;
                    case "play":
                        handlePlay(commandParts);
                        break;
                    case "status":
                        handleStatus(commandParts);
                        break;
                    case "done":
                        handleDone(commandParts);
                        done = true;
                        break;
                    default:
                        throw new IllegalArgumentException("Unknown command: " + command);
                }
            } catch (IOException e) {
                System.err.println("ERROR: Unable to write to the server: " + e.getMessage());
                return;
            } catch (IllegalArgumentException e) {
                System.err.println("ERROR: Unable to parse the command: " + e.getMessage());
                return;
            }
        }
        try {
            socket.close();
        } catch (IOException e) {
            System.err.println("ERROR: Unable to close the socket: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        String ipAddress;
        int ipPort;
        try {
            ipAddress = args[0];
            ipPort = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            System.err.println("ERROR: Unable to initialize IP Port: IP Port is not a number: " + args[1]);
            return;
        } catch (ArrayIndexOutOfBoundsException e) {
            System.err.println(
                    "ERROR: Unable to initialize IP address and/or IP Port: Missing IP address and/or IP Port");
            return;
        }

        Acey player = new Acey(ipAddress, ipPort);
        player.parseCommand();
    }
}
