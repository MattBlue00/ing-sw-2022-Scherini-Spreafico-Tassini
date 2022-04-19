package it.polimi.ingsw;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.exceptions.EmptyBagException;
import it.polimi.ingsw.model.exceptions.EmptyCloudException;
import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.network.client.SocketClient;
import it.polimi.ingsw.network.server.Server;
import it.polimi.ingsw.network.server.SocketServer;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main(String[] args) {

        // For network debugging purpose only
        GameController g = new GameController();
        Server s1 = new Server(g);

        SocketServer socketServer = new SocketServer(s1, 12345);
        socketServer.run();
    }
}
