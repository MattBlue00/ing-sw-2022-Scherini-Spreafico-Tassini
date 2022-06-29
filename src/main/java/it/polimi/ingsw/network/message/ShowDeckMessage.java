package it.polimi.ingsw.network.message;

import it.polimi.ingsw.model.Game;

/**
 * This {@link Message} is used to communicate to the client the player's Assistant Card deck, so that it is
 * correctly shown.
 */

public class ShowDeckMessage extends Message{

    private final Game game;

    /**
     * Default constructor.
     *
     * @param game the serialized game model.
     */

    public ShowDeckMessage(Game game) {
        super(null, MessageType.SHOW_DECK_MESSAGE);
        this.game = game;
    }

    /**
     * Returns the current game model to pick the deck from.
     *
     * @return the current game model.
     */

    public Game getGame() {
        return game;
    }
}
