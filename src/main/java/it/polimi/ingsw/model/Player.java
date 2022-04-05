package it.polimi.ingsw.model;

import it.polimi.ingsw.model.exceptions.CharacterCardAlreadyPlayedException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

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
        int j = 0;
        for(int i = 1; i <= 10; i++){
            if(!(i%2==0))
                j++;
            this.deck.add(new AssistantCard(i, i, j));
        }

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

    public AssistantCard getLastCardPlayed() {
        return lastAssistantCardPlayed;
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

    public void moveStudent(int islandID, String color){
        this.school.moveStudentToIsland(islandID, color);
    }

    public void moveStudent(String color){
        this.school.moveStudentToTable(this, color);
    }

}
