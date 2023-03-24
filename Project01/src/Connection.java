import java.net.Socket;
import java.io.IOException;
import java.io.DataInputStream;
import java.io.DataOutputStream;

/**
 * Represents a connection to a server using a socket.
 * Handles sending and receiving messages from the server.
 */
class Connection {
    private Socket socket;
    private DataInputStream dis;
    private DataOutputStream dos;

    /**
     * Constructs a new connection to a server at the specified IP address and port.
     *
     * @param ipAddress The IP address of the server
     * @param ipPort    The port number of the server
     */
    public Connection(String ipAddress, int ipPort) {
        try {
            socket = new Socket(ipAddress, ipPort);
            dis = new DataInputStream(socket.getInputStream());
            dos = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            System.err.println("ERROR: Unable to connect to the server: " + e.getMessage());
            System.exit(1);
        }
    }

    public Connection(Socket socket) {
        try {
            this.socket = socket;
            dis = new DataInputStream(socket.getInputStream());
            dos = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            System.err.println("ERROR: Unable to connect to the server: " + e.getMessage());
            System.exit(1);
        }
    }

    /**
     * Sends a message to the server.
     *
     * @param s The message to send
     */
    public void write(String s) {
        try {
            dos.writeUTF(s);
            dos.flush();
        } catch (IOException e) {
            System.err.println("ERROR: Unable to write to the server: " + e.getMessage());
            System.exit(1);
        }
    }

    /**
     * Reads a message from the server.
     *
     * @return The message received from the server
     */
    public String read() {
        try {
            return dis.readUTF();
        } catch (IOException e) {
            System.err.println("ERROR: Unable to read from the server: " + e.getMessage());
            System.exit(1);
        }
        return null;
    }

    /**
     * Closes the connection to the server.
     */
    public void close() {
        try {
            socket.close();
        } catch (IOException e) {
            System.err.println("ERROR: Unable to close the socket: " + e.getMessage());
            System.exit(1);
        }
    }
}
