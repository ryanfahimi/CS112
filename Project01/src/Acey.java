/*
 * filename: Acey.java
 * Contains classes that represent a AceyDeucey player client that interacts with the dealer server
 * Author: Ryan Fahimi
 */

/**
 * The Acey class represents a simple AceyDeucey player that connects to a
 * server and plays a game of AceyDeucey.
 */
public class Acey {
    private static final String LOGIN = "login";
    private static final String PLAY = "play";
    private static final String STATUS = "status";
    private static final String DONE = "done";
    private static final String HIGH = "high";
    private static final String LOW = "low";
    private static final String MID = "mid";
    private Connection connection;

    public Acey(String ipAddress, int ipPort) {
        connection = new Connection(ipAddress, ipPort);
    }

    /**
     * The main method which initializes the Acey player and starts parsing
     * commands.
     *
     * @param args The command line arguments containing the IP address and port
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

        Acey player = new Acey(ipAddress, ipPort);
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
                    case PLAY:
                        handlePlay(commandParts);
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
        connection.write("rfahimi:AceyDoesIt");
    }

    /**
     * Handles the play and bet placing process with the provided message parts.
     *
     * @param commandParts The message parts containing the play information
     * @throws IllegalArgumentException If the play message format is invalid
     */
    private void handlePlay(String[] commandParts) {
        int dealtCardsInt = 0;
        Card[] dealtCardsArray = new Card[364];
        if (commandParts.length < 6) {
            throw new IllegalArgumentException("Invalid play message format");
        }

        AceyHand hand = new AceyHand();

        // Add cards to the hand
        for (int i = 3; i < 5; i++) {
            Card card = Card.fromString(commandParts[i]);
            hand.addCard(card);
        }
        for (int i = 6; i < commandParts.length; i++) {
            dealtCardsArray[dealtCardsInt] = Card.fromString(commandParts[i]);
            dealtCardsInt++;
        }
        int pot = Integer.parseInt(commandParts[1]);
        int stack = Integer.parseInt(commandParts[2]);
        placeBet(hand, stack, pot, dealtCardsInt, dealtCardsArray);
    }

    /**
     * Implements the player's Acey strategy based on the
     * the player's current hand, bankroll amount, and the current pot.
     * 
     * @param hand            The player's current hand
     * @param stack           The player's bankroll amount
     * @param pot             The current pot amount
     * @param dealtCardsInt   The current number of dealt cards
     * @param dealtCardsArray The current dealt cards
     */
    private void placeBet(AceyHand hand, int stack, int pot, int dealtCardsInt, Card[] dealtCardsArray) {
        int difference = hand.getDifference();
        double confidence;
        int riskFactor = 2;

        String decision;
        if (difference == 0) {
            decision = hand.getValue() > 16 ? LOW : HIGH;
            confidence = 0.3;
            riskFactor = 3;
        } else if (difference <= 4) {
            decision = MID;
            confidence = 0;
        } else {
            decision = MID;
            confidence = 1.0 - (1.0 / difference);
        }

        // Incorporate remaining cards probability
        int remainingCards = 364 - dealtCardsInt;
        int favorableCards = hand.getFavorableCards(decision, dealtCardsInt, dealtCardsArray);
        double probabilityOfWinning = (double) favorableCards / remainingCards;
        confidence *= probabilityOfWinning;
        confidence /= riskFactor;
        int bet = (int) (stack * confidence);
        bet = Math.min(bet, pot); // Ensure the bet is not greater than the pot
        connection.write(decision + ":" + bet);
    }

    /**
     * Handles the status display process with the provided message parts.
     *
     * @param commandParts The message parts containing the status information
     * @throws IllegalArgumentException If the status message format is invalid
     */
    private void handleStatus(String[] commandParts) {
        if (commandParts.length < 5) {
            throw new IllegalArgumentException("Invalid status message format");
        }
        System.out.println("Status: " + commandParts[1]);
    }

    /**
     * Handles the completion of the game with the provided message parts.
     *
     * @param commandParts The message parts containing the completion information
     * @throws IllegalArgumentException If the done message format is invalid
     */
    private void handleDone(String[] commandParts) {
        if (commandParts.length < 2) {
            throw new IllegalArgumentException("Invalid done message format");
        }
        System.out.println("Game result: " + commandParts[1]);
    }

}
