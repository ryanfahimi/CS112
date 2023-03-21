
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
 * The Rank enumeration defines the 13 possible card ranks in a standard 52-card
 * deck.
 */
enum Rank {
    TWO("2"), THREE("3"), FOUR("4"), FIVE("5"), SIX("6"), SEVEN("7"), EIGHT("8"), NINE("9"),
    TEN("10"), JACK("J"), QUEEN("Q"), KING("K"), ACE("A");

    private final String rank;

    /**
     * Constructor for the Rank enumeration.
     *
     * @param rank The rank as a string.
     */
    Rank(String rank) {
        this.rank = rank;
    }

    /**
     * Returns the rank as a string.
     *
     * @return The rank as a string.
     */
    public String getRank() {
        return rank;
    }

    /**
     * Returns the Rank enumeration that matches the given string.
     *
     * @param rankString The rank as a string.
     * @return The corresponding Rank enumeration, or null if no match is found.
     */
    public static Rank fromString(String rankString) {
        for (Rank rank : Rank.values()) {
            if (rank.getRank().equals(rankString)) {
                return rank;
            }
        }
        return null;
    }
}

/**
 * The Suit enumeration defines the 4 possible card suits in a standard 52-card
 * deck.
 */
enum Suit {
    SPADES("S"), HEARTS("H"), DIAMONDS("D"), CLUBS("C");

    private final String suit;

    /**
     * Constructor for the Suit enumeration.
     *
     * @param suit The suit as a string.
     */
    Suit(String suit) {
        this.suit = suit;
    }

    /**
     * Returns the suit as a string.
     *
     * @return The suit as a string.
     */
    public String getSuit() {
        return suit;
    }

    /**
     * Returns the Suit enumeration that matches the given string.
     *
     * @param suitString The suit as a string.
     * @return The corresponding Suit enumeration, or null if no match is found.
     */
    public static Suit fromString(String suitString) {
        for (Suit suit : Suit.values()) {
            if (suit.getSuit().equals(suitString)) {
                return suit;
            }
        }
        return null;
    }
}

/**
 * The Card class represents a playing card with a rank and suit.
 */
class Card {
    private Rank rank;
    private Suit suit;

    /**
     * Constructs a Card object given a card string in the format "rank + suit".
     *
     * @param card The card string in the format "rank + suit".
     * @throws IllegalArgumentException If the card string is invalid.
     */
    public Card(String card) throws IllegalArgumentException {
        String[] parsedCard = parseCard(card);
        if (parsedCard != null) {
            this.rank = Rank.fromString(parsedCard[0]);
            this.suit = Suit.fromString(parsedCard[1]);
        } else {
            throw new IllegalArgumentException("Invalid card format: " + card);
        }
    }

    /**
     * Parses the card string to extract the rank and suit.
     *
     * @param card The card string in the format "rank + suit"
     * @return A string array containing the rank and suit, or null if the format is
     *         invalid
     * @throws IllegalArgumentException If the card string is null or empty
     */
    private String[] parseCard(String card) throws IllegalArgumentException {
        if (card == null || card.isEmpty()) {
            throw new IllegalArgumentException("Null or empty card string");
        }
        for (int i = 1; i <= 2; i++) {
            String rank = card.substring(0, i).toUpperCase();
            String suit = card.substring(i).toUpperCase();
            if (isValidRank(rank) && isValidSuit(suit)) {
                return new String[] { rank, suit };
            }
        }
        return null;
    }

    /**
     * Checks if the provided rank string is a valid rank.
     *
     * @param rank The rank string to be checked for validity
     * @return true if the provided rank string is a valid rank, false otherwise
     */
    private boolean isValidRank(String rank) {
        return Rank.fromString(rank) != null;
    }

    /**
     * Checks if the provided suit string is a valid suit.
     *
     * @param suit The suit string to be checked for validity
     * @return true if the provided suit string is a valid suit, false otherwise
     */
    private boolean isValidSuit(String suit) {
        return Suit.fromString(suit) != null;
    }

    // Getters

    public String getRank() {
        return rank.getRank();
    }

    public String getSuit() {
        return suit.getSuit();
    }

    /**
     * Gets the numerical value of the card according to its rank.
     *
     * @return The numerical value of the card. Ace returns 11, face cards (King,
     *         Queen, and Jack) return 10,
     *         and all other cards return their rank as an integer value.
     */
    public int getValue() {
        switch (rank) {
            case ACE:
                return 11;
            case KING:
            case QUEEN:
            case JACK:
                return 10;
            default:
                return Integer.parseInt(rank.getRank());
        }
    }

