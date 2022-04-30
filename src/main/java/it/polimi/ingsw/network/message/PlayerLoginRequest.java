package it.polimi.ingsw.network.message;

public class PlayerLoginRequest extends Message{

    private final String wizardID;

    public PlayerLoginRequest(String nickname, String wizard) {
        super(nickname, MessageType.PLAYER_LOGIN);
        this.wizardID = wizard;
    }

    public String getWizardID() {
        return wizardID;
    }
}
