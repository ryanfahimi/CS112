import java.net.Socket;
import java.io.IOException;
import java.io.DataInputStream;
import java.io.DataOutputStream;

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

public class Acey {
    private Socket socket;
    private DataInputStream dis;
    private DataOutputStream dos;

    public Acey(String ipAddress, int ipPort) throws IOException {
        socket = new Socket(ipAddress, ipPort);
        dis = new DataInputStream(socket.getInputStream());
        dos = new DataOutputStream(socket.getOutputStream());
    }

    private void write(String s) throws IOException {
        dos.writeUTF(s);
        dos.flush();
    }

    private String read() throws IOException {
        return dis.readUTF();
    }

    public void play() throws IOException {
        String message;
        while (true) {
            message = read();
            String[] parts = message.split(":");
            String command = parts[0];

            switch (command) {
                case "login":
                    write("rfahimi:<<your avatar name>>");
                    break;
                case "play":
                    // Implement your strategy here
                    int bet = 0;
                    String decision = "mid";
                    write(decision + ":" + bet);
                    break;
                case "status":
                    // Process the status message
                    break;
                case "done":
                    // Process the done message and exit the game
                    socket.close();
                    return;
                default:
                    System.out.println("Unknown command: " + command);
            }
        }
    }

    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Usage: java Acey <ipAddress> <ipPort>");
            return;
        }

        String ipAddress = args[0];
        int ipPort = Integer.parseInt(args[1]);

        try {
            Acey player = new Acey(ipAddress, ipPort);
            player.play();
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