    public String toString() {
        return rank.getRank() + suit.getSuit();
    }

}

/**
 * The Hand class represents a hand of playing cards in Blackjack.
 */
class Hand {
    private Card[] hand;
    private int numCards;

    /**
     * Constructs an empty Hand object.
     */
    public Hand() {
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
            int cardValue = card.getValue();
            if (card.getRank().equals("A")) {
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
     * Parses and handles commands received from the server.
     */
    public void parseCommand() {
        String message;
        boolean done = false;
        while (!done) {
            try {
                message = read();
            } catch (IOException e) {
                System.err.println("ERROR: Unable to read from the server: " + e.getMessage());
                return;
            }
            String[] parts = message.split(":");
            String command = parts[0];

            try {
                switch (command) {
                    case "login":
                        handleLogin(parts);
                        break;

                    case "bet":
                        handleBet(parts);
                        break;

                    case "play":
                        handlePlay(parts);
                        break;

                    case "status":
                        handleStatus(parts);
                        break;

                    case "done":
                        handleDone(parts);
                        done = true;
                        break;

                    default:
                        System.err.println("ERROR: Unable to parse the command: Unknown command: " + command);
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
     * Handles the login process with the provided message parts.
     *
     * @param parts The message parts containing the login information
     * @throws IOException              If an error occurs while writing the login
     *                                  message
     * @throws IllegalArgumentException If the login message format is invalid
     */
    private void handleLogin(String[] parts) throws IOException, IllegalArgumentException {
        if (parts.length < 2) {
            throw new IllegalArgumentException("Invalid login message format");
        }
        write("rfahimi:21Savage");
    }

    /**
     * Handles the bet placing process with the provided message parts.
     *
     * @param parts The message parts containing the bet information
     * @throws IOException              If an error occurs while writing the bet
     *                                  message
     * @throws IllegalArgumentException If the bet message format is invalid
     */
    private void handleBet(String[] parts) throws IOException, IllegalArgumentException {
        if (parts.length < 2) {
            throw new IllegalArgumentException("Invalid bet message format");
        }
        int bankroll;
        try {
            bankroll = Integer.parseInt(parts[1]);
        } catch (NumberFormatException e) {
            System.err.println("ERROR: Unable to initialize bankroll: Bankroll value is not an int: " + parts[1]);
            return;
        }
        write("bet:" + getBetAmount(bankroll));
    }

    /**
     * Handles the play process with the provided message parts.
     *
     * @param parts The message parts containing the play information
     * @throws IOException              If an error occurs while playing
     * @throws IllegalArgumentException If the play message format is invalid
     */
    private void handlePlay(String[] parts) throws IOException, IllegalArgumentException {
        if (parts.length < 3) {
            throw new IllegalArgumentException("Invalid play message format");
        }
        Card dealerUpCard;
        Hand hand = new Hand();
        try {
            dealerUpCard = new Card(parts[2]);
            for (int i = 4; i < parts.length; i++) {
                hand.addCard(new Card(parts[i]));
            }
        } catch (IllegalArgumentException e) {
            System.err.println("ERROR: Unable to initialize card: " + e.getMessage());
            return;
        }
        play(dealerUpCard, hand);
    }

    /**
     * Handles the status display process with the provided message parts.
     *
     * @param parts The message parts containing the status information
     * @throws IllegalArgumentException If the status message format is invalid
     */
    private void handleStatus(String[] parts) throws IllegalArgumentException {
        if (parts.length < 6) {
            throw new IllegalArgumentException("Invalid status message format");
        }
        System.out.println("Result: " + parts[1] + " | Dealer score: " + parts[3] + " | Your score: " + parts[5]);
    }

    /**
     * Handles the completion of the game with the provided message parts.
     *
     * @param parts The message parts containing the completion information
     * @throws IllegalArgumentException If the done message format is invalid
     */
    private void handleDone(String[] parts) throws IllegalArgumentException {
        if (parts.length < 2) {
            throw new IllegalArgumentException("Invalid done message format");
        }
        System.out.println("Game over: " + parts[1]);
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
    public void play(Card dealerUpcard, Hand hand) throws IOException {
        int numCards = hand.getNumCards();
        int handValue = hand.getValue();
        Card[] cards = hand.getHand();
        int dealerUpcardValue = dealerUpcard.getValue();

        // Check for split
        if (numCards == 2 && cards[0].getRank().equals(cards[1].getRank())) {
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
     * The main method that runs the Blackjack client.
     * 
     * @param args Command line arguments containing the IP address and port number
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
