import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public abstract class Dealer {
    protected static final int IP_PORT = 8080;
    protected static final int STARTING_STACK = 50;

    protected int ipPort;
    protected Deck deck;
    protected int stack;
    protected int round;

    public Dealer(int ipPort) {
        this.ipPort = ipPort;
        deck = new Deck();
        stack = STARTING_STACK;
        round = 1;
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

    protected void handleClient(Socket clientSocket) {
        Connection connection = new Connection(clientSocket);

        // Send login command
        sendLoginCommand(connection);

        while (stack > 0) {
            playRound(connection);
        }

        // Finish game
        connection.write("done:Out of chips");
        connection.close();
    }

    protected abstract void playRound(Connection connection);

    protected void sendLoginCommand(Connection connection) {
        connection.write("login");
        String loginResponse = connection.read();
        System.out.println("Received login response: " + loginResponse);
    }
}
