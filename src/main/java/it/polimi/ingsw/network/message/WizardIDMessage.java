package it.polimi.ingsw.network.message;

/**
 * This Message class is used to select a WizardID.
 */
public class WizardIDMessage extends Message{

    String wizardID;

    /**
     * WizardIDMessage constructor.
     *
     * @param nickname client's nickname
     * @param wizardID wizard chosen
     */
    public WizardIDMessage(String nickname, String wizardID) {
        super(nickname, MessageType.WIZARD_ID);
        this.wizardID = wizardID;
    }

    /**
     * This method returns the wizardID.
     *
     * @return {@code WizardID}
     */
    public String getWizardID() {
        return wizardID;
    }
}
