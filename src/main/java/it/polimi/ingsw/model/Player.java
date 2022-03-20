package it.polimi.ingsw.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Player {
    private final Wizard wizardID;
    private final String nickname;
    private List<AssistantCard> deck;
    private School school;
    private int coinsWallet;


    public Player(Wizard wizardID, String nickname) {
        this.wizardID = wizardID;
        this.nickname = nickname;
        this.coinsWallet = 1;
        this.deck = new ArrayList<>(10);
        this.school = new School(2);    // TODO: School depends on numberOfPlayers,
                                                     // which is set to be decided after Players' creations
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

    // methods

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

    // For debugging
    public School getSchool() {
        return school;
    }
}
