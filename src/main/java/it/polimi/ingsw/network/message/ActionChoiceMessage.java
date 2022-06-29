package it.polimi.ingsw.network.message;

/**
 *  This {@link Message} is used to communicate the player's choice (Character Card or student movement) during
 *  an Action Phase of an Expert mode match.
 */

public class ActionChoiceMessage extends Message{

    private final String choice;

    /**
     * Default constructor.
     *
     * @param nickname client's nickname.
     * @param choice action phase choice.
     */

    public ActionChoiceMessage(String nickname, String choice) {
        super(nickname, MessageType.ACTION_CHOICE);
        this.choice = choice;
    }

    /**
     * Returns the choice made.
     *
     * @return a {@link String} representing the choice made.
     */

    public String getChoice() {
        return choice;
    }
}
