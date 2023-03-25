import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class BlackjackDealer {
    private static final int DEFAULT_PORT = 8080;
    private static final int STARTING_STACK = 50;

    private int port;
    private Deck deck;
    private int stack;
    private int round;
    private int bet;

    public BlackjackDealer(int port) {
        this.port = port;
        deck = new Deck();
        stack = STARTING_STACK;
        round = 1;
    }

    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Blackjack Dealer Server is running...");

            Socket clientSocket = serverSocket.accept();
            System.out.println("Accepted connection from " + clientSocket.getRemoteSocketAddress());

            Connection connection = new Connection(clientSocket);

            // Send login command
            sendLoginCommand(connection);

            while (stack > 0) {
                playRound(connection);
            }

            // Finish game
            connection.write("done:Out of chips");
            connection.close();

        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    private void sendLoginCommand(Connection connection) {
        connection.write("login");
        String loginResponse = connection.read();
        System.out.println("Received login response: " + loginResponse);
    }

    private void playRound(Connection connection) {
        // Send bet command
        connection.write("bet:" + stack);
        String betResponse = connection.read();
        System.out.println("Received bet response: " + betResponse);
        String[] betResponseParts = betResponse.split(":");
        bet = Integer.parseInt(betResponseParts[1]);

        // Deal cards
        BlackjackHand playerHand = new BlackjackHand();
        playerHand.addCard(deck.deal());
        playerHand.addCard(deck.deal());

        BlackjackHand dealerHand = new BlackjackHand();
        dealerHand.addCard(deck.deal());
        dealerHand.addCard(deck.deal());

        // Play the round
        boolean roundOver = false;
        while (!roundOver) {
            roundOver = handlePlayerTurn(connection, playerHand, dealerHand, roundOver);
            roundOver = handleDealerTurn(dealerHand, roundOver);
        }

        // Determine the winner and update the stack
        String roundResult = determineRoundResult(playerHand, dealerHand, bet);

        // Send round result to the player
        connection
                .write("status:" + roundResult + ":dealer:" + dealerHand.getValue() + ":you:" + playerHand.getValue());

        // Print the round result
        System.out.println("Round " + round + " - Result: " + roundResult.toUpperCase() + ", Player bet " + bet
                + " chips. Stack is now " + stack);
        round++;
    }

    private boolean handlePlayerTurn(Connection connection, BlackjackHand playerHand, BlackjackHand dealerHand,
            boolean roundOver) {
        Card dealerUpCard = dealerHand.getHand()[dealerHand.getNumCards() - 1];

        String playCommand = "play:dealer:" + dealerUpCard + ":you:" + playerHand;
        connection.write(playCommand);
        // Parse player response
        String response = connection.read();
        System.out.println("Received play response: " + response);

        // Handle player response and update hands accordingly
        switch (response) {
            case "hit":
                playerHand.addCard(deck.deal());
                break;
            case "stand":
                roundOver = true;
                break;
            case "double":
                bet *= 2;
                playerHand.addCard(deck.deal());
                roundOver = true;
                break;
            case "split":
                // Implement split logic if desired
                break;
            default:
                System.err.println("Invalid response from player: " + response);
                break;
        }

        // Check for player bust
        if (playerHand.isBust()) {
            roundOver = true;
        }

        return roundOver;
    }

    private boolean handleDealerTurn(BlackjackHand dealerHand, boolean roundOver) {
        // Play dealer's hand according to the game rules
        if (dealerHand.getValue() < 17) {
            dealerHand.addCard(deck.deal());
            roundOver = false;
        }
        // Check for player bust
        if (dealerHand.isBust()) {
            roundOver = true;
        }

        return roundOver;
    }

    private String determineRoundResult(BlackjackHand playerHand, BlackjackHand dealerHand, int bet) {
        String roundResult;
        if (playerHand.isBust() || (!dealerHand.isBust() && dealerHand.getValue() > playerHand.getValue())) {
            roundResult = "lose";
            stack -= bet;
        } else if (dealerHand.isBust() || playerHand.getValue() > dealerHand.getValue()) {
            roundResult = "win";
            stack += bet;
        } else {
            roundResult = "push";
        }

        return roundResult;
    }

    public static void main(String[] args) {
        int dealerPort = DEFAULT_PORT;
        BlackjackDealer dealer = new BlackjackDealer(dealerPort);
        dealer.start();
    }
}
