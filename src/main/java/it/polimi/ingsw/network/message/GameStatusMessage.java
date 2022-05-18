package it.polimi.ingsw.network.message;

import it.polimi.ingsw.model.Game;

public class GameStatusMessage extends Message{

    private Game game;

    public GameStatusMessage(Game game) {
        super(null, MessageType.GAME_STATUS);
        this.game = game;
    }

    public Game getGame() {
        return game;
    }
}
