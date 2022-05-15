package it.polimi.ingsw.network.message;

public class AskMessage extends Message{
    private Ask_Type askType;
    public AskMessage(Ask_Type askType) {
        super(null, MessageType.ASK_TYPE);
        this.askType = askType;
    }

    public Ask_Type getAskType() {
        return askType;
    }
}
