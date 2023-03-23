import java.net.Socket;
import java.io.IOException;
import java.io.DataInputStream;
import java.io.DataOutputStream;

class Connection {
    private Socket socket;
    private DataInputStream dis;
    private DataOutputStream dos;

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

    public void write(String s) {
        try {
            dos.writeUTF(s);
            dos.flush();
        } catch (IOException e) {
            System.err.println("ERROR: Unable to write to the server: " + e.getMessage());
            System.exit(1);
        }
    }

    public String read() {
        try {
            return dis.readUTF();
        } catch (IOException e) {
            System.err.println("ERROR: Unable to read from the server: " + e.getMessage());
            System.exit(1);
        }
        return null;
    }

    public void close() {
        try {
            socket.close();
        } catch (IOException e) {
            System.err.println("ERROR: Unable to close the socket: " + e.getMessage());
            System.exit(1);
        }
    }
}
