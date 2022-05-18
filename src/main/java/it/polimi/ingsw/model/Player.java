package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.FullTableException;
import it.polimi.ingsw.exceptions.NonExistentColorException;
import it.polimi.ingsw.exceptions.StudentNotFoundException;
import it.polimi.ingsw.utils.ANSIConstants;
import it.polimi.ingsw.utils.Constants;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Player {

    private final Wizard wizardID;
    private final String nickname;
    private List<AssistantCard> deck;
    private School school;
    private int coinsWallet;
    private AssistantCard lastAssistantCardPlayed;
    private boolean characterCardAlreadyPlayed;

    public Player(Wizard wizardID, String nickname, Constants constants) {
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

        this.school = new School(constants);
        this.lastAssistantCardPlayed = null;
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

    public Wizard getWizardID() { return wizardID; }

    public AssistantCard getLastAssistantCardPlayed() {
        return lastAssistantCardPlayed;
    }

    public void setLastAssistantCardPlayed(AssistantCard lastAssistantCardPlayed) {
        this.lastAssistantCardPlayed = lastAssistantCardPlayed;
    }

    public void resetLastAssistantCardPlayed(){
        this.lastAssistantCardPlayed = null;
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
        Optional <AssistantCard> assistantCard  =
                getDeck().stream().filter(card -> card.getName().equals(cardName)).findFirst();
        return assistantCard.orElse(null);
    }
    // TODO: findFirst returns an Optional, better check

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

    public void showSchool(){

        System.out.printf("SCUOLA DI: %s \n", getNickname());

        int yellowStudents, blueStudents, greenStudents, redStudents, pinkStudents;
        yellowStudents =
                (int) this.getSchool().getHall().getStudents().stream().filter(x -> x.getColor().equals(Color.YELLOW)).count();
        blueStudents =
                (int) this.getSchool().getHall().getStudents().stream().filter(x -> x.getColor().equals(Color.BLUE)).count();
        greenStudents =
                (int) this.getSchool().getHall().getStudents().stream().filter(x -> x.getColor().equals(Color.GREEN)).count();
        redStudents =
                (int) this.getSchool().getHall().getStudents().stream().filter(x -> x.getColor().equals(Color.RED)).count();
        pinkStudents =
                (int) this.getSchool().getHall().getStudents().stream().filter(x -> x.getColor().equals(Color.PINK)).count();

        System.out.println("Ingresso: " +
                ANSIConstants.ANSI_YELLOW + yellowStudents + ANSIConstants.ANSI_RESET + " " +
                ANSIConstants.ANSI_BLUE + blueStudents + ANSIConstants.ANSI_RESET + " " +
                ANSIConstants.ANSI_GREEN + greenStudents + ANSIConstants.ANSI_RESET + " " +
                ANSIConstants.ANSI_RED + redStudents + ANSIConstants.ANSI_RESET + " " +
                ANSIConstants.ANSI_PINK + pinkStudents + ANSIConstants.ANSI_RESET);

        yellowStudents = -1;
        blueStudents = -1;
        greenStudents = -1;
        redStudents = -1;
        pinkStudents = -1;

        try {

            yellowStudents =
                    (int) this.getSchool().getTable("YELLOW").getStudents().stream().filter(x -> x.getColor().equals(Color.YELLOW)).count();
            blueStudents =
                    (int) this.getSchool().getTable("BLUE").getStudents().stream().filter(x -> x.getColor().equals(Color.BLUE)).count();
            greenStudents =
                    (int) this.getSchool().getTable("GREEN").getStudents().stream().filter(x -> x.getColor().equals(Color.GREEN)).count();
            redStudents =
                    (int) this.getSchool().getTable("RED").getStudents().stream().filter(x -> x.getColor().equals(Color.RED)).count();
            pinkStudents =
                    (int) this.getSchool().getTable("PINK").getStudents().stream().filter(x -> x.getColor().equals(Color.PINK)).count();
        } catch(NonExistentColorException ignored){}

        System.out.println("Studenti per tavolo: " +
                ANSIConstants.ANSI_YELLOW + yellowStudents + ANSIConstants.ANSI_RESET + " " +
                ANSIConstants.ANSI_BLUE + blueStudents + ANSIConstants.ANSI_RESET + " " +
                ANSIConstants.ANSI_GREEN + greenStudents + ANSIConstants.ANSI_RESET + " " +
                ANSIConstants.ANSI_RED + redStudents + ANSIConstants.ANSI_RESET + " " +
                ANSIConstants.ANSI_PINK + pinkStudents + ANSIConstants.ANSI_RESET);

        System.out.print("Professori: ");
        String yellowProf = "no";
        String blueProf = "no";
        String greenProf = "no";
        String redProf = "no";
        String pinkProf = "no";
        try {

            if (this.getSchool().getTable("YELLOW").getHasProfessor())
                yellowProf = "sì";

            if (this.getSchool().getTable("BLUE").getHasProfessor())
                blueProf = "sì";

            if (this.getSchool().getTable("GREEN").getHasProfessor())
                greenProf = "sì";

            if (this.getSchool().getTable("RED").getHasProfessor())
                redProf = "sì";

            if (this.getSchool().getTable("PINK").getHasProfessor())
                pinkProf = "sì";

        } catch(NonExistentColorException ignored){}

        System.out.println(
                ANSIConstants.ANSI_YELLOW + yellowProf + ANSIConstants.ANSI_RESET + " " +
                ANSIConstants.ANSI_BLUE + blueProf + ANSIConstants.ANSI_RESET + " " +
                ANSIConstants.ANSI_GREEN + greenProf + ANSIConstants.ANSI_RESET + " " +
                ANSIConstants.ANSI_RED + redProf + ANSIConstants.ANSI_RESET + " " +
                ANSIConstants.ANSI_PINK + pinkProf + ANSIConstants.ANSI_RESET);

        System.out.println("Torri: " + this.getSchool().getTowerRoom().getTowersLeft());
        System.out.println("--------------------");
    }
}
