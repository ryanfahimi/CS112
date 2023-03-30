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
    private static final String LOGIN = "login";
    private static final String BET = "bet";
    private static final String PLAY = "play";
    private static final String STATUS = "status";
    private static final String DONE = "done";
    private static final String HIT = "hit";
    private static final String STAND = "stand";
    private static final String DOUBLE = "double";
    private static final String SPLIT = "split";
    private static final int NUM_DECKS = 7;
    private static final int[] APC_CARD_VALUES = { 1, 2, 2, 3, 2, 1, 0, -1, -2, -2, -2, -2, -1 };
    private static final String[][] HARD_STRATEGY_CHART = {
            // 2 3 4 5 6 7 8 9 10 A
            { HIT, HIT, HIT, HIT, HIT, HIT, HIT, HIT, HIT, HIT }, // 5
            { HIT, HIT, HIT, HIT, HIT, HIT, HIT, HIT, HIT, HIT }, // 6
            { HIT, HIT, HIT, HIT, HIT, HIT, HIT, HIT, HIT, HIT }, // 7
            { HIT, HIT, HIT, DOUBLE, DOUBLE, HIT, HIT, HIT, HIT, HIT }, // 8
            { DOUBLE, DOUBLE, DOUBLE, DOUBLE, DOUBLE, HIT, HIT, HIT, HIT, HIT }, // 9
            { DOUBLE, DOUBLE, DOUBLE, DOUBLE, DOUBLE, DOUBLE, DOUBLE, DOUBLE, HIT, HIT }, // 10
            { DOUBLE, DOUBLE, DOUBLE, DOUBLE, DOUBLE, DOUBLE, DOUBLE, DOUBLE, DOUBLE, DOUBLE }, // 11
            { HIT, HIT, STAND, STAND, STAND, HIT, HIT, HIT, HIT, HIT }, // 12
            { STAND, STAND, STAND, STAND, STAND, HIT, HIT, HIT, HIT, HIT }, // 13
            { STAND, STAND, STAND, STAND, STAND, HIT, HIT, HIT, HIT, HIT }, // 14
            { STAND, STAND, STAND, STAND, STAND, HIT, HIT, HIT, HIT, HIT }, // 15
            { STAND, STAND, STAND, STAND, STAND, HIT, HIT, HIT, HIT, HIT }, // 16
            { STAND, STAND, STAND, STAND, STAND, STAND, STAND, STAND, STAND, STAND }, // 17
            { STAND, STAND, STAND, STAND, STAND, STAND, STAND, STAND, STAND, STAND }, // 18
            { STAND, STAND, STAND, STAND, STAND, STAND, STAND, STAND, STAND, STAND }, // 19
            { STAND, STAND, STAND, STAND, STAND, STAND, STAND, STAND, STAND, STAND }, // 20
            { STAND, STAND, STAND, STAND, STAND, STAND, STAND, STAND, STAND, STAND }, // 21
    };

    private static final String[][] SOFT_STRATEGY_CHART = {
            // 2 3 4 5 6 7 8 9 10 A
            { HIT, HIT, HIT, DOUBLE, DOUBLE, HIT, HIT, HIT, HIT, HIT }, // A2 (13)
            { HIT, HIT, HIT, DOUBLE, DOUBLE, HIT, HIT, HIT, HIT, HIT }, // A3 (14)
            { HIT, HIT, DOUBLE, DOUBLE, DOUBLE, HIT, HIT, HIT, HIT, HIT }, // A4 (15)
            { HIT, HIT, DOUBLE, DOUBLE, DOUBLE, HIT, HIT, HIT, HIT, HIT }, // A5 (16)
            { HIT, DOUBLE, DOUBLE, DOUBLE, DOUBLE, HIT, HIT, HIT, HIT, HIT }, // A6 (17)
            { STAND, DOUBLE, DOUBLE, DOUBLE, DOUBLE, STAND, STAND, HIT, HIT, HIT }, // A7 (18)
            { STAND, STAND, STAND, STAND, STAND, STAND, STAND, STAND, STAND, STAND }, // A8 (19)
            { STAND, STAND, STAND, STAND, STAND, STAND, STAND, STAND, STAND, STAND }, // A9 (20)
            { STAND, STAND, STAND, STAND, STAND, STAND, STAND, STAND, STAND, STAND }, // A10 (21)
    };

    private static final String[][] PAIR_STRATEGY_CHART = {
            // 2 3 4 5 6 7 8 9 10 A
            { SPLIT, SPLIT, SPLIT, SPLIT, SPLIT, SPLIT, HIT, HIT, HIT, HIT }, // 2, 2
            { SPLIT, SPLIT, SPLIT, SPLIT, SPLIT, SPLIT, HIT, HIT, HIT, HIT }, // 3, 3
            { HIT, HIT, HIT, SPLIT, SPLIT, HIT, HIT, HIT, HIT, HIT }, // 4, 4
            { DOUBLE, DOUBLE, DOUBLE, DOUBLE, DOUBLE, DOUBLE, DOUBLE, DOUBLE, HIT, HIT }, // 5, 5
            { SPLIT, SPLIT, SPLIT, SPLIT, SPLIT, HIT, HIT, HIT, HIT, HIT }, // 6, 6
            { SPLIT, SPLIT, SPLIT, SPLIT, SPLIT, SPLIT, HIT, HIT, HIT, HIT }, // 7, 7
            { SPLIT, SPLIT, SPLIT, SPLIT, SPLIT, SPLIT, SPLIT, SPLIT, SPLIT, SPLIT }, // 8, 8
            { SPLIT, SPLIT, SPLIT, SPLIT, SPLIT, STAND, SPLIT, SPLIT, STAND, STAND }, // 9, 9
            { STAND, STAND, STAND, STAND, STAND, STAND, STAND, STAND, STAND, STAND }, // 10, 10
    };

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
                    case LOGIN:
                        handleLogin(commandParts);
                        break;

                    case BET:
                        handleBet(commandParts);
                        // connection.write("bet:1");
                        break;

                    case PLAY:
                        handlePlay(commandParts);
                        // connection.write("stand");
                        break;

                    case STATUS:
                        handleStatus(commandParts);
                        break;

                    case DONE:
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

    /**
     * Updates the card count based on the cards received from the server.
     *
     * @param commandParts The message parts containing the card information
     */
    private void updateCount(String[] commandParts) {
        count = 0;
        for (int i = 3; i < commandParts.length; i++) {
            Card card = Card.fromString(commandParts[i]);
            int cardValue = card.RANK.toInt();
            count += APC_CARD_VALUES[cardValue - 1];
            dealtCards++;
        }
    }

    /**
     * Determines the bet amount based on the player's bankroll and the Kelly
     * Criterion.
     *
     * @return The bet amount
     */
    public int getBet() {
        int baseBet = 1; // Base betting unit
        int trueCount = getTrueCount();

        if (trueCount > 1) {
            double advantage = 0.05 + trueCount * 0.01;
            bet = (int) Math.round(advantage * stack);
            bet = Math.max(bet, baseBet);
        } else {
            bet = baseBet;
        }
        // Ensure the bet is at least 1 and is an integer
        return bet;
    }

    /**
     * Calculates the true count based on the card count and the remaining decks.
     *
     * @return The true count
     */
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
            String decision = basicStrategy(dealerUpCard, hand);
            connection.write(decision);
        } catch (IllegalArgumentException e) {
            System.err.println("ERROR: Unable to initialize card: " + e.getMessage());
            System.exit(1);
        }
    }

    /**
     * Implements basic strategy for playing blackjack based on the dealer's up card
     * and the player's hand.
     *
     * @param dealerUpCard The dealer's up card
     * @param hand         The player's hand
     * @return The action to take (HIT, STAND, DOUBLE_DOWN, or SPLIT)
     */
    private String basicStrategy(Card dealerUpCard, BlackjackHand hand) {
        int dealerUpCardValue = dealerUpCard.RANK.toInt();
        int handValue = hand.getValue();
        boolean canDouble = bet * 2 < stack;
        boolean canSplit = hand.isPair() && bet * 2 < stack;

        String decision = getDecision(handValue, dealerUpCardValue, hand.isSoft(), canSplit);

        // Adjust the decision based on whether the player can double or split
        if (decision.equals(DOUBLE) && !canDouble) {
            decision = HIT;
        }
        return decision;
    }

    private static String getDecision(int handValue, int dealerUpCardValue, boolean isSoft, boolean canSplit) {
        String[][] strategyChart;
        int rowIndex;
        int columnIndex;

        if (isSoft) {
            if (canSplit) {
                return SPLIT;
            } else {
                rowIndex = handValue - 13;
                strategyChart = SOFT_STRATEGY_CHART;
            }
        } else if (canSplit) {
            rowIndex = handValue / 2 - 2;
            strategyChart = PAIR_STRATEGY_CHART;
        } else {
            rowIndex = handValue - 5;
            strategyChart = HARD_STRATEGY_CHART;
        }

        columnIndex = dealerUpCardValue - 2;

        return strategyChart[rowIndex][columnIndex];
    }

    /**
     * Handles the status display process with the provided message parts.
     *
     * @param commandParts The message parts containing the status information
     * @throws IllegalArgumentException If the status message format is invalid
     */
    private void handleStatus(String[] commandParts) throws IllegalArgumentException {
        if (commandParts.length < 4) {
            throw new IllegalArgumentException("Invalid status message format");
        }
        if (commandParts.length == 4) {
            System.out.println("Result: " + commandParts[1] + ", Your score: " + commandParts[3]);
        }

        if (commandParts.length == 6) {
            System.out.println("Result: " + commandParts[1] + ", Dealer score: " + commandParts[3] + ", Your score: "
                    + commandParts[5]);
        }
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
