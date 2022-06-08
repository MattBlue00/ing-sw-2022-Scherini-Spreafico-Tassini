package it.polimi.ingsw.network.client;

import it.polimi.ingsw.network.message.ErrorMessage;
import it.polimi.ingsw.network.message.Message;
import it.polimi.ingsw.network.server.Server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.concurrent.ExecutorService;


/**
 * The implementation of the Client interface.
 * It can send and receive messages from and to a {@link Server}
 */
public class SocketClient extends Client{

    private final Socket socket;
    private final ObjectOutputStream out;
    private final ObjectInputStream in;
    private ExecutorService readExecutionQueue;
    private static final int SOCKET_TIMEOUT = 10000;

    public SocketClient(String address, int port) throws IOException {
        this.socket = new Socket();
        this.socket.connect(new InetSocketAddress(address, port), SOCKET_TIMEOUT);
        this.out = new ObjectOutputStream(socket.getOutputStream());
        this.in = new ObjectInputStream(socket.getInputStream());
    }


    /**
     * The method sends the message passed as parameter (from the view)
     * to the server.
     * (Messages are serializable Objects so we use out.WriteObject).
     * @param message the message to send.
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

    /**
     *  The method reads messages from the {@link Server} asynchronously
     *  using {@link ExecutorService}.
     */
    @Override
    public void readMessage() {
        readExecutionQueue.execute(() -> {
            while (!readExecutionQueue.isShutdown()) {
                Message message;
                try {
                    message = (Message) in.readObject();
                } catch (IOException | ClassNotFoundException e) {
                    message = new ErrorMessage("Connection lost...");
                    disconnect();
                }
                notifyObservers(message);
            }
        });
    }

    /**
     * The method makes the Client disconnect from the {@link Server}
     */
    @Override
    public void disconnect() {
        try {
            if (!socket.isClosed()) {
                System.out.println("---Disconnected from the server---");
                out.close();
                in.close();
                readExecutionQueue.shutdownNow();
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @return the actual Socket.
     */
    public Socket getSocket() {
        return socket;
    }

    /**
     * Sets the {@link ExecutorService}.
     * @param readExecutionQueue the {@link ExecutorService} to set.
     */
    public void setReadExecutionQueue(ExecutorService readExecutionQueue) {
        this.readExecutionQueue = readExecutionQueue;
    }
}
