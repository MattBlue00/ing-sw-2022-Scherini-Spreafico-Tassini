package it.polimi.ingsw.network.message;

/**
 * This {@link Message} is used to communicate to the players who are waiting for their turn the actions done by the
 * current player.
 */

public class UpdateMessage extends Message {

    private final String messageContent;

    /**
     * Default constructor.
     *
     * @param messageContent the message to communicate.
     */

    public UpdateMessage(String messageContent) {
        super(null, MessageType.UPDATE);
        this.messageContent = messageContent;
    }

    /**
     * Returns the content of the message.
     *
     * @return the content of the message.
     */

    @Override
    public String toString() {
        return messageContent;
    }
}
