package it.polimi.ingsw.network.message;

public class GenericMessage extends Message{
    private final String messageContent;
    public GenericMessage(String messageContent) {
        super(null, MessageType.GENERIC);
        this.messageContent = messageContent;
    }


    @Override
    public String toString() {
        return messageContent;
    }
}
