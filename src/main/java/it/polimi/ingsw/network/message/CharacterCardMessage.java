package it.polimi.ingsw.network.message;

/**
 * The message is used when a CharacterCard is played,
 * the card choice is based on the card id.
 */
public class CharacterCardMessage extends Message{

    private final int cardID;

    /**
     * Default constructor.
     *
     * @param nickname client's nickname.
     * @param cardID the chosen card id.
     */
    public CharacterCardMessage(String nickname, int cardID) {
        super(nickname, MessageType.CHARACTER_CARD_REPLY);
        this.cardID = cardID;
    }

    public int getCardID(){
        return cardID;
    }

}
