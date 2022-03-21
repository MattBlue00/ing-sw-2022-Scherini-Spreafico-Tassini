package it.polimi.ingsw.model;

import it.polimi.ingsw.model.exceptions.EmptyBagException;
import it.polimi.ingsw.model.exceptions.EmptyCloudException;
import it.polimi.ingsw.model.exceptions.NonExistentTableException;

public class Game {

    // game variables

    private final int playersNumber;
    private GameState state;
    private final Player players[];
    private int roundNumber;
    private GameBoard board;
    private Player currentPlayer;
    private final int CHARACTER_NUM;
    private final CharacterCard characters[];
    private final int PLAYER_MOVES; // defines how many students you can normally move in a turn from your hall
    private final int NUM_COLORS = 5;

    public Game(int playersNumber) {
        this.state = GameState.INIT;
        this.CHARACTER_NUM = 3;
        this.playersNumber = playersNumber;
        players = new Player[playersNumber];
        characters = new CharacterCard[CHARACTER_NUM];
        board = new GameBoard(playersNumber);
        this.PLAYER_MOVES = 3;
    }

    //Getter and setter section

    public int getPlayersNumber() {
        return playersNumber;
    }

    public Player[] getPlayers() {
        return players;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(Player currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    // methods
    /*
        this method starts the chain of event that take a certain number of students, equal to the max number of students
        the cloud can have, from the studentBag and puts them on the clouds, one at a time
     */
    public void refillClouds() throws EmptyBagException {
        board.refillClouds();
    }

    public void playersPlayAssistantCard(){}

    /*

     */
    public void playerMovesStudent(){
        for(int i=0; i<PLAYER_MOVES; i++){
            currentPlayer.moveStudent();
        }
        profCheck();
    }

    public void profCheck(){

        boolean hasProfessor[] = new boolean[NUM_COLORS];   // contains which professor each player has
        for(int i = 0; i < NUM_COLORS; i++){		// init
            hasProfessor[i] = false;
        }

        Color colors[] = Color.values();

        try {
            for (Color color : colors) {

                int dimBiggestTable = -1;   // number of students to beat in order to claim a professor
                int playerWithProf = -1;    // index of the player who currently has this color's professor
                int playerWhoLostProf = -1; // index of the player whose prof has been claimed

                // true if this color's professor was already claimed by a player during one of the previous rounds
                boolean professorAssigned = false;

                // for each color, stores how many students each player has
                int numOfStudents[] = new int[playersNumber];

                for (int i = 0; i < playersNumber; i++) {
                    numOfStudents[i] =
                            players[i].getSchool().getTable(color.toString()).getNumOfStudents();
                    if (players[i].getSchool().getTable(color.toString()).getHasProfessor()) {
                        playerWithProf = i;
                        professorAssigned = true;
                    }
                }

                // if this color's professor was already assigned, profCheck rules vary
                if (professorAssigned) {
                    dimBiggestTable = numOfStudents[playerWithProf];
                }

                for (int i = 0; i < playersNumber; i++) {
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
                players[playerWithProf].getSchool().getTable(color.toString()).setHasProfessor(true);
                // if the professor was already assigned and its owner did change
                if (professorAssigned && playerWhoLostProf != -1) {
                    players[playerWhoLostProf].getSchool().getTable(color.toString()).setHasProfessor(false);
                }
            }
        }
        catch(NonExistentTableException e){
            e.printStackTrace();
        }
    }

    public void playerPlaysCharacterCard(){
        currentPlayer.playCharacterCard();
    }

    public void moveMothernature(){}

    public void islandConquerCheck(){}

    public void mergeCheck(){} // may be a Gameboard method called inside the conquerCheck chain of methods

    /*
        this method starts the chain of event that take all the students from
        a chosen cloud and put them into the hall of the current player
     */
    public void takeStudentsFromCloud(int cloudID) throws EmptyCloudException {
        board.takeStudentsFromCloud(cloudID, currentPlayer);
    }


    // For debugging

    public GameBoard getBoard() {
        return board;
    }

    //for debugging
    // adding players to the game
    //implemented for two players
    public void addPlayer(Player player){
        if(this.players[0] == null){
            this.players[0] = player;
        }
        else {
            this.players[1] = player;
        }
    }
}
