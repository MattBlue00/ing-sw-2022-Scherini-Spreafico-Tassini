package it.polimi.ingsw.network.server;

import it.polimi.ingsw.exceptions.TryAgainException;
import it.polimi.ingsw.network.message.Message;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketServer implements Runnable{

    private final Server server;
    private final int port;
    private ServerSocket serverSocket;

    public SocketServer(Server server, int port) {
        this.server = server;
        this.port = port;
    }

    /*
        Create a new ServerSocket associated to the chosen port.
        Threads will accept different clients (each one has a Socket client)
        New Thread is created with the associated ClientHandler.
        In the end the thread starts.
     */
    @Override
    public void run() {
        try {
            serverSocket = new ServerSocket(port);
            System.out.println("Server running on port: "+port);
        } catch (IOException e) {
            System.out.println("Error initializing serverSocket");
            e.printStackTrace();
        }
        while(!Thread.currentThread().isInterrupted()){
            try {
                Socket client = serverSocket.accept();
                SocketClientHandler clientHandler = new SocketClientHandler(this, client);
                Thread thread = new Thread(clientHandler, "ss_handler: "+client.getInetAddress());
                thread.start();
            } catch (IOException e) {
                System.out.println("Connection ended");
                e.printStackTrace();
            }
        }
    }

    public Server getServer() {
        return server;
    }

    public void addClient(String nickname, ClientHandler clientHandler) throws TryAgainException {
        server.addClient(nickname, clientHandler);
    }

    public void getMessage(Message message){
        server.getMessage(message);
    }

    public void onDisconnect(ClientHandler clientHandler){
        server.onDisconnect(clientHandler);
    }

}
