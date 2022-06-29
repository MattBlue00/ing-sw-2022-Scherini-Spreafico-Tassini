package it.polimi.ingsw.network.message;

/**
 * This {@link Message} is used to communicate to the client a change of phase during a match.
 */

public class GamePhaseMessage extends Message{

    private final boolean isActionPhase;

    /**
     * Default constructor.
     *
     * @param isActionPhase {@code true} if the match is entering the Action Phase, {@code false} otherwise.
     */

    public GamePhaseMessage(boolean isActionPhase){
        super(null, MessageType.GAME_PHASE);
        this.isActionPhase = isActionPhase;
    }

    /**
     * Returns a flag that indicates in which phase the match is.
     *
     * @return {@code true} if the match is entering the Action Phase, {@code false} otherwise.
     */

    public boolean isActionPhase() {
        return isActionPhase;
    }

}
