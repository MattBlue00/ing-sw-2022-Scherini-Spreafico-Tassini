package it.polimi.ingsw.model;

import java.util.ArrayList;
import java.util.List;

public class Player {
    private final Wizard wizardID;
    private final String nickname;
    private List<AssistantCard> deck;
    private School school;


    public Player(Wizard wizardID, String nickname) {
        this.wizardID = wizardID;
        this.nickname = nickname;
        this.deck = new ArrayList<>(10);
        this.school = new School(2);    // TODO: School depends on numberOfPlayers,
                                                        // which is set to be decided after Players' creations
    }



    public Wizard getWizardID() {
        return wizardID;
    }

    public String getNickname() {
        return nickname;
    }

    public List<AssistantCard> getDeck() {
        return deck;
    }
}
