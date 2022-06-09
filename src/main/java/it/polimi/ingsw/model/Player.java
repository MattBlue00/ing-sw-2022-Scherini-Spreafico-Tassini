package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.FullTableException;
import it.polimi.ingsw.exceptions.NonExistentColorException;
import it.polimi.ingsw.exceptions.StudentNotFoundException;
import it.polimi.ingsw.utils.ANSIConstants;
import it.polimi.ingsw.utils.Constants;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * This class is the representation of a playing client in the game model. It holds all the data unique to each player
 * in a match, such as their nickname, their wizardID, the Assistant Cards they still have and their school. If the
 * match is being played in Expert mode, other useful data (otherwise present but unused), such as the coins' wallet
 * and a flag that is {@code true} if the player has already played a Character Card in a round, will be accessed and
 * updated.
 */

public class Player implements Serializable {

    private final Wizard wizardID;
    private final String nickname;
    private List<AssistantCard> deck;
    private final School school;
    private int coinsWallet;
    private AssistantCard latestAssistantCardPlayed;
    private boolean characterCardAlreadyPlayed;

    /**
     * Player constructor.
     *
     * @param wizardID the ID of the wizard the player wishes to embody.
     * @param nickname the associated client's nickname.
     * @param constants the set of constants for this game.
     */

    public Player(Wizard wizardID, String nickname, Constants constants) {
        this.wizardID = wizardID;
        this.nickname = nickname;
        this.coinsWallet = 1;

        // building the Assistant Card deck
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
        this.latestAssistantCardPlayed = null;
        this.characterCardAlreadyPlayed = false;
    }

    /**
     * Returns a list of the Assistant Cards currently present in the player's deck.
     *
     * @return a list of the Assistant Cards currently present in the player's deck.
     */

    public List<AssistantCard> getDeck() {
        return deck;
    }

    /**
     * Updates the list of the Assistant Cards currently present in the player's deck.
     *
     * @param deck the new deck of Assistant Cards.
     */

    public void setDeck(List<AssistantCard> deck) {
        this.deck = deck;
    }

    /**
     * Returns the player's nickname.
     *
     * @return a {@link String} containing the player's nickname.
     */

    public String getNickname() {
        return nickname;
    }

    /**
     * Returns the player's number of coins.
     *
     * @return an {@code int} representing the number of coins in the player's wallet.
     */

    public int getCoinsWallet() {
        return coinsWallet;
    }

    /**
     * Updates the player's number of coins.
     *
     * @param coinsWallet an {@code int} representing the new number of coins in the player's wallet.
     */

    public void setCoinsWallet(int coinsWallet) {
        this.coinsWallet = coinsWallet;
    }

    /**
     * Returns the player's {@link School}.
     *
     * @return the player's {@link School}.
     */

    public School getSchool() {
        return school;
    }

    /**
     * Returns the player's wizard ID.
     *
     * @return the ID of the wizard the player is embodying.
     */

    public Wizard getWizardID() { return wizardID; }

    /**
     * Returns the Assistant Card played by the player in the current round.
     *
     * @return the latest {@link AssistantCard} played.
     */

    public AssistantCard getLatestAssistantCardPlayed() {
        return latestAssistantCardPlayed;
    }

    /**
     * Updates the Assistant Card played by the player in the current round.
     *
     * @param latestAssistantCardPlayed the latest {@link AssistantCard} played.
     */

    public void setLatestAssistantCardPlayed(AssistantCard latestAssistantCardPlayed) {
        this.latestAssistantCardPlayed = latestAssistantCardPlayed;
    }

    /**
     * Resets (sets back to null) the attribute latestAssistantCardPlayed so that the game controller can properly
     * handle the Planning Phase (when deciding if a player can - or cannot - play a specific Assistant Card).
     */

    public void resetLatestAssistantCardPlayed(){
        this.latestAssistantCardPlayed = null;
    }

    /**
     * Checks if the player has already played a Character Card in the current round.
     *
     * @return {@code true} if the player has, {@code false} otherwise.
     */

    public boolean getCharacterCardAlreadyPlayed() {
        return characterCardAlreadyPlayed;
    }

    /**
     * Updates the boolean flag to {@code true} if the player has just played a Character Card, to {@code false} if a
     * new round has just begun.
     *
     * @param characterCardAlreadyPlayed the new flag.
     */

