package it.polimi.ingsw;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.controller.GameState;
import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.network.client.ClientController;
import it.polimi.ingsw.view.cli.CommandLineInterface;

import java.util.logging.Level;

public class ClientApp {

    public static void main(String[] args) {

        boolean cliParam = false; // default value

        for (String arg : args) {
            if (arg.equals("--cli") || arg.equals("-c")) {
                cliParam = true;
                break;
            }
        }

        if (cliParam) {
            Client.LOGGER.setLevel(Level.WARNING);
            CommandLineInterface view = new CommandLineInterface();
            ClientController clientcontroller = new ClientController(view);
            view.addObserver(clientcontroller);
            //view.init(); // TODO: need to implement init method first!
        } else {
            //Application.launch(JavaFXGui.class); // TODO: need to implement a GUI first!
        }
    }
}
