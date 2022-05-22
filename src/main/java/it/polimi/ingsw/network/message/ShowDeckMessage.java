package it.polimi.ingsw.network.message;

import it.polimi.ingsw.model.Game;

public class ShowDeckMessage extends Message{

    private Game game;

    public ShowDeckMessage(Game game) {
        super(null, MessageType.SHOW_DECK_MESSAGE);
        this.game = game;
    }

    public Game getGame() {
        return game;
    }
}
