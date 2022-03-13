package it.polimi.ingsw.model;

import java.util.ArrayList;
import java.util.List;

public class Player {
    private final Wizard wizardID;
    private final String nickname;
    private final AssistantCard[] deck;


    public Player(Wizard wizardID, String nickname) {
        this.wizardID = wizardID;
        this.nickname = nickname;
        this.deck = new AssistantCard[10];
    }

    public Wizard getWizardID() {
        return wizardID;
    }

    public String getNickname() {
        return nickname;
    }

    public AssistantCard[] getDeck() {
        return deck;
    }
}
