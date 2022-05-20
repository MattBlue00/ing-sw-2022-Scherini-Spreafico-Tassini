package it.polimi.ingsw.network.message;

public class AskMessage extends Message{
    private AskType askType;
    public AskMessage(AskType askType) {
        super(null, MessageType.ASK_TYPE);
        this.askType = askType;
    }

    public AskType getAskType() {
        return askType;
    }
}
