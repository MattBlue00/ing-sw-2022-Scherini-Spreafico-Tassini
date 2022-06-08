package it.polimi.ingsw.network.message;

public class UpdateMessage extends Message {
    private final String messageContent;

    public UpdateMessage(String messageContent) {
        super(null, MessageType.UPDATE);
        this.messageContent = messageContent;
    }


    @Override
    public String toString() {
        return messageContent;
    }
}
