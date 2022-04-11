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

    /*
        Find the chosen card in the deck
        set it as lastCardPlayed
        remove it from player's deck
     */
    public void playAssistantCard(String cardName) {
        AssistantCard chosenCard = getAssistantCardFromName(cardName);
        this.lastAssistantCardPlayed = chosenCard;
        getDeck().remove(chosenCard);
    }

    /*
        Find all cards with the name equals to the parameter,
        hypothesis: deck does not contain cards with same name.
     */
    public AssistantCard getAssistantCardFromName(String cardName){
        return (AssistantCard) getDeck().stream().filter(card -> card.getName().equals(cardName));
    }

    public void moveStudent(int islandID, String color){
        this.school.moveStudentToIsland(islandID, color);
    }

    public void moveStudent(String color){
        this.school.moveStudentToTable(this, color);
    }

}
