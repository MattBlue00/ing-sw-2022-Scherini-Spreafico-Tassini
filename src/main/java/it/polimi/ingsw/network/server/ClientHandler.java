package it.polimi.ingsw.network.server;

import it.polimi.ingsw.network.message.Message;
import it.polimi.ingsw.view.VirtualView;

import java.net.Socket;

/**
 * Interface to handle clients. Every type of connection must implement this interface.
 */
public interface ClientHandler {
    /**
     * Returns the connection status.
     *
     * @return {@code true} if the client is still connected and reachable, {@code false} otherwise.
     */
    boolean isConnected();

    /**
     * Disconnects from the client.
     */
    void disconnect();

    /**
     *  Sends a message to the client.
     *
     *  @param message the message to be sent.
     */
    void sendMessage(Message message);

    /**
     * This method sets the {@code VirtualView}
     *
     * @param virtualView the VirtualView that ha to be set.
     */
    void setVirtualView(VirtualView virtualView);

    /**
     * This methods returns the {@code VirtualView}.
     *
     * @return {@link VirtualView}
     */
    VirtualView getVirtualView();

    /**
     * This methods returns the {@code socketClient}.
     *
     * @return {@link Socket}
     */
    Socket getSocketClient();
}
