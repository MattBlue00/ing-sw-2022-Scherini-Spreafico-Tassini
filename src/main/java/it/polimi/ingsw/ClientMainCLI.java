package it.polimi.ingsw;

import it.polimi.ingsw.controller.ClientController;
import it.polimi.ingsw.view.cli.CommandLineInterface;

/**
 * This class contains a {@code main} method that allows a person to launch Eriantys via CLI.
 */

public class ClientMainCLI {

    /**
     * Launches Eriantys via CLI.
     *
     * @param args default argument.
     */

    public static void main(String[] args){
        CommandLineInterface cli = new CommandLineInterface();
        ClientController clientController = new ClientController(cli);
        cli.addObserver(clientController);
        cli.init();
    }

}
