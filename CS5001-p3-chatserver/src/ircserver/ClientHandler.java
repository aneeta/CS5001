import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import exceptions.DisconnectedException;

public class ClientHandler extends Thread {

    private Socket client;
    private InputStream inStream;
    private OutputStream outStream;
    BufferedReader bufferedReader;

    public ClientHandler(Socket client) {
        this.client = client;
        try {
            // TODO

        } catch (IOException ioe) {
            System.out.println(
                "Exception in ClientHandeler: " + ioe.getMessage()
            );
        }
    }

    public void run() {
        try {
            // TODO

        } catch (Exception err) {
            System.out.println(
                "Exception in ClientHandeler:run : " + err.getMessage()
            );
            cleanup();
        }
    }

    private void handleCommand() {
        while (true) {
            String line = bufferedReader.readLine(); // get data from client over socket

            // if readLine fails we can deduce here that the connection to the client is broken
            // and shut down the connection on this side cleanly by throwing a DisconnectedException
            // which will be passed up the call stack to the nearest handler (catch block)
            // in the run method
            if (line == null || line.equals("null") || line.equals(Configuration.exitString)) {
                throw new DisconnectedException(" ... client has closed the connection ... ");
            }

            // in this simple setup all the server does in response to messages from the client is
            // to send a single ACK byte back to client - the client uses this ACK byte to test
            // whether the connection to this server is still live, if not the client shuts down cleanly
            os.write(Configuration.ackByte);

            // assuming no exception, print out line received from client
            System.out.println("ConnectionHandler: " + line);
        }
    }

    private void cleanup() {
        System.out.println("ClientHandeler: ... cleaning up and exiting ... ");
        try {
            bufferedReader.close();
            inStream.close();
            client.close();
        } catch (IOException ioe) {
            System.out.println("ClientHandeler:cleanup " + ioe.getMessage());
        }
    }



}
