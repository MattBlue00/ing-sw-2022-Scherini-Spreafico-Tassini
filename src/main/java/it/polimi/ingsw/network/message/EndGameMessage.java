package it.polimi.ingsw.network.message;

/**
 * This {@link Message} is used to communicate to the players that the match they are playing has ended.
 */

public class EndGameMessage extends Message {

    /**
     * EndGameMessage constructor.
     */

    public EndGameMessage() {
        super(null, MessageType.END_GAME);
    }

}