    public void setCharacterCardAlreadyPlayed(boolean characterCardAlreadyPlayed) {
        this.characterCardAlreadyPlayed = characterCardAlreadyPlayed;
    }

    /**
     * Finds the chosen card in the deck, sets the latestAssistantCardPlayed accordingly and removes it from the
     * player's deck. The game controller won't call this method if it finds out that the chosen card can't be played.
     *
     * @param cardName the name of the card the player has chosen to play.
     */

    public void playAssistantCard(String cardName) {
        AssistantCard chosenCard = getAssistantCardFromName(cardName);
        this.latestAssistantCardPlayed = chosenCard;
        getDeck().remove(chosenCard);
    }

    /**
     * Returns the {@link AssistantCard} in the player's deck whose name is identical to the one provided as a
     * parameter.
     *
     * @param cardName the Assistant Card's name.
     * @return the desired card.
     */

    public AssistantCard getAssistantCardFromName(String cardName){
        Optional <AssistantCard> assistantCard  =
                getDeck().stream().filter(card -> card.getName().equals(cardName)).findFirst();
        return assistantCard.orElse(null);
    }

    /**
     * Moves a student of the chosen color to the chosen island, if possible.
     *
     * @param island the island to move the student to.
     * @param color the color of the student to move.
     * @throws StudentNotFoundException if a student of the chosen color does not exist in the player's hall.
     */

    public void moveStudent(Island island, String color) throws StudentNotFoundException {
        this.school.moveStudentToIsland(island, color);
    }

    /**
     * Moves a student of the chosen color to the proper table in the player's {@link School}.
     *
     * @param color the color of the student to move.
     * @throws StudentNotFoundException if a student of the chosen color does not exist in the player's hall.
     * @throws NonExistentColorException if a non-existent color is somehow provided (view input controls should
     * prevent this exception to be thrown).
     * @throws FullTableException if the table of the chosen color is full.
     */

    public void moveStudent(String color) throws
            StudentNotFoundException, NonExistentColorException, FullTableException {
        this.school.moveStudentToTable(this, color);
    }

    /**
     * Allows the view to properly show the player's school.
     */

    public void showSchool(){

        System.out.println(getNickname() + "'s school:");

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

        System.out.println("Hall: " +
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

        System.out.println("Students per table: " +
                ANSIConstants.ANSI_YELLOW + yellowStudents + ANSIConstants.ANSI_RESET + " " +
                ANSIConstants.ANSI_BLUE + blueStudents + ANSIConstants.ANSI_RESET + " " +
                ANSIConstants.ANSI_GREEN + greenStudents + ANSIConstants.ANSI_RESET + " " +
                ANSIConstants.ANSI_RED + redStudents + ANSIConstants.ANSI_RESET + " " +
                ANSIConstants.ANSI_PINK + pinkStudents + ANSIConstants.ANSI_RESET);

        System.out.print("Professors: ");
        String yellowProf = "no";
        String blueProf = "no";
        String greenProf = "no";
        String redProf = "no";
        String pinkProf = "no";
        try {

            if (this.getSchool().getTable("YELLOW").getHasProfessor())
                yellowProf = "yes";

            if (this.getSchool().getTable("BLUE").getHasProfessor())
                blueProf = "yes";

            if (this.getSchool().getTable("GREEN").getHasProfessor())
                greenProf = "yes";

            if (this.getSchool().getTable("RED").getHasProfessor())
                redProf = "yes";

            if (this.getSchool().getTable("PINK").getHasProfessor())
                pinkProf = "yes";

        } catch(NonExistentColorException ignored){}

        System.out.println(
                ANSIConstants.ANSI_YELLOW + yellowProf + ANSIConstants.ANSI_RESET + " " +
                ANSIConstants.ANSI_BLUE + blueProf + ANSIConstants.ANSI_RESET + " " +
                ANSIConstants.ANSI_GREEN + greenProf + ANSIConstants.ANSI_RESET + " " +
                ANSIConstants.ANSI_RED + redProf + ANSIConstants.ANSI_RESET + " " +
                ANSIConstants.ANSI_PINK + pinkProf + ANSIConstants.ANSI_RESET);

        System.out.println("Towers: " + this.getSchool().getTowerRoom().getTowersLeft());
        System.out.println("--------------------");
    }

}
