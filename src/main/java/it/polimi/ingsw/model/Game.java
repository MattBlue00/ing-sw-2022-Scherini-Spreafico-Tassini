package it.polimi.ingsw.model;

import it.polimi.ingsw.model.exceptions.*;

import java.util.ArrayList;
import java.util.List;

public class Game{


    // game variables

    private int playersNumber;
    private List<Player> players;
    private int roundNumber;
    private final GameBoard board;
    private Player currentPlayer;
    private int maxSteps;

    public Game(int playersNumber) {
        this.playersNumber = playersNumber;
        players = new ArrayList<>();
        board = new GameBoard(playersNumber);
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

    public int getRoundNumber() {
        return roundNumber;
    }

    public void setRoundNumber(int roundNumber) {
        this.roundNumber = roundNumber;
    }

    public GameBoard getBoard() {
        return board;
    }

    public void setPlayersNumber(int playersNumber) {
        this.playersNumber = playersNumber;
    }

    // Game methods

    /*
        This method starts the chain of events that take a certain number of students, equal to the max number of students
        the cloud can have, from the studentBag and puts them on the clouds, one at a time
     */
    public void refillClouds() throws EmptyBagException {
        board.refillClouds();
    }

    // TODO: check if the card has already been played
    public void playersPlayAssistantCard(int cardID) throws AssistantCardAlreadyPlayedException {
        players.forEach(player -> player.playAssistantCard(cardID));
    }

    /*
        A player can move 3 students from their Halls each turn
        At the end of the movement phase profCheck() is called
     */
    public void playerMovesStudent(){

        // TODO: the view will be responsible for choices

        try {
            for (int i = 0; i < Constants.PLAYER_MOVES; i++) {
                currentPlayer.moveStudent(1, Color.BLUE.toString()); // PLACEHOLDERS
                currentPlayer.moveStudent(Color.BLUE.toString()); // PLACEHOLDERS
                profCheck();
            }
        }
        catch(StudentNotFoundException | NonExistentColorException | FullTableException e){
            e.printStackTrace();
        }

    }

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
    public void moveMotherNature(int steps) throws InvalidNumberOfStepsException {

        int max_steps = currentPlayer.getLastAssistantCardPlayed().getMotherNatureSteps();

        if(steps > max_steps || steps < Constants.MIN_NUM_OF_STEPS)
            throw new InvalidNumberOfStepsException("The number of steps selected is not valid.");

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

    // Debug methods

    public void addPlayer(Player player){
        players.add(player);
    }

}
