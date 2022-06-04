package it.polimi.ingsw.network.server;

import it.polimi.ingsw.network.message.Message;
import it.polimi.ingsw.view.VirtualView;

import java.net.Socket;

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

    void setVirtualView(VirtualView virtualView);

    VirtualView getVirtualView();

    Socket getSocketClient();
}
