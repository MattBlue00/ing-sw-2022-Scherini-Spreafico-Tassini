package it.polimi.ingsw;

import it.polimi.ingsw.controller.ClientController;
import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.network.client.SocketClient;
import it.polimi.ingsw.view.cli.CommandLineInterface;

import java.io.IOException;

public class ClientMainCLI {

    public static void main(String[] args){

        try {
            SocketClient c = new SocketClient("127.0.0.1", 12345);
            CommandLineInterface cli = new CommandLineInterface();
            ClientController clientController = new ClientController(cli,c);
            cli.addObserver(clientController);
            cli.askNickname();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

}
