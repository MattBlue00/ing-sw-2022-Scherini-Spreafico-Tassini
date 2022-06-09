package it.polimi.ingsw.network.message;

/**
 * The message extends {@link Message}.
 * It is used to send clients a generic String Message.
 * It can contain information about game status or back-end events.
 */
public class GenericMessage extends Message{
    private final String messageContent;

    /**
     * Default constructor.
     *
     * @param messageContent the String to send.
     */
    public GenericMessage(String messageContent) {
        super(null, MessageType.GENERIC);
        this.messageContent = messageContent;
    }


    @Override
    public String toString() {
        return messageContent;
    }
}
