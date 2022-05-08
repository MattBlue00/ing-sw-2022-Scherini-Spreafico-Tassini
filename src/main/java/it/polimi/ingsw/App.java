package it.polimi.ingsw;

import it.polimi.ingsw.network.server.Server;
import it.polimi.ingsw.network.server.SocketServer;


public class App 
{
    public static void main(String[] args) {

        // For network debugging purpose only
        Server s1 = new Server();

        SocketServer socketServer = new SocketServer(s1, 12345);
        socketServer.run();
    }
}
