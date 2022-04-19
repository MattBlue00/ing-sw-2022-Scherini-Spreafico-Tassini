package it.polimi.ingsw.network.server;

import it.polimi.ingsw.network.message.Message;

public interface ClientHandler {
    /*
        Returns the connection status.
    */
    boolean isConnected();

    /*
        Disconnects from the client.
    */
    void disconnect();

    /*
        Sends a message to the client.
    */
    void sendMessage(Message message);
}
