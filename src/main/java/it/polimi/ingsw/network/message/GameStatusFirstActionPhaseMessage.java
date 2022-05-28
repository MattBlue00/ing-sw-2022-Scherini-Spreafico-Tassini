package it.polimi.ingsw.network.message;

import it.polimi.ingsw.model.Game;

public class GameStatusFirstActionPhaseMessage extends Message{

    private Game game;

    public GameStatusFirstActionPhaseMessage(Game game){
        super(null, MessageType.GAME_STATUS_FIRST_ACTION_PHASE);
        this.game = game;
    }

    public Game getGame() {
        return game;
    }
}
