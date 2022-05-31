package it.polimi.ingsw.network.client;

import it.polimi.ingsw.network.message.Message;
import it.polimi.ingsw.observers.Observable;

import java.util.logging.Logger;

public abstract class Client extends Observable<Message> {
    /*
        Sends a message to the server.
     */
    public abstract void sendMessage(Message message);

    /*
        Asynchronously reads a message from the server and notifies the ClientController.
     */
    public abstract void readMessage();

    /*
        Disconnects from the server.
     */
    public abstract void disconnect();

    /*
     * Enable a heartbeat (ping messages) to keep the connection alive.
     */
    public abstract void enablePinger(boolean enabled);

}
