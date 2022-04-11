package it.polimi.ingsw.model;

import it.polimi.ingsw.model.exceptions.FullTableException;
import it.polimi.ingsw.model.exceptions.NonExistentColorException;
import it.polimi.ingsw.model.exceptions.StudentNotFoundException;

import java.util.ArrayList;
import java.util.List;

public class Player {

    private final Wizard wizardID;
    private final String nickname;
    private List<AssistantCard> deck;
    private School school;
    private int coinsWallet;
    private AssistantCard lastAssistantCardPlayed;
    private boolean characterCardAlreadyPlayed;

    public Player(Wizard wizardID, String nickname) {
        this.wizardID = wizardID;
        this.nickname = nickname;
        this.coinsWallet = 1;

        this.deck = new ArrayList<>(10);
        this.deck.add(new AssistantCard(AssistantType.CHEETAH));
        this.deck.add(new AssistantCard(AssistantType.OSTRICH));
        this.deck.add(new AssistantCard(AssistantType.CAT));
        this.deck.add(new AssistantCard(AssistantType.EAGLE));
        this.deck.add(new AssistantCard(AssistantType.FOX));
        this.deck.add(new AssistantCard(AssistantType.SNAKE));
        this.deck.add(new AssistantCard(AssistantType.OCTOPUS));
        this.deck.add(new AssistantCard(AssistantType.DOG));
        this.deck.add(new AssistantCard(AssistantType.ELEPHANT));
        this.deck.add(new AssistantCard(AssistantType.TURTLE));



        this.school = new School(2);    // TODO: School depends on numberOfPlayers, which is set to be decided after Players' creations
        this.lastAssistantCardPlayed = null;
        this.school = new School(2);    // TODO: School depends on numberOfPlayers,
                                                     // which is set to be decided after Players' creations
        this.characterCardAlreadyPlayed = false;
    }

    // Getter and setter methods

    public Wizard getWizardID() {
        return wizardID;
    }

    public String getNickname() {
        return nickname;
    }

    public List<AssistantCard> getDeck() {
        return deck;
    }

    public int getCoinsWallet() {
        return coinsWallet;
    }

    public void setCoinsWallet(int coinsWallet) {
        this.coinsWallet = coinsWallet;
    }

    public School getSchool() {
        return school;
    }

    public AssistantCard getLastAssistantCardPlayed() {
        return lastAssistantCardPlayed;
    }

    public void setLastAssistantCardPlayed(AssistantCard lastAssistantCardPlayed) {
        this.lastAssistantCardPlayed = lastAssistantCardPlayed;
    }

    public boolean getCharacterCardAlreadyPlayed() {
        return characterCardAlreadyPlayed;
    }

    public void setCharacterCardAlreadyPlayed(boolean characterCardAlreadyPlayed) {
        this.characterCardAlreadyPlayed = characterCardAlreadyPlayed;
    }

    // Player methods

    public AssistantCard playAssistantCard(int cardID) {
        this.lastAssistantCardPlayed = getDeck().remove(cardID);
        return lastAssistantCardPlayed;
    }

    public void moveStudent(int islandID, String color) throws StudentNotFoundException {
        this.school.moveStudentToIsland(islandID, color);
    }

    public void moveStudent(String color) throws
            StudentNotFoundException, NonExistentColorException, FullTableException {
        this.school.moveStudentToTable(this, color);
    }

}
