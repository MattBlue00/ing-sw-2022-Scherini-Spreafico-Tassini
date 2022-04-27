package it.polimi.ingsw.network.message;

import it.polimi.ingsw.model.Wizard;

public class PlayerLoginRequest extends Message{

    private final Wizard wizardID;

    public PlayerLoginRequest(String nickname, Wizard wizard) {
        super(nickname, MessageType.PLAYER_LOGIN);
        this.wizardID = wizard;
    }

    public Wizard getWizardID() {
        return wizardID;
    }
}
