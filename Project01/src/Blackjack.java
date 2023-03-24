/*
 * filename: Blackjack.java
 * Contains classes that represent a blackjack player client that interacts with the dealer server
 * Author: Ryan Fahimi
 */

/**
 * The BlackjackHand class represents a hand of playing cards in Blackjack.
 */
class BlackjackHand {
    private Card[] hand;
    private int numCards;

    /**
     * Constructs an empty BlackjackHand object.
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
            int cardValue = card.rank.toInt();
            if (card.rank.toString().equals("A")) {
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
    private Connection connection;

    /**
     * Constructs a Blackjack object and connects to the server at the specified IP
     * address and port.
     * 
     * @param ipAddress The IP address of the server
     * @param ipPort    The port number of the server
     */

    public Blackjack(String ipAddress, int ipPort) {
        connection = new Connection(ipAddress, ipPort);
    }

    /**
     * Determines the bet amount based on the player's bankroll.
     * 
     * @param bankroll The player's current bankroll
     * @return The bet amount
     */
    public int getBet(int bankroll) {
        // In this example, we bet 5% of the bankroll each round
        return (int) Math.max(bankroll * 0.05, 1);
    }

    /**
     * Implements the player's Blackjack strategy based on the dealer's upcard and
     * the player's current hand.
     * 
     * @param dealerUpcard The dealer's upcard
     * @param hand         The player's current hand
     */
    public void play(Card dealerUpcard, AceyHand hand) {
        int numCards = hand.getNumCards();
        int handValue = hand.getValue();
        Card[] cards = hand.getHand();
        int dealerUpcardValue = dealerUpcard.rank.toInt();

        // Check for split
        if (numCards == 2 && cards[0].rank.toString().equals(cards[1].rank.toString())) {
            connection.write("split");
        } // Check for double down
        else if (handValue >= 9 && handValue <= 11 && numCards == 2) {
            connection.write("double");
        } // Implement hit or stand strategy based on dealer's upcard
        else if (handValue < 17 && (dealerUpcardValue >= 7 || dealerUpcardValue == 11)) {
            connection.write("hit");
        } else {
            connection.write("stand");
        }
    }

    /**
     * Handles the login process with the provided message parts.
     *
     * @param commandParts The message parts containing the login information
     * @throws IllegalArgumentException If the login message format is invalid
     */
    private void handleLogin(String[] commandParts) throws IllegalArgumentException {
        if (commandParts.length < 2) {
            throw new IllegalArgumentException("Invalid login message format");
        }
        connection.write("rfahimi:21Savage");
    }

    /**
     * Handles the bet placing process with the provided message parts.
     *
     * @param commandParts The message parts containing the bet information
     * @throws IllegalArgumentException If the bet message format is invalid
     */
    private void handleBet(String[] commandParts) throws IllegalArgumentException {
        if (commandParts.length < 2) {
            throw new IllegalArgumentException("Invalid bet message format");
        }
        int bankroll;
        try {
            bankroll = Integer.parseInt(commandParts[1]);
            connection.write("bet:" + getBet(bankroll));
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
     * @throws IllegalArgumentException If the play message format is invalid
     */
    private void handlePlay(String[] commandParts) throws IllegalArgumentException {
        if (commandParts.length < 3) {
            throw new IllegalArgumentException("Invalid play message format");
        }
        Card dealerUpCard;
        AceyHand hand = new AceyHand();
        try {
            dealerUpCard = Card.fromString(commandParts[2]);
            for (int i = 4; i < commandParts.length; i++) {
                hand.addCard(Card.fromString(commandParts[i]));
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
        System.out.println("Game result: " + commandParts[1]);
    }

    /**
     * Parses commands received from the server and calls the corresponding handler
     * methods.
     */
    public void parseCommand() {
        String command;
        boolean done = false;
        while (!done) {
            command = connection.read();
            String[] commandParts = command.split(":");
            command = commandParts[0];

            try {
                switch (command) {
                    case "login":
                        handleLogin(commandParts);
                        break;

                    case "bet":
                        // handleBet(commandParts);
                        connection.write("bet:1");
                        break;

                    case "play":
                        // handlePlay(commandParts);
                        connection.write("stand");
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
            } catch (IllegalArgumentException e) {
                System.err.println("ERROR: Unable to parse the command: " + e.getMessage());
                return;
            }
        }
        connection.close();
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
