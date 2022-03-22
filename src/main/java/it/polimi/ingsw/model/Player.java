package it.polimi.ingsw.model;

import it.polimi.ingsw.model.charactercards.MotherNaturePlusTwoStrategy;
import it.polimi.ingsw.model.exceptions.CharacterCardAlreadyPlayedException;
import it.polimi.ingsw.model.exceptions.NonExistentCharacterCardException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class Player {

    private final Wizard wizardID;
    private final String nickname;
    private List<AssistantCard> deck;
    private School school;
    private int coinsWallet;
    private AssistantCard lastCardPlayed;

    private boolean characterCardPlayed;
    private int motherNatureStepsLeft;

    public Player(Wizard wizardID, String nickname) {
        this.wizardID = wizardID;
        this.nickname = nickname;
        this.coinsWallet = 1;
        this.deck = new ArrayList<>(10);
        this.school = new School(2);    // TODO: School depends on numberOfPlayers, which is set to be decided after Players' creations
        this.lastCardPlayed = null;
        this.school = new School(2);    // TODO: School depends on numberOfPlayers,
                                                     // which is set to be decided after Players' creations
        this.motherNatureStepsLeft = 0;
        this.characterCardPlayed = false;
    }

    // getter and setter
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
        return lastCardPlayed;
    }
    public School getSchool() {
        return school;
    }

    public boolean getCharacterCardPlayed() {
        return characterCardPlayed;
    }

    public void setCharacterCardPlayed(boolean characterCardPlayed) {
        this.characterCardPlayed = characterCardPlayed;
    }

    public int getMotherNatureStepsLeft() {
        return motherNatureStepsLeft;
    }

    public void setMotherNatureStepsLeft(int motherNatureStepsLeft) {
        this.motherNatureStepsLeft = motherNatureStepsLeft;
    }

    // methods

    public AssistantCard playAssistantCard(int cardID) {
        this.lastCardPlayed = getDeck().remove(cardID);
        return lastCardPlayed;
    }

    public void moveStudent(){
        // in the final version the view will be responsible to get the choice from the player
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        try {
            String choice = br.readLine().toUpperCase(Locale.ROOT);
            String color = br.readLine().toUpperCase(Locale.ROOT);
            if(choice.equals("ISLAND")){
                int islandID = Integer.parseInt(br.readLine());
                this.school.moveStudentToIsland(islandID, color);
            }
            else{
                this.school.moveStudentToTable(this, color);
            }

        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    public void playCharacterCard(int id) throws
            NonExistentCharacterCardException, CharacterCardAlreadyPlayedException{

        // this choice will be handled by the view when available
        if(characterCardPlayed)
            throw new CharacterCardAlreadyPlayedException("You have already played a character card this round!");
        else {
            switch (id) {
                case 1:
                    new CharacterCard(id, new MotherNaturePlusTwoStrategy()).getStrategy().doEffect(this);
                    characterCardPlayed = true;
                    break;
                default:
                    throw new NonExistentCharacterCardException("A character card with id " + id + " was not found.");
            }
        }
    }
}
