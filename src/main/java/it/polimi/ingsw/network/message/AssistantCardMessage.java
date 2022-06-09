package it.polimi.ingsw.network.message;

/**
 * The message is used when an AssistantCard is played,
 * the card choice is based on the String cardName.
 */
public class AssistantCardMessage extends Message{

    private final String cardName;

    /**
     * Default constructor.
     *
     * @param nickname client's nickname.
     * @param cardName the chosen card name.
     */
    public AssistantCardMessage(String nickname, String cardName) {
        super(nickname, MessageType.ASSISTANT_CARD_REPLY);
        this.cardName = cardName;
    }

    public String getCardName() {
        return cardName;
    }

}
