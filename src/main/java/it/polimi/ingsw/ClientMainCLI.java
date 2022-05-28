package it.polimi.ingsw;

import it.polimi.ingsw.controller.ClientController;
import it.polimi.ingsw.view.cli.CommandLineInterface;

public class ClientMainCLI {

    public static void main(String[] args){
        CommandLineInterface cli = new CommandLineInterface();
        ClientController clientController = new ClientController(cli);
        cli.addObserver(clientController);
        cli.init();
    }
}
