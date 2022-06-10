package it.polimi.ingsw.network.message;

/**
 * This message extends {@link Message}.
 * It is used to trigger the closing of the clients' app.
 */

public class EndGameMessage extends Message {

    /**
     * EndGameMessage constructor.
     */

    public EndGameMessage() {
        super(null, MessageType.END_GAME);
    }

}
