package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.*;
import it.polimi.ingsw.utils.ANSIConstants;
import it.polimi.ingsw.utils.Constants;

import java.io.Serializable;
import java.util.*;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.controller.GameControllerExpertMode;

/**
 * This class is the core of the game model, as it holds all the values that describe its status. Each game is
 * controlled by a {@link GameController}, which triggers all the status changes (note that this class acts as some
 * sort of "interface" between the "physical" elements of the game and the controller). This class contains all the
 * necessary values to play a Normal mode match. For Expert mode matches, see the {@link GameExpertMode} model and
 * the related {@link GameControllerExpertMode}.
 */

public class Game implements Serializable {

    private int playersNumber;
    private List<Player> players;
    private int roundNumber;
    private final GameBoard board;
    private Player currentPlayer;
    private int maxSteps;
    private final Constants constants;
    private final Map<Player, TowerColor> towersColor;

    /**
     * Game constructor.
     *
     * @param playersNumber the number of players of the match.
     * @param constants the useful constants to set for this game.
     */

    public Game(int playersNumber, Constants constants) {
        this.constants = constants;
        this.playersNumber = playersNumber;
        this.players = new ArrayList<>();
        this.board = new GameBoard(playersNumber, constants);
        this.roundNumber = 1;
        this.towersColor = new HashMap<>();
    }

    /**
     * Returns the game's number of players.
     *
     * @return an {@code int} representing the number of players.
     */

    public int getPlayersNumber() {
        return playersNumber;
    }

    /**
     * Returns a list containing the players participating in the game.
     *
     * @return a list of the players participating in the game.
     */

    public List<Player> getPlayers() {
        return players;
    }

    /**
     * Sets the list of players participating in the game.
     *
     * @param players the new list of players participating in the game.
     */

    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    /**
     * Returns the player currently playing.
     *
     * @return the {@link Player} currently playing.
     */

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    /**
     * Sets the new current player.
     *
     * @param currentPlayer the new current {@link Player}.
     */

