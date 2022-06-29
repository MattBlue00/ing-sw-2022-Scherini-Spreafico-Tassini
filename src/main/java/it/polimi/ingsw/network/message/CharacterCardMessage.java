package it.polimi.ingsw.network.message;

/**
 * This {@link Message} is used to communicate which parameter-less Character Card has been chosen.
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

    /**
     * Returns the id of the chosen card.
     *
     * @return an {@code int} representing the id of the chosen card.
     */

    public int getCardID(){
        return cardID;
    }

}
