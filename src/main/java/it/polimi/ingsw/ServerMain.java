package it.polimi.ingsw;

import it.polimi.ingsw.network.server.Server;
import it.polimi.ingsw.network.server.SocketServer;

/**
 * This class contains a {@code main} method that launches the Eriantys' server, so that clients can connect to it and
 * play the game.
 */

public class ServerMain {

    /**
     * Launches the Eriantys' server. Its socket will listen on the device's IP at the default port "12345".
     *
     * @param args default argument.
     */

    public static void main(String[] args) {
        Server server = new Server();
        SocketServer socketServer = new SocketServer(server, 12345);
        socketServer.run();
    }
}
