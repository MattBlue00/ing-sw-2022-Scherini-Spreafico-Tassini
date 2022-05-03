package it.polimi.ingsw.network.message;

public class AssistantCardMessage extends Message{

    private String cardName;

    public AssistantCardMessage(String nickname, String cardName) {
        super(nickname, MessageType.ASSISTANT_CARD_REPLY);
        this.cardName = cardName;
    }

    public String getCardName() {
        return cardName;
    }

}
