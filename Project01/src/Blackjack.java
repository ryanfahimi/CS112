/*
 * filename: Blackjack.java
 * Contains classes that represent a blackjack player client that interacts with the dealer server
 * Author: Ryan Fahimi
 */

/**
 * The Blackjack class represents a simple Blackjack player that connects to a
 * server and plays a game of Blackjack.
 */
public class Blackjack {
    private static final int NUM_DECKS = 7;

    private Connection connection;
    private int count;
    private int dealtCards;
    private int bet;
    private int stack;

    /**
     * Constructs a Blackjack object and connects to the server at the specified IP
     * address and port.
     * 
     * @param ipAddress The IP address of the server
     * @param ipPort    The port number of the server
     */

    public Blackjack(String ipAddress, int ipPort) {
        connection = new Connection(ipAddress, ipPort);
        dealtCards = 0;
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
     * Handles the login process with the provided message parts.
     *
     * @param commandParts The message parts containing the login information
     * @throws IllegalArgumentException If the login message format is invalid
     */
    private void handleLogin(String[] commandParts) throws IllegalArgumentException {
        if (commandParts.length < 1) {
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
        try {
            stack = Integer.parseInt(commandParts[1]);
            updateCount(commandParts);
            connection.write("bet:" + getBet());
        } catch (NumberFormatException e) {
            System.err
                    .println("ERROR: Unable to initialize bankroll: Bankroll value is not an int: " + commandParts[1]);
            System.exit(1);
        }
    }

    private void updateCount(String[] commandParts) {
        count = 0;
        for (int i = 3; i < commandParts.length; i++) {
            Card card = Card.fromString(commandParts[i]);
            int cardValue = card.rank.toInt();
            if (cardValue >= 2 && cardValue <= 6) {
                count++;
            } else if (cardValue == 10 || cardValue == 1) {
                count--;
            }
            dealtCards++;
        }
    }

    /**
     * Determines the bet amount based on the player's bankroll and the Kelly
     * Criterion.
     *
     * @param stack The player's current bankroll
     * @return The bet amount
     */
    public int getBet() {
        double advantage = getAdjustedAdvantage(); // Assuming a 2% advantage

        // Calculate the bet amount based on the Kelly Criterion
        double betAmount = stack * advantage;
        // Ensure the bet is at least 1 and is an integer
        bet = (int) Math.max(Math.round(betAmount), 1);
        return bet;
    }

    public double getAdjustedAdvantage() {
        int trueCount = getTrueCount();
        // Adjust the player's advantage based on the true count
        // This is a simplified example; you may use a different formula
        return 0.05 + 0.01 * trueCount;
    }

    public int getTrueCount() {
        double remainingDecks = (double) (NUM_DECKS * 52 - dealtCards) / 52;
        return (int) Math.round(count / remainingDecks);
    }

    /**
     * Handles the play process with the provided message parts.
     *
     * @param commandParts The message parts containing the play information
     * @throws IllegalArgumentException If the play message format is invalid
     */
    private void handlePlay(String[] commandParts) throws IllegalArgumentException {
        if (commandParts.length < 5) {
            throw new IllegalArgumentException("Invalid play message format");
        }
        Card dealerUpCard;
        BlackjackHand hand = new BlackjackHand();
        try {
            dealerUpCard = Card.fromString(commandParts[2]);
            for (int i = 4; i < commandParts.length; i++) {
                Card card = Card.fromString(commandParts[i]);
                hand.addCard(card);
            }
            play(dealerUpCard, hand);
        } catch (IllegalArgumentException e) {
            System.err.println("ERROR: Unable to initialize card: " + e.getMessage());
            System.exit(1);
        }
    }

    /**
     * Implements the player's Blackjack strategy based on the dealer's upcard and
     * the player's current hand.
     * 
     * @param dealerUpcard The dealer's upcard
     * @param hand         The player's current hand
     */
    public void play(Card dealerUpcard, BlackjackHand hand) {
        int handValue = hand.getValue();
        int dealerUpcardValue = dealerUpcard.rank.toInt();
        boolean isSoft = hand.isSoft();

        // Check for split
        if (shouldSplit(hand, dealerUpcardValue)) {
            connection.write("split");
            return;
        }

        // Check for double down
        if (shouldDouble(hand, handValue, dealerUpcardValue)) {
            connection.write("double");
            return;
        }

        // Implement hit or stand strategy based on dealer's upcard and softness of the
        // hand
        if (shouldHit(isSoft, handValue, dealerUpcardValue)) {
            connection.write("hit");
        } else {
            connection.write("stand");
        }
    }

    private boolean shouldSplit(BlackjackHand hand, int dealerUpcardValue) {
        int handValue = hand.getValue();

        if (!hand.isSplittable() || (stack >= bet * 2)) {
            return false;
        }

        if (handValue == 16 || hand.isSoft()) { // Split 8s and Aces
            return true;
        } else if ((handValue >= 4 && handValue <= 14) && (dealerUpcardValue <= 7 && dealerUpcardValue != 1)) {
            return true;
        }

        return false;
    }

    private boolean shouldDouble(BlackjackHand hand, int handValue, int dealerUpcardValue) {
        int numCards = hand.getNumCards();

        if ((handValue >= 9 && handValue <= 11) && numCards == 2 && (dealerUpcardValue >= 3 && dealerUpcardValue <= 6)
                && (stack >= bet * 2)) {
            return true;
        }

        return false;
    }

    private boolean shouldHit(boolean isSoft, int handValue, int dealerUpcardValue) {
        if (isSoft) {
            return handValue <= 17 || (handValue == 18
                    && (dealerUpcardValue == 9 || dealerUpcardValue == 10 || dealerUpcardValue == 1));
        } else {
            return handValue < 12
                    || (handValue >= 12 && handValue <= 16 && (dealerUpcardValue >= 7 || dealerUpcardValue == 1));
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
        System.out.println("Result: " + commandParts[1] + ", Dealer score: " + commandParts[3] + ", Your score: "
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

}
