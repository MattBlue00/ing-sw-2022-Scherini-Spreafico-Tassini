package it.polimi.ingsw.network.message;

public class GamePhaseMessage extends Message{

    private boolean isActionPhase;

    public GamePhaseMessage(boolean isActionPhase){
        super(null, MessageType.GAME_PHASE);
        this.isActionPhase = isActionPhase;
    }

    public boolean isActionPhase() {
        return isActionPhase;
    }

}
