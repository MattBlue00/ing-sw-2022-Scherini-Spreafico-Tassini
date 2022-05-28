package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.*;
import it.polimi.ingsw.utils.ANSIConstants;
import it.polimi.ingsw.utils.Constants;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Game implements Serializable {

    // game variables

    private int playersNumber;
    private List<Player> players;
    private int roundNumber;
    private final GameBoard board;
    private Player currentPlayer;
    private int maxSteps;
    private final Constants constants;

    public Game(int playersNumber, Constants constants) {
        this.constants = constants;
        this.playersNumber = playersNumber;
        this.players = new ArrayList<>();
        this.board = new GameBoard(playersNumber, constants);
        this.roundNumber = 1;
    }

    // Getter and setter methods

    public int getPlayersNumber() {
        return playersNumber;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(Player currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public int getMaxSteps() {
        return maxSteps;
    }

    public void setMaxSteps(int maxSteps) {
        this.maxSteps = maxSteps;
    }

    public GameBoard getBoard() {
        return board;
    }

    public void setPlayersNumber(int playersNumber) {
        this.playersNumber = playersNumber;
    }

    public int getRoundNumber() {
        return roundNumber;
    }

    public void setRoundNumber(int roundNumber) {
        this.roundNumber = roundNumber;
    }

    public Constants getConstants(){
        return constants;
    }

    // Game methods

    /*
        This method starts the chain of events that take a certain number of students, equal to the max number of students
        the cloud can have, from the studentBag and puts them on the clouds, one at a time
     */
    public void refillClouds() throws EmptyBagException {
        board.refillClouds();
    }

    /*
        A player can move 3 students from their Halls each turn
        At the end of the movement phase profCheck() is called
     */
    public void playerMovesStudent(String color, int islandID) throws IslandNotFoundException, StudentNotFoundException {
            currentPlayer.moveStudent(board.getIslands().getIslandFromID(islandID), color);
    }

    public void playerMovesStudent(String color) throws FullTableException, StudentNotFoundException,
            NonExistentColorException {
        currentPlayer.moveStudent(color);
        profCheck();
    }

    /*
    ----THIS METHOD IS NEEDED FOR THE THIEF CARD THAT WE DID NOT IMPLEMENT YET----
    public void playerMovesStudentToHall(String color){
        try{
            currentPlayer.moveStudentToHall(color);
        }catch (NonExistentColorException e){
            e.printStackTrace();
        }
    }*/

    /*
        This method is needed to make overriding possible in GameExpertMode.
        If the game is played with the basic rules, it just calls the profCheckAlgorithm method.
     */
    public void profCheck() throws NonExistentColorException{
        profCheckAlgorithm(getPlayers());
    }

    /*
        This method reassigns the professors if the necessary conditions are reached.
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

    /*
        This method allows Mother Nature to move of up to the given steps.
     */
    public void moveMotherNature(int steps) throws InvalidNumberOfStepsException, IslandNotFoundException {

        int max_steps = currentPlayer.getLastAssistantCardPlayed().getMotherNatureSteps();

        if(steps > max_steps || steps < Constants.MIN_NUM_OF_STEPS)
            throw new InvalidNumberOfStepsException("You can't move Mother Nature of " + steps + " steps! Maximum allowed: " + max_steps);

        board.moveMotherNature(steps);

    }

    /*
        This method triggers the chain of methods which decides whether the island where Mother Nature is will be
        conquered by a player.
     */
    public void islandConquerCheck(int islandID) throws IslandNotFoundException {
        board.islandConquerCheck(currentPlayer, islandID);
    }

    /*
        This method starts the chain of event that take all the students from
        a chosen cloud and put them into the hall of the current player
     */
    public void takeStudentsFromCloud(int cloudID) throws EmptyCloudException {
        board.takeStudentsFromCloud(cloudID, currentPlayer);
    }

    public void startGame(){
        for(int i = 0; i < constants.MAX_HALL_STUDENTS; i++) {
            for (Player player : players) {
                player.getSchool().getHall().addStudent(board.getStudentsBag().remove
                        (board.getStudentsBag().size() - 1));
            }
        }

        Collections.shuffle(players);
        currentPlayer = players.get(0);
    }

    // Debug methods

    public void addPlayer(Player player){
        players.add(player);
    }

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

                yellowStudents =
                        (int) currentIsland.getStudents().stream().filter(x -> x.getColor().equals(Color.YELLOW)).count();
                blueStudents =
                        (int) currentIsland.getStudents().stream().filter(x -> x.getColor().equals(Color.BLUE)).count();
                greenStudents =
                        (int) currentIsland.getStudents().stream().filter(x -> x.getColor().equals(Color.GREEN)).count();
                redStudents =
                        (int) currentIsland.getStudents().stream().filter(x -> x.getColor().equals(Color.RED)).count();
                pinkStudents =
                        (int) currentIsland.getStudents().stream().filter(x -> x.getColor().equals(Color.PINK)).count();

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
                        (int) getBoard().getCloud(i).getStudents().stream().filter(x -> x.getColor().equals(Color.YELLOW)).count();
                blueStudents =
                        (int) getBoard().getCloud(i).getStudents().stream().filter(x -> x.getColor().equals(Color.BLUE)).count();
                greenStudents =
                        (int) getBoard().getCloud(i).getStudents().stream().filter(x -> x.getColor().equals(Color.GREEN)).count();
                redStudents =
                        (int) getBoard().getCloud(i).getStudents().stream().filter(x -> x.getColor().equals(Color.RED)).count();
                pinkStudents =
                        (int) getBoard().getCloud(i).getStudents().stream().filter(x -> x.getColor().equals(Color.PINK)).count();

                //TODO: when the cloud number is asked, remember to save it decremented of 1!
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

    public void showDeck(){
        List<AssistantCard> deck = this.currentPlayer.getDeck();
        System.out.println("Assistant cards in the deck: ");
        for(AssistantCard assistantCard : deck){
            System.out.print(assistantCard.getName() + " card, Mother Nature steps: " +
                    assistantCard.getMotherNatureSteps() + ", card's weight: " + assistantCard.getWeight() + "\n");
        }
    }

    public void showAssistantCardsPlayed(){
        players.forEach(p -> System.out.println(p.getNickname() + " has played: "
                + p.getLastAssistantCardPlayed().getName() +
                " (Mother Nature steps: " + p.getLastAssistantCardPlayed().getMotherNatureSteps() +
                ", Weight: " + p.getLastAssistantCardPlayed().getWeight() + ")"));
        System.out.println("--------------------");
    }

    public void showPlayersOrder(){
        System.out.println("The players' order is: ");
        players.forEach(p -> System.out.println(p.getNickname()));
        System.out.println("--------------------");
    }

}
