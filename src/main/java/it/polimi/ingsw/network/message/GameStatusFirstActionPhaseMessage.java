package it.polimi.ingsw.network.message;

import it.polimi.ingsw.model.Game;

/**
 * This {@link Message} is used to communicate the current game status to the players. This special version of the
 * {@link GameStatusMessage} has been designed for CLIs, and allows them to display useful information in a
 * convenient way.
 */

public class GameStatusFirstActionPhaseMessage extends Message{

    private final Game game;

    /**
     * Default constructor.
     *
     * @param game the serialized game model.
     */

    public GameStatusFirstActionPhaseMessage(Game game){
        super(null, MessageType.GAME_STATUS_FIRST_ACTION_PHASE);
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
