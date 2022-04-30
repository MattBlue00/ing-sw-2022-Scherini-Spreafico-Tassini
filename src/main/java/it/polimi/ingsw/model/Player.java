package it.polimi.ingsw.model;

import it.polimi.ingsw.model.exceptions.FullTableException;
import it.polimi.ingsw.model.exceptions.NonExistentColorException;
import it.polimi.ingsw.model.exceptions.StudentNotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Player {

    private final String wizardID;
    private final String nickname;
    private List<AssistantCard> deck;
    private School school;
    private int coinsWallet;
    private Optional<AssistantCard> lastAssistantCardPlayed;
    private boolean characterCardAlreadyPlayed;

    public Player(String wizardID, String nickname, int playersNumber) {
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



        this.school = new School(playersNumber);    // TODO: School depends on numberOfPlayers, which is set to be decided after Players' creations
        this.lastAssistantCardPlayed = Optional.empty();
        this.characterCardAlreadyPlayed = false;
    }

    // Getter and setter methods

    public List<AssistantCard> getDeck() {
        return deck;
    }

    public String getNickname() {
        return nickname;
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

    public String getWizardID() { return wizardID; }

    public Optional<AssistantCard> getLastAssistantCardPlayed() {
        return lastAssistantCardPlayed;
    }

    public void setLastAssistantCardPlayed(AssistantCard lastAssistantCardPlayed) {
        this.lastAssistantCardPlayed = Optional.of(lastAssistantCardPlayed);
    }

    public void resetLastAssistantCardPlayed(){
        this.lastAssistantCardPlayed = Optional.empty();
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
        Optional<AssistantCard> chosenCard = getAssistantCardFromName(cardName);
        this.lastAssistantCardPlayed = chosenCard;
        getDeck().remove(chosenCard.get());
    }

    /*
        Find all cards with the name equals to the parameter,
        hypothesis: deck does not contain cards with same name.
     */
    public Optional<AssistantCard> getAssistantCardFromName(String cardName){
        return getDeck().stream().filter(card -> card.getName().equals(cardName)).findFirst();
    }

    public void moveStudent(Island island, String color) throws StudentNotFoundException {
        this.school.moveStudentToIsland(island, color);
    }

    public void moveStudent(String color) throws
            StudentNotFoundException, NonExistentColorException, FullTableException {
        this.school.moveStudentToTable(this, color);
    }

    /*public void moveStudentToHall(String color) throws NonExistentColorException {
        this.school.moveStudentToHall(this, color);
    }*/

}
