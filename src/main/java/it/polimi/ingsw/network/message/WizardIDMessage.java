package it.polimi.ingsw.network.message;

/**
 * This {@link Message} is used to communicate to the server the ID of the wizard chosen by the client.
 */

public class WizardIDMessage extends Message{

    private final String wizardID;

    /**
     * WizardIDMessage constructor.
     *
     * @param nickname client's nickname
     * @param wizardID the ID of the chosen wizard.
     */

    public WizardIDMessage(String nickname, String wizardID) {
        super(nickname, MessageType.WIZARD_ID);
        this.wizardID = wizardID;
    }

    /**
     * Returns the ID of the chosen wizard.
     *
     * @return the {@link String} representation of the ID of the chosen wizard.
     */

    public String getWizardID() {
        return wizardID;
    }
}
