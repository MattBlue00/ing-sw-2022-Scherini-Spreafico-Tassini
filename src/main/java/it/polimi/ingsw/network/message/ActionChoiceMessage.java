package it.polimi.ingsw.network.message;

/**
 *  The {@link Message} is used to communicate the choice
 *  (Character card or student movement) during ActionPhase
 *  when in GameExpertMode.
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

    public String getChoice() {
        return choice;
    }
}
