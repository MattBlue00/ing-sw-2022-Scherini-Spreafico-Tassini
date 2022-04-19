package it.polimi.ingsw.network.message;

public class CharacterCardReply extends Message{

    private int cardID;

    public CharacterCardReply(String nickname, int cardID) {
        super(nickname, MessageType.CHARACTER_CARD_REPLY);
        this.cardID = cardID;
    }

    public int getCardID(){
        return cardID;
    }

}
