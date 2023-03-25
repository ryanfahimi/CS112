import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class AceyDealer {
    private int port;
    private Deck deck;
    private int stack;
    private int pot;
    private int ante;
    private int round;

    public AceyDealer(int port) {
        this.port = port;
        deck = new Deck();
        stack = 500;
        pot = 0;
        ante = 1;
        round = 1;
    }

    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Acey Dealer Server is running...");

            Socket clientSocket = serverSocket.accept();
            System.out.println("Accepted connection from " + clientSocket.getRemoteSocketAddress());

            Connection connection = new Connection(clientSocket);

            // Send login command
            sendLoginCommand(connection);

            while (stack > 1) {
                // Send play command
                AceyHand hand = new AceyHand();
                hand.addCard(deck.deal());
                hand.addCard(deck.deal());
                hand.addCard(deck.deal());
                Card firstCard = hand.getFirstCard();
                Card secondCard = hand.getSecondCard();
                Card thirdCard = hand.getThirdCard();

                String playCommand = "play:" + pot + ":" + stack + ":" + firstCard + ":"
                        + secondCard;
                connection.write(playCommand);

                // Parse player response
                String response = connection.read();
                System.out.println("Received play response: " + response);
                String[] responseParts = response.split(":");
                String decision = responseParts[0];
                int bet = Integer.parseInt(responseParts[1]);

                // Validate player response
                if (!(decision.equals("high") || decision.equals("low") || decision.equals("mid"))) {
                    System.err.println("Invalid decision from player: " + decision);
                    break;
                }
                if (bet > stack || bet > pot) {
                    System.err.println("Cheating detected: Player attempted to bet more than allowed.");
                    connection.write("done:Cheating");
                    break;
                }

                // Increment pot and print status
                pot += bet;
                stack -= bet;
                System.out.println("Round " + round + " - Player bet " + bet + " chips. Pot is now " + pot
                        + ". Stack is now " + stack);

                // Check if player wins or loses

                String statusCommand;
                if (playerWins(decision, hand)) {
                    // Player wins, send win status command
                    stack += pot;
                    pot = 0;
                    statusCommand = "status:win:" + firstCard + ":" + secondCard + ":"
                            + thirdCard;
                } else {
                    // Player loses, send lose status command
                    statusCommand = "status:lose:" + firstCard + ":" + secondCard + ":"
                            + thirdCard;
                }
                System.out.println(statusCommand);
                connection.write(statusCommand);

                round++;

                // Take ante
                if (pot == 0) {
                    stack -= ante;
                    pot += ante;
                }
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

    private boolean playerWins(String decision, AceyHand hand) {
        if (decision.equals("high")) {
            return hand.isHigh();
        } else if (decision.equals("low")) {
            return hand.isLow();
        } else {
            return hand.isMid();
        }
    }

    public static void main(String[] args) {
        int dealerPort = 8080;
        AceyDealer dealer = new AceyDealer(dealerPort);
        dealer.start();
    }
}