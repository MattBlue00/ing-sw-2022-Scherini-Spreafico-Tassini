package it.polimi.ingsw.network.message;

public class AssistantCardReply extends Message{
    private String cardName;


    AssistantCardReply(String nickname, String cardName) {
        super(nickname, MessageType.ASSISTANT_CARD_REPLY);
        this.cardName = cardName;
    }

    public String getCardName() {
        return cardName;
    }

}
