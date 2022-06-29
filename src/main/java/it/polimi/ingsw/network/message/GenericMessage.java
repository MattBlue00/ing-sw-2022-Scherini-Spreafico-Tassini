package it.polimi.ingsw.network.message;

/**
 * This {@link Message} is used to communicate to the clients generic messages that need to be notified.
 */

public class GenericMessage extends Message{

    private final String messageContent;

    /**
     * Default constructor.
     *
     * @param messageContent the {@link String} to send.
     */

    public GenericMessage(String messageContent) {
        super(null, MessageType.GENERIC);
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
