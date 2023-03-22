/*
 * filename: Blackjack.java
 * Contains classes that represent a blackjack player client that interacts with the dealer server
 * Author: Ryan Fahimi
 */

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * The BlackjackHand class represents a hand of playing cards in Blackjack.
 */
class BlackjackHand {
    private Card[] hand;
    private int numCards;

    /**
     * Constructs an empty Hand object.
     */
    public BlackjackHand() {
        hand = new Card[11]; // Maximum 11 cards in a blackjack hand
        numCards = 0;
    }

    /**
     * Adds a card to the hand.
     *
     * @param card The card to add
     */
    public void addCard(Card card) {
        hand[numCards] = card;
        numCards++;
    }

    /**
     * Returns the numerical value of the hand for Blackjack.
     *
     * @return The hand's value
     */
    public int getValue() {
        int value = 0;
        int numAces = 0;

        for (int i = 0; i < numCards; i++) {
            Card card = hand[i];
            int cardValue = card.getRankInt();
            if (card.getRankString().equals("A")) {
                numAces++;
            }
            value += cardValue;
        }

        // Handle aces as either 1 or 11, depending on the hand value
        while (value > 21 && numAces > 0) {
            value -= 10;
            numAces--;
        }

        return value;
    }

    // Getters

    public int getNumCards() {
        return numCards;
    }

    public Card[] getHand() {
        return hand;
    }
}

/**
 * The Blackjack class represents a simple Blackjack player that connects to a
 * server and plays a game of Blackjack.
 */
public class Blackjack {
    private Socket socket;
    private DataInputStream dis;
    private DataOutputStream dos;

    /**
     * Constructs a Blackjack object and connects to the server at the specified IP
     * address and port.
     * 
     * @param ipAddress The IP address of the server
     * @param ipPort    The port number of the server
     */

    public Blackjack(String ipAddress, int ipPort) {
        try {
            socket = new Socket(ipAddress, ipPort);
            dis = new DataInputStream(socket.getInputStream());
            dos = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            System.err.println("ERROR: Unable to connect to the server: " + e.getMessage());
            System.exit(1);
        }
    }

    /**
     * Writes a string to the server.
     * 
     * @param s The string to send
     * @throws IOException If an I/O error occurs
     */
    private void write(String s) throws IOException {
        dos.writeUTF(s);
        dos.flush();
    }

    /**
     * Reads a string from the server.
     * 
     * @return The received string
     * @throws IOException If an I/O error occurs
     */
    private String read() throws IOException {
        return dis.readUTF();
    }

    /**
     * Determines the bet amount based on the player's bankroll.
     * 
     * @param bankroll The player's current bankroll
     * @return The bet amount
     */
    public int getBetAmount(int bankroll) {
        // In this example, we bet 5% of the bankroll each round
        return (int) Math.max(bankroll * 0.05, 1);
    }

    /**
     * Implements the player's Blackjack strategy based on the dealer's upcard and
     * the player's current hand.
     * 
     * @param dealerUpcard The dealer's upcard
     * @param hand         The player's current hand
     * @throws IOException If an I/O error occurs
     */
    public void play(Card dealerUpcard, AceyHand hand) throws IOException {
        int numCards = hand.getNumCards();
        int handValue = hand.getValue();
        Card[] cards = hand.getHand();
        int dealerUpcardValue = dealerUpcard.getRankInt();

        // Check for split
        if (numCards == 2 && cards[0].getRankString().equals(cards[1].getRankString())) {
            write("split");
        } // Check for double down
        else if (handValue >= 9 && handValue <= 11 && numCards == 2) {
            write("double");
        } // Implement hit or stand strategy based on dealer's upcard
        else if (handValue < 17 && (dealerUpcardValue >= 7 || dealerUpcardValue == 11)) {
            write("hit");
        } else {
            write("stand");
        }
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
        write("rfahimi:21Savage");
    }

    /**
     * Handles the bet placing process with the provided message parts.
     *
     * @param commandParts The message parts containing the bet information
     * @throws IOException              If an error occurs while writing the bet
     *                                  message
     * @throws IllegalArgumentException If the bet message format is invalid
     */
    private void handleBet(String[] commandParts) throws IOException, IllegalArgumentException {
        if (commandParts.length < 2) {
            throw new IllegalArgumentException("Invalid bet message format");
        }
        int bankroll;
        try {
            bankroll = Integer.parseInt(commandParts[1]);
            write("bet:" + getBetAmount(bankroll));
        } catch (NumberFormatException e) {
            System.err
                    .println("ERROR: Unable to initialize bankroll: Bankroll value is not an int: " + commandParts[1]);
            System.exit(1);
        }
    }

    /**
     * Handles the play process with the provided message parts.
     *
     * @param commandParts The message parts containing the play information
     * @throws IOException              If an error occurs while playing
     * @throws IllegalArgumentException If the play message format is invalid
     */
    private void handlePlay(String[] commandParts) throws IOException, IllegalArgumentException {
        if (commandParts.length < 3) {
            throw new IllegalArgumentException("Invalid play message format");
        }
        Card dealerUpCard;
        AceyHand hand = new AceyHand();
        try {
            dealerUpCard = new Card(commandParts[2]);
            for (int i = 4; i < commandParts.length; i++) {
                hand.addCard(new Card(commandParts[i]));
            }
            play(dealerUpCard, hand);
        } catch (IllegalArgumentException e) {
            System.err.println("ERROR: Unable to initialize card: " + e.getMessage());
            System.exit(1);
        }
    }

    /**
     * Handles the status display process with the provided message parts.
     *
     * @param commandParts The message parts containing the status information
     * @throws IllegalArgumentException If the status message format is invalid
     */
    private void handleStatus(String[] commandParts) throws IllegalArgumentException {
        if (commandParts.length < 6) {
            throw new IllegalArgumentException("Invalid status message format");
        }
        System.out.println("Result: " + commandParts[1] + " | Dealer score: " + commandParts[3] + " | Your score: "
                + commandParts[5]);
    }

    /**
     * Handles the completion of the game with the provided message parts.
     *
     * @param commandParts The message parts containing the completion information
     * @throws IllegalArgumentException If the done message format is invalid
     */
    private void handleDone(String[] commandParts) throws IllegalArgumentException {
        if (commandParts.length < 2) {
            throw new IllegalArgumentException("Invalid done message format");
        }
        System.out.println("Game over: " + commandParts[1]);
    }

    /**
     * Parses and handles commands received from the server.
     */
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

                    case "bet":
                        // handleBet(commandParts);
                        write("bet:1");
                        break;

                    case "play":
                        // handlePlay(commandParts);
                        write("stand");
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

    /**
     * The main method that runs the Blackjack client.
     * 
     * @param args Command line arguments containing the IP address and IP port
     *             number
     */
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

        Blackjack player = new Blackjack(ipAddress, ipPort);
        player.parseCommand();
    }
}
