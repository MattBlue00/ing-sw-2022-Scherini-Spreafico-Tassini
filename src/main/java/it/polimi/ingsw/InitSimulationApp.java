package it.polimi.ingsw;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.controller.GameControllerExpertMode;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Wizard;
import it.polimi.ingsw.network.message.PlayerNumberMessage;

public class InitSimulationApp {

    public static void main(String[] args) {
        GameController gc = new GameControllerExpertMode();
        PlayerNumberMessage message = new PlayerNumberMessage("Matteo", 2);
        gc.prepareGame(message.getPlayerNumber());

        Player p1 = new Player(Wizard.BLUE_WIZARD, "Matteo", gc.getGame().getConstants());
        Player p2 = new Player(Wizard.PINK_WIZARD, "Ludo", gc.getGame().getConstants());
        gc.getGame().addPlayer(p1);
        gc.getGame().addPlayer(p2);
        gc.startGame();

        gc.getGame().showGameBoard();
    }

}
