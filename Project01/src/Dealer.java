import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Abstract class representing a card game dealer.
 * Contains the basic functionality to connect to a client, handle
 * communication,
 * and play a game.
 */
public abstract class Dealer {
    protected static final int IP_PORT = 8080;
    protected static final int STARTING_STACK = 500;
    protected static final String WIN = "win";
    protected static final String LOSE = "lose";

    protected int ipPort;
    protected Deck deck;
    protected int stack;
    protected int bet;
    protected int round;
    protected Connection connection;

    /**
     * Constructs a new Dealer instance.
     *
     * @param ipPort the IP port to be used for the connection.
     */
    public Dealer(int ipPort) {
        this.ipPort = ipPort;
        deck = new Deck();
        stack = STARTING_STACK;
        round = 0;
    }

    /**
     * Starts the server and accepts a client connection.
     */
    protected void start() {
        try (ServerSocket serverSocket = new ServerSocket(ipPort)) {
            System.out.println("Dealer Server is running...");

            Socket clientSocket = serverSocket.accept();
            System.out.println("Accepted connection from " + clientSocket.getRemoteSocketAddress());
            handleClient(clientSocket);

        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    /**
     * Handles communication with the client.
     *
     * @param clientSocket the socket representing the client connection.
     */
    private void handleClient(Socket clientSocket) {
        connection = new Connection(clientSocket);

        sendLoginCommand();

        playGame();

        finishGame();
    }

    /**
     * Sends a login command to the client.
     */
    protected void sendLoginCommand() {
        connection.write("login");
        String loginResponse = connection.read();
        System.out.println("Received login response: " + loginResponse);
    }

    /**
     * Plays the game as long as there are chips in the stack.
     */
    private void playGame() {
        while (stack > 0) {
            round++;
            System.out.println("ROUND: " + round);
            playRound();
            // try {
            // Thread.sleep(200); // Pause for 1 second (1000 milliseconds)
            // } catch (InterruptedException e) {
            // // Handle interrupted exception
            // System.err.println("Interrupted exception");
            // }
        }
    }

    /*
     * Sends a done command to the client and closes the connection.
     */
    private void finishGame() {
        connection.write("done:Out of chips");
        System.out.println("Done: Out of chips");
        connection.close();
    }

    /**
     * Abstract method to play a single round of the game.
     * Must be implemented in a subclass.
     */
    protected abstract void playRound();

}
