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
        value += card.rank.toInt();
        numCards++;
    }

    /**
     * Calculates and returns the difference between the first two cards in the
     * hand.
     *
     * @return The difference between the first two cards in the hand
     */
    public int getDifference() {
        difference = Math.abs(hand[1].rank.toInt() - hand[0].rank.toInt());
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

/**
 * The Acey class represents a simple AceyDeucey player that connects to a
 * server and plays a game of AceyDeucey.
 */
public class Acey {
    private Connection connection;

    public Acey(String ipAddress, int ipPort) {
        connection = new Connection(ipAddress, ipPort);
    }

    /**
     * Implements the player's Acey strategy based on the
     * the player's current hand, bankroll amount, and the current pot.
     * 
     * @param hand  The player's current hand
     * @param stack The player's bankroll amount
     * @param pot   The current pot amount
     */
    private void play(AceyHand hand, int stack, int pot) {
        int difference = hand.getDifference();
        double confidence;

        String decision;
        if (difference == 0) {
            decision = hand.getValue() > 16 ? "low" : "high";
            confidence = 0.3;
        } else if (difference <= 2) {
            decision = "mid";
            confidence = 0;
        } else {
            decision = "mid";
            confidence = 1.0 - (1.0 / difference);
            confidence = Math.min(confidence, 0.7); // Limit the maximum confidence to 70% to avoid losing all chips
        }

        int bet = (int) (stack * confidence);
        bet = Math.min(bet, pot); // Ensure the bet is not greater than the pot
        connection.write(decision + ":" + bet);
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
        connection.write("rfahimi:AceyDoesIt");
    }

    /**
     * Handles the play and bet placing process with the provided message parts.
     *
     * @param commandParts The message parts containing the play information
     * @throws IllegalArgumentException If the play message format is invalid
     */
    private void handlePlay(String[] commandParts) {
        if (commandParts.length < 6) {
            throw new IllegalArgumentException("Invalid play message format");
        }

        AceyHand hand = new AceyHand();

        // Add cards to the hand
        for (int i = 3; i < commandParts.length; i++) {
            Card card = Card.fromString(commandParts[i]);
            hand.addCard(card);
        }
        int pot = Integer.parseInt(commandParts[1]);
        int stack = Integer.parseInt(commandParts[2]);
        play(hand, stack, pot);
    }

    /**
     * Handles the status display process with the provided message parts.
     *
     * @param commandParts The message parts containing the status information
     * @throws IllegalArgumentException If the status message format is invalid
     */
    private void handleStatus(String[] commandParts) {
        if (commandParts.length < 2) {
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
            } catch (IllegalArgumentException e) {
                System.err.println("ERROR: Unable to parse the command: " + e.getMessage());
                return;
            }
        }
        connection.close();
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
}
