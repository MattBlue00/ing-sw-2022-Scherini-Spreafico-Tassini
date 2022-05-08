package it.polimi.ingsw.network.client;

import it.polimi.ingsw.network.message.Message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SocketClient extends Client{


    private final Socket socket;

    private final ObjectOutputStream out;
    private final ObjectInputStream in;

    private final ExecutorService readExecutionQueue;

    private static final int SOCKET_TIMEOUT = 10000;

    public SocketClient(String address, int port) throws IOException {
        this.socket = new Socket();
        this.socket.connect(new InetSocketAddress(address, port), SOCKET_TIMEOUT);
        this.out = new ObjectOutputStream(socket.getOutputStream());
        this.in = new ObjectInputStream(socket.getInputStream());
        this.readExecutionQueue = Executors.newSingleThreadExecutor();
    }


    /*
        The method sends the message passed as parameter (from the view)
        to the server.
        (Messages are serializable Objects so we use out.WriteObject)
     */
    @Override
    public void sendMessage(Message message) {
        try {
            out.writeObject(message);
            out.flush();
            out.reset();
        } catch (IOException e) {
            disconnect();
        }
    }

    @Override
    public void readMessage() {
        readExecutionQueue.execute(() -> {

            while (!readExecutionQueue.isShutdown()) {
                Message message;
                try {
                    message = (Message) in.readObject();
                    System.out.println("Received: " + message.toString());
                } catch (IOException | ClassNotFoundException e) {
                    System.out.println("Connection lost with the server.");
                    disconnect();
                    readExecutionQueue.shutdownNow();
                }
            }});
        }

    @Override
    public void disconnect() {
        try {
            if (!socket.isClosed()) {
                out.close();
                in.close();
                readExecutionQueue.shutdownNow();
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
