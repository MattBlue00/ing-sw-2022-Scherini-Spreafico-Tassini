package it.polimi.ingsw.network.message;

/**
 * The message extends {@link Message}.
 * It is used to notify if the game is in the action phase.
 */
public class GamePhaseMessage extends Message{

    private final boolean isActionPhase;

    /**
     * Default constructor.
     *
     * @param isActionPhase boolean: true if it is action phase.
     */
    public GamePhaseMessage(boolean isActionPhase){
        super(null, MessageType.GAME_PHASE);
        this.isActionPhase = isActionPhase;
    }

    public boolean isActionPhase() {
        return isActionPhase;
    }

}
