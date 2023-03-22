import java.net.Socket;
import java.io.IOException;
import java.io.DataInputStream;
import java.io.DataOutputStream;

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
        Object[] parsedCard = parseCard(card);
        if (parsedCard != null) {
            this.rank = (Rank) parsedCard[0];
            this.suit = (Suit) parsedCard[1];
        } else {
            throw new IllegalArgumentException("Invalid card format: " + card);
        }
    }

    /**
     * Parses the card string to extract the rank and suit.
     *
     * @param card The card string in the format "rank + suit"
     * @return An object array containing the rank and suit, or null if the format
     *         is
     *         invalid
     * @throws IllegalArgumentException If the card string is null or empty
     */
    private Object[] parseCard(String card) throws IllegalArgumentException {
        if (card == null || card.isEmpty()) {
            throw new IllegalArgumentException("Null or empty card string");
        }
        for (int i = 1; i <= 2; i++) {
            String rankString = card.substring(0, i).toUpperCase();
            String suitString = card.substring(i).toUpperCase();
            Rank rank = Rank.fromString(rankString);
            Suit suit = Suit.fromString(suitString);
            if (rank != null && suit != null) {
                return new Object[] { rank, suit };
            }
        }
        return null;
    }

    // Getters

    public String getRank() {
        return rank.getRank();
    }

    public int getValue() {
        return rank.getValue();
    }

    public String getSuit() {
        return suit.getSuit();
    }

    @Override
    public String toString() {
        return rank.getRank() + suit.getSuit();
    }

    /**
     * The Rank enumeration defines the 13 possible card ranks in a standard 52-card
     * deck.
     */
    private enum Rank {
        TWO("2", 2), THREE("3", 3), FOUR("4", 4), FIVE("5", 5), SIX("6", 6), SEVEN("7", 7), EIGHT("8", 8), NINE("9", 9),
        TEN("10", 10), JACK("J", 11), QUEEN("Q", 12), KING("K", 13), ACE("A", 14);

        private final String rank;
        private final int value;

        /**
         * Constructor for the Rank enumeration.
         *
         * @param rank  The rank as a string.
         * @param value The rank as an int.
         */
        Rank(String rank, int value) {
            this.rank = rank;
            this.value = value;
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
         * Returns the value of the rank as an int.
         *
         * @return The rank value as an int.
         */
        public int getValue() {
            return value;
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
    private enum Suit {
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
}

/**
 * The Hand class represents a hand of playing cards in Acey.
 */
class Hand {
    private Card[] hand;
    private int numCards;

    /**
     * Constructs an empty Hand object.
     */
    public Hand() {
        hand = new Card[3];
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

    private void handlePlay(String[] parts) throws IOException {
        int bet = 0;
        String decision = "mid";
        write(decision + ":" + bet);
    }

    public void parseCommand() throws IOException {
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
                        write("rfahimi:AceyDoesIt");
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
        if (args.length != 2) {
            System.out.println("Usage: java Acey <ipAddress> <ipPort>");
            return;
        }

        String ipAddress = args[0];
        int ipPort = Integer.parseInt(args[1]);

        try {
            Acey player = new Acey(ipAddress, ipPort);
            player.parseCommand();
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
