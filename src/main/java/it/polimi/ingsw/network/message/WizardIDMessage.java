package it.polimi.ingsw.network.message;

public class WizardIDMessage extends Message{

    String wizardID;

    public WizardIDMessage(String nickname, String wizardID) {
        super(nickname, MessageType.WIZARD_ID);
        this.wizardID = wizardID;
    }

    public String getWizardID() {
        return wizardID;
    }
}
