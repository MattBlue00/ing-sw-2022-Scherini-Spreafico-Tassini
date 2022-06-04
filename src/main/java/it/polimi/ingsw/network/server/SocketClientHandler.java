package it.polimi.ingsw.network.server;

import it.polimi.ingsw.exceptions.TryAgainException;
import it.polimi.ingsw.network.message.Message;
import it.polimi.ingsw.network.message.MessageType;
import it.polimi.ingsw.view.VirtualView;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class SocketClientHandler implements ClientHandler, Runnable{

    private final Socket client;
    private final SocketServer socketServer;

    private boolean connected;

    private final Object inputLock;
    private final Object outputLock;

    private ObjectOutputStream out;
    private ObjectInputStream in;
    private VirtualView virtualView;

    public SocketClientHandler(SocketServer socketServer, Socket client) {
        this.socketServer = socketServer;
        this.client = client;
        this.connected = true;
        this.inputLock = new Object();
        this.outputLock = new Object();
        try {
            this.out = new ObjectOutputStream(client.getOutputStream());
            this.in = new ObjectInputStream(client.getInputStream());
        } catch (IOException ex) {
            Server.LOGGER.severe("Error from socketClientHandler"+ ex.getClass().getSimpleName()
                    + ": " + ex.getMessage());
        }
    }

    public void setVirtualView(VirtualView virtualView) {
        this.virtualView = virtualView;
    }

    public VirtualView getVirtualView() {
        return virtualView;
    }

    @Override
    public Socket getSocketClient() { return client; }

    @Override
    public void run() {
        try {
            handleClientConnection();
        } catch (IOException ex) {
            Server.LOGGER.severe("Client " + client.getInetAddress() + " connection dropped. \n" +
                    ex.getClass().getSimpleName() + ": " + ex.getMessage());
            disconnect();
        }
    }

    /*
        The method reads messages from the associated client after deserializing
        the message, if it is a login request it adds the client to our client list
        else it just sends the message to the socketServer -> Server -> GameController
        FIXME: During testing it launches an Exception despite working correctly
     */
    public void handleClientConnection() throws IOException {
        Server.LOGGER.info("Client connected from " + client.getInetAddress());
        try{
            while(!Thread.currentThread().isInterrupted()) {
                synchronized (inputLock) {
                    if (in != null && connected) {
                        Message message;
                        message = (Message) in.readObject();
                        if (!message.getClass().getSimpleName().equalsIgnoreCase("PingMessage")) {
                            Server.LOGGER.info("Message: " + message.getClass().getSimpleName());
                        }
                        if (message.getMessageType() == MessageType.LOGIN_REQUEST) {
                            try {
                                socketServer.addClient(message.getNickname(), this);
                                virtualView.showExistingGames(socketServer.getServer().getGameControllerMap());
                                virtualView.askCreateOrJoin();
                            } catch (TryAgainException e) {
                                Server.LOGGER.severe("Nickname has already been chosen.");
                                virtualView.showGenericMessage("Nickname has already been chosen.");
                                virtualView.askNickname();
                            }
                        } else {
                            Server.LOGGER.info("Received: " + message.getClass().getSimpleName());
                            socketServer.getMessage(message);
                        }
                    }
                }
            }
        }catch(IOException | ClassNotFoundException ex){
            Server.LOGGER.severe("Invalid stream from client. \n" +
                    ex.getClass().getSimpleName() + ": " + ex.getMessage());
        } finally {
            disconnect();
        }
    }

    /*
        boolean, return true if the client is connected.
     */
    @Override
    public boolean isConnected() {
        return this.connected;
    }

    @Override
    public void disconnect() {
        if (connected) {
            try {
                if (!client.isClosed()) {
                    client.close();
                }
            } catch (IOException ex) {
                Server.LOGGER.severe(ex.getClass().getSimpleName() + ": " + ex.getMessage());
            }
            connected = false;
            Thread.currentThread().interrupt();
            socketServer.onDisconnect(this);
        }
    }

    @Override
    public void sendMessage(Message message) {
        try {
            synchronized (outputLock) {
                out.writeObject(message);
                out.reset();
                Server.LOGGER.info("Sent: "+message.getClass().getSimpleName());
            }
        } catch (IOException ex) {
            Server.LOGGER.severe(ex.getClass().getSimpleName() + ": " + ex.getMessage());
            disconnect();
        }
    }

}
