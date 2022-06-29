package it.polimi.ingsw.network.message;

import it.polimi.ingsw.model.Game;

/**
 * This {@link Message} is used to communicate the current game status to the players.
 */

public class GameStatusMessage extends Message{

    private final Game game;

    /**
     * Default constructor.
     *
     * @param game the serialized game model.
     */

    public GameStatusMessage(Game game) {
        super(null, MessageType.GAME_STATUS);
        this.game = game;
    }

    /**
     * Returns the up-to-date game model.
     *
     * @return the up-to-date game model.
     */

    public Game getGame() {
        return game;
    }
}
