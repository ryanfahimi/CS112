import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public abstract class Dealer {
    protected static final int IP_PORT = 8080;
    protected static final int STARTING_STACK = 500;

    protected int ipPort;
    protected Deck deck;
    protected int stack;
    protected int bet;
    protected int round;
    protected Connection connection;

    public Dealer(int ipPort) {
        this.ipPort = ipPort;
        deck = new Deck();
        stack = STARTING_STACK;
        round = 0;
    }

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

    private void handleClient(Socket clientSocket) {
        connection = new Connection(clientSocket);

        // Send login command
        sendLoginCommand();

        playGame();

        // Finish game
        connection.write("done:Out of chips");
        System.out.println("Done: Out of chips");
        connection.close();
    }

    protected void sendLoginCommand() {
        connection.write("login");
        String loginResponse = connection.read();
        System.out.println("Received login response: " + loginResponse);
    }

    private void playGame() {
        while (stack > 0) {
            round++;
            System.out.println("ROUND: " + round);
            playRound();
        }
    }

    protected abstract void playRound();

}
