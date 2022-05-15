package it.polimi.ingsw.network.message;

public class SuccessMessage extends Message{
    private final String messageContent;
    public SuccessMessage(String messageContent) {
        super("SERVER", MessageType.SUCCESS_MESSAGE);
        this.messageContent = messageContent;
    }

    public String getMessageContent() {
        return messageContent;
    }
}