    public void setCurrentPlayer(Player currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    /**
     * Returns the maximum number of Mother Nature steps allowed by the current player's last Assistant Card played.
     *
     * @return an {@code int} representing the maximum number of Mother Nature steps allowed by the current player's
     * last Assistant Card played.
     */

    public int getMaxSteps() {
        return maxSteps;
    }

    /**
     * Sets the new maximum number of Mother Nature steps allowed by the current player's last Assistant Card played.
     *
     * @param maxSteps the new number of steps.
     */

    public void setMaxSteps(int maxSteps) {
        this.maxSteps = maxSteps;
    }

    /**
     * Returns the game board.
     *
     * @return the game's {@link GameBoard}.
     */

    public GameBoard getBoard() {
        return board;
    }

    /**
     * Sets the game's number of players
     *
     * @param playersNumber an {@code int} representing the new number of players.
     */

    public void setPlayersNumber(int playersNumber) {
        this.playersNumber = playersNumber;
    }

    /**
     * Returns the current game round number.
     *
     * @return an {@code int} representing the current round number.
     */

    public int getRoundNumber() {
        return roundNumber;
    }

    /**
     * Sets the new game round number.
     *
     * @param roundNumber an {@code int} representing the new round number.
     */

    public void setRoundNumber(int roundNumber) {
        this.roundNumber = roundNumber;
    }

    /**
     * Returns the set of {@link Constants} specific to this game.
     *
     * @return the constants unique to this game's players' number.
     */

    public Constants getConstants(){
        return constants;
    }

    /**
     * Returns the {@code towersColor} {@link HashMap}, used to associate a {@link TowerColor} to a player
     *
     * @return {@code towersColor}
     */

    public Map<Player, TowerColor> getTowersColor() { return towersColor; }

    /**
     * Calls the {@link GameBoard} method which refills the cloud with the bag students, once each of them is empty.
     *
     * @throws EmptyBagException if no student is present in the students' bag.
     */

    public void refillClouds() throws EmptyBagException {
        board.refillClouds();
    }

    /**
     * Calls the {@link Player} method which takes a student from their hall and moves it onto the specified island.
     *
     * @param color the color of the student to move.
     * @param islandID the ID of the island to move the student to.
     * @throws IslandNotFoundException if the provided ID does not correspond to any of the existing islands.
     * @throws StudentNotFoundException if the provided color does not match to any of the player's hall students'
     * colors.
     */

    public void playerMovesStudent(String color, int islandID) throws IslandNotFoundException, StudentNotFoundException {
            currentPlayer.moveStudent(board.getIslands().getIslandFromID(islandID), color);
    }

    /**
     * Calls the {@link Player} method which takes a student from their hall and moves it to the correct table.
     *
     * @param color the color of the student to move.
     * @throws FullTableException if the table of the provided color is full.
     * @throws StudentNotFoundException if the provided color does not match to any of the player's hall students'
     * colors.
     * @throws NonExistentColorException if the player somehow manages to provide a non-existent color as a parameter.
     */

    public void playerMovesStudent(String color) throws FullTableException, StudentNotFoundException,
            NonExistentColorException {
        currentPlayer.moveStudent(color);
        profCheck();
    }

    /**
     * Makes overriding possible in {@link GameExpertMode}. If the game is played in Normal mode, calls the
     * profCheckAlgorithm method.
     *
     * @throws NonExistentColorException if the method somehow tries to access to tables of non-existing colors (it
     * should never happen, so it is safely ignorable).
     */

    public void profCheck() throws NonExistentColorException{
        profCheckAlgorithm(getPlayers());
    }

    /**
     * Reassigns the professors if the necessary conditions are reached.
     *
     * @param players the list of the playing players whose tables need to be checked.
     * @throws NonExistentColorException if the method somehow tries to access to tables of non-existing colors (it
     * should never happen, so it is safely ignorable).
     */

    public static void profCheckAlgorithm(List<Player> players) throws NonExistentColorException{

        Color[] colors = Color.values();

        for (Color color : colors) {

            int dimBiggestTable = 0;   // number of students to beat in order to claim a professor
            int playerWithProf = -1;    // index of the player who currently has this color's professor
            int playerWhoLostProf = -1; // index of the player whose prof has been claimed

            // true if this color's professor was already claimed by a player during one of the previous rounds
            boolean professorAssigned = false;

            // for each color, stores how many students each player has
            int[] numOfStudents = new int[players.size()];

            for (int i = 0; i < players.size(); i++) {
                numOfStudents[i] =
                        players.get(i).getSchool().getTable(color.toString()).getNumOfStudents();
                if (players.get(i).getSchool().getTable(color.toString()).getHasProfessor()) {
                    playerWithProf = i;
                    professorAssigned = true;
                }
            }

            // if this color's professor was already assigned, profCheck rules vary
            if (professorAssigned) {
                dimBiggestTable = numOfStudents[playerWithProf];
                if(dimBiggestTable == 0){
                    players.get(playerWithProf).getSchool().getTable(color.toString()).setHasProfessor(false);
                    professorAssigned = false;
                    playerWithProf = -1;
                }
            }

            for (int i = 0; i < players.size(); i++) {
                if (numOfStudents[i] > dimBiggestTable) {
                    // if clause needed to store which player's table will have its "hasProfessor" flag
                    // set to false, in case a player actually claimed that prof before
                    if (professorAssigned)
                        playerWhoLostProf = playerWithProf;
                    dimBiggestTable = numOfStudents[i];
                    playerWithProf = i;
                }
            }

            // sets the "hasProfessor" flags accordingly
            if(playerWithProf != -1)
                players.get(playerWithProf).getSchool().getTable(color.toString()).setHasProfessor(true);

            // if the professor was already assigned and its owner did change
            if (playerWhoLostProf != -1)
                players.get(playerWhoLostProf).getSchool().getTable(color.toString()).setHasProfessor(false);

        }

    }

    /**
     * Allows Mother Nature to move of the given steps, if possible.
     *
     * @param steps the desired number of steps.
     * @throws InvalidNumberOfStepsException if the provided number of steps violates the game's rules.
     */

    public void moveMotherNature(int steps) throws InvalidNumberOfStepsException {

        int max_steps = currentPlayer.getLatestAssistantCardPlayed().getMotherNatureSteps();

        if(steps > max_steps || steps < Constants.MIN_NUM_OF_STEPS)
            throw new InvalidNumberOfStepsException("You can't move Mother Nature of " + steps + " steps! Maximum allowed: " + max_steps);

        board.moveMotherNature(steps);

    }

    /**
     * Triggers the chain of methods which decides whether the island where Mother Nature is will be conquered by
     * the current player.
     *
     * @param islandID the ID of the island to check.
     * @throws IslandNotFoundException if the provided ID does not correspond to any of the existing islands.
     */

    public void islandConquerCheck(int islandID) throws IslandNotFoundException {
        board.islandConquerCheck(currentPlayer, islandID);
    }

    /**
     * Triggers the chain of methods that moves all the students from the chosen cloud into the hall of the current
     * player, if possible.
     *
     * @param cloudID the ID of the chosen cloud.
     * @throws EmptyCloudException if the chosen cloud is empty (which means it has already been chosen on the same
     * round).
     */

    public void takeStudentsFromCloud(int cloudID) throws EmptyCloudException {
        board.takeStudentsFromCloud(cloudID, currentPlayer);
    }

    /**
     * Fills the players' halls with the right amount of students and shuffles the players' order at the beginning of
     * the match, in order to start the game properly.
     */

    public void startGame(){
        for(int i = 0; i < constants.MAX_HALL_STUDENTS; i++) {
            for (Player player : players) {
                player.getSchool().getHall().addStudent(board.getStudentsBag().remove
                        (board.getStudentsBag().size() - 1));
            }
        }

        Collections.shuffle(players);
        currentPlayer = players.get(0);

        towersColor.put(players.get(0), TowerColor.BLACK);
        towersColor.put(players.get(1), TowerColor.WHITE);
        if(players.size() == 3)
            towersColor.put(players.get(2), TowerColor.GREY);
    }

    /**
     * Adds a {@link Player} to the game.
     *
     * @param player the player to add.
     */

    public void addPlayer(Player player){
        players.add(player);
    }

    /**
     * Allows the CLI to properly show the game status (Normal mode).
     */

    public void showGameBoard(){

        try {

            String nextPlayerNickname;
            if(players.size() - 1 == players.indexOf(currentPlayer))
                nextPlayerNickname = players.get(0).getNickname();
            else
                nextPlayerNickname = players.get(players.indexOf(currentPlayer) + 1).getNickname();
            System.out.printf("ROUND: %d\tCURRENT PLAYER: %s\tNEXT PLAYER: %s\n",
                    roundNumber, currentPlayer.getNickname(), nextPlayerNickname);
            System.out.println("--------------------");

            int yellowStudents, blueStudents, greenStudents, redStudents, pinkStudents;

            for (int i = 1; i <= getBoard().getIslands().getSize(); i++) {

                Island currentIsland = getBoard().getIslands().getIslandFromID(i);

                String ownerNickname;
                if (currentIsland.getOwner() != null)
                    ownerNickname = currentIsland.getOwner().getNickname();
                else
                    ownerNickname = "--";

                System.out.println("ISLAND " + i);
                System.out.println(currentIsland.getNumOfTowers() + " towers");
                System.out.println("Owner: " + ownerNickname);

                yellowStudents = currentIsland.getNumOfStudentsOfColor("YELLOW");
                blueStudents = currentIsland.getNumOfStudentsOfColor("BLUE");
                greenStudents = currentIsland.getNumOfStudentsOfColor("GREEN");
                redStudents = currentIsland.getNumOfStudentsOfColor("RED");
                pinkStudents = currentIsland.getNumOfStudentsOfColor("PINK");

                System.out.println("Students: " +
                        ANSIConstants.ANSI_YELLOW + yellowStudents + ANSIConstants.ANSI_RESET + " " +
                        ANSIConstants.ANSI_BLUE + blueStudents + ANSIConstants.ANSI_RESET + " " +
                        ANSIConstants.ANSI_GREEN + greenStudents + ANSIConstants.ANSI_RESET + " " +
                        ANSIConstants.ANSI_RED + redStudents + ANSIConstants.ANSI_RESET + " " +
                        ANSIConstants.ANSI_PINK + pinkStudents + ANSIConstants.ANSI_RESET);

                if(currentIsland.getId() == getBoard().getMotherNaturePos())
                    System.out.println(ANSIConstants.ANSI_BOLD + "Mother Nature is here!" + ANSIConstants.ANSI_RESET);

                System.out.println("--------------------");

            }

            for(int i = 0; i < playersNumber; i++) {
                yellowStudents =
                        (int) getBoard().getCloud(i).getStudents().stream().filter(x -> x.color().equals(Color.YELLOW)).count();
                blueStudents =
                        (int) getBoard().getCloud(i).getStudents().stream().filter(x -> x.color().equals(Color.BLUE)).count();
                greenStudents =
                        (int) getBoard().getCloud(i).getStudents().stream().filter(x -> x.color().equals(Color.GREEN)).count();
                redStudents =
                        (int) getBoard().getCloud(i).getStudents().stream().filter(x -> x.color().equals(Color.RED)).count();
                pinkStudents =
                        (int) getBoard().getCloud(i).getStudents().stream().filter(x -> x.color().equals(Color.PINK)).count();

                System.out.print("CLOUD " + (i + 1) + ": ");
                System.out.println(
                        ANSIConstants.ANSI_YELLOW + yellowStudents + ANSIConstants.ANSI_RESET + " " +
                                ANSIConstants.ANSI_BLUE + blueStudents + ANSIConstants.ANSI_RESET + " " +
                                ANSIConstants.ANSI_GREEN + greenStudents + ANSIConstants.ANSI_RESET + " " +
                                ANSIConstants.ANSI_RED + redStudents + ANSIConstants.ANSI_RESET + " " +
                                ANSIConstants.ANSI_PINK + pinkStudents + ANSIConstants.ANSI_RESET);
            }

            System.out.println("--------------------");

            // Show all schools
            players.forEach(Player::showSchool);

        }
        catch(IslandNotFoundException ignored){}

    }

    /**
     * Allows the CLI to properly show the current player's Assistant Card deck.
     */

    public void showDeck(){
        List<AssistantCard> deck = this.currentPlayer.getDeck();
        System.out.println("Assistant cards in the deck: ");
        for(AssistantCard assistantCard : deck){
            System.out.print(assistantCard.getName() + " card, Mother Nature steps: " +
                    assistantCard.getMotherNatureSteps() + ", card's weight: " + assistantCard.getWeight() + "\n");
        }
    }

    /**
     * Allows the CLI to properly show the Assistant Cards played by the players at the end of a Planning Phase.
     */

    public void showAssistantCardsPlayed(){
        players.forEach(p -> System.out.println(p.getNickname() + " has played: "
                + p.getLatestAssistantCardPlayed().getName() +
                " (Mother Nature steps: " + p.getLatestAssistantCardPlayed().getMotherNatureSteps() +
                ", Weight: " + p.getLatestAssistantCardPlayed().getWeight() + ")"));
        System.out.println("--------------------");
    }

    /**
     * Allows the CLI to properly show the players' order at the beginning of an Action Phase.
     */

    public void showPlayersOrder(){
        System.out.println("The players' order is: ");
        players.forEach(p -> System.out.println(p.getNickname()));
        System.out.println("--------------------");
    }

    /**
     * Returns the player of the given nickname.
     *
     * @param nick the given nickname.
     * @return the {@link Player} of the given nickname.
     */

    public Player getPlayerFromNickname(String nick){
        Player p = null;
        for (Player player : getPlayers()){
            if(player.getNickname().equals(nick))
                p = player;
        }
        return p;
    }
}
