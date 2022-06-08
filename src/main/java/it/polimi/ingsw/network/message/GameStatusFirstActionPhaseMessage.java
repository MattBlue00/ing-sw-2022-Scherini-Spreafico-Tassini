package it.polimi.ingsw.network.message;

import it.polimi.ingsw.model.Game;

public class GameStatusFirstActionPhaseMessage extends Message{

    private final Game game;

    /**
     * Default constructor.
     *
     * @param game the serialized Model.
     */
    public GameStatusFirstActionPhaseMessage(Game game){
        super(null, MessageType.GAME_STATUS_FIRST_ACTION_PHASE);
        this.game = game;
    }

    public Game getGame() {
        return game;
    }
}
