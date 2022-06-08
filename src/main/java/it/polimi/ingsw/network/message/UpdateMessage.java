package it.polimi.ingsw.network.message;

/**
 * The message extends {@link Message}.
 * It is used when the Server sends updates about
 * the game to the clients such as Last card played and
 * current player.
 */
public class UpdateMessage extends Message {
    private final String messageContent;

    /**
     * Default constructor.
     *
     * @param messageContent String update to send.
     */
    public UpdateMessage(String messageContent) {
        super(null, MessageType.UPDATE);
        this.messageContent = messageContent;
    }


    @Override
    public String toString() {
        return messageContent;
    }
}
