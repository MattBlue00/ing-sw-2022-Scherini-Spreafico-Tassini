package it.polimi.ingsw.network.message;

/**
 * This {@link Message} is used to communicate which Assistant Card has been chosen.
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

    /**
     * Returns the name of the chosen card.
     *
     * @return the name of the chosen card.
     */

    public String getCardName() {
        return cardName;
    }

}
