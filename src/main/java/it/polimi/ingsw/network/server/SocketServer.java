package it.polimi.ingsw.network.server;

import it.polimi.ingsw.exceptions.TryAgainException;
import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.network.message.Message;
import it.polimi.ingsw.utils.Constants;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static it.polimi.ingsw.network.server.Server.LOGGER;

/**
 * This class implements the socket on the computer's local address and on a specified port.
 */

public class SocketServer implements Runnable{

    private final Server server;
    private final int port;
    private ServerSocket serverSocket;
    private final ScheduledExecutorService pinger;

    /**
     * SocketServer constructor.
     *
     * @param server the {@link Server} related to the socket.
     * @param port an {@code int} representing the chosen socket's port.
     */

    public SocketServer(Server server, int port) {
        this.server = server;
        this.port = port;
        this.pinger = Executors.newSingleThreadScheduledExecutor();
    }

    /**
     * Create a new {@link ServerSocket} associated to the chosen port. The threads will accept different clients
     * (each one has its own socket). A new thread is created with the associated {@link ClientHandler}, and
     * eventually starts to run.
     */

    @Override
    public void run() {
        try {
            serverSocket = new ServerSocket(port, 100, InetAddress.getLocalHost());
            LOGGER.info("Server running on port: "+port + "\nServer address: " + serverSocket.getInetAddress());
        } catch (IOException ex) {
            LOGGER.severe("Error initializing serverSocket.\n" + ex.getClass().getSimpleName() +
                    ": " + ex.getMessage());
        }
        while (!Thread.currentThread().isInterrupted()) {
            try {
                Socket client = serverSocket.accept();
                SocketClientHandler clientHandler = new SocketClientHandler(this, client);
                Thread thread = new Thread(clientHandler, "ss_handler: " + client.getInetAddress());
                thread.start();
                pinger.scheduleAtFixedRate(this::isReachable, 0, 1000, TimeUnit.MILLISECONDS);
            } catch (IOException ex) {
                LOGGER.severe("Connection ended.\n" + ex.getClass().getSimpleName() + ": " + ex.getMessage());
            }
        }
        LOGGER.severe("Server is offline.\n");
    }

    /**
     * Returns the Server associated to the socket.
     *
     * @return the {@link Server} associated to the socket.
     */

    public Server getServer() {
        return server;
    }

    /**
     * Tells the Server to add a given client.
     *
     * @param nickname the nickname of the client to connect.
     * @param clientHandler the {@link ClientHandler} to Associate.
     * @throws TryAgainException when the {@link Client}'s nickname has already been chosen.
     */

    public void addClient(String nickname, ClientHandler clientHandler) throws TryAgainException {
        server.addClient(nickname, clientHandler);
    }

    /**
     * Returns the message received from the client.
     *
     * @param message received from {@link SocketClientHandler} to send to the {@link Server}.
     */

    public void getMessage(Message message){
        server.getMessage(message);
    }

    /**
     * Notifies the Server of a client's disconnection.
     *
     * @param clientHandler the {@link ClientHandler} to pass to the server.
     */

    public void onDisconnect(ClientHandler clientHandler){
        server.onDisconnect(clientHandler);
    }

    /**
     * Notifies the Server that the client has ended a game.
     *
     * @param clientHandler the {@link ClientHandler} to pass to the server.
     */

    public void onQuit(ClientHandler clientHandler){
        server.onQuit(clientHandler);
    }

    /**
     * Checks if all the clients are reachable.
     */

    public void isReachable(){
        server.getClientHandlerMap().forEach( (name, clientHandler) -> {
            try {
                boolean reachable;
                reachable = clientHandler.getSocketClient().getInetAddress().isReachable(Constants.CONNECTION_TIMEOUT_SERVER);
                if(!reachable){
                    LOGGER.severe(name + " disconnected: connection timed out");
                    onDisconnect(clientHandler);
                }
            } catch (IOException e) {
                LOGGER.severe("Something went wrong.\n" + e.getClass().getSimpleName() + ": " + e.getMessage());
                throw new RuntimeException(e);
            }
        });
    }
}
