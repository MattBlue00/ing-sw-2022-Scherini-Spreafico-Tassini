package it.polimi.ingsw.network.message;

import it.polimi.ingsw.model.Game;

/**
 * The message extends {@link Message}.
 * It is used to display the Assistant card's deck
 * to the player.
 */
public class ShowDeckMessage extends Message{

    private final Game game;

    /**
     * Default constructor.
     *
     * @param game serialized {@link Game} Model.
     */
    public ShowDeckMessage(Game game) {
        super(null, MessageType.SHOW_DECK_MESSAGE);
        this.game = game;
    }

    public Game getGame() {
        return game;
    }
}
