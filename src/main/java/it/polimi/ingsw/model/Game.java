package it.polimi.ingsw.model;

import it.polimi.ingsw.model.exceptions.EmptyBagException;
import it.polimi.ingsw.model.exceptions.EmptyCloudException;

import java.util.ArrayList;
import java.util.List;

public class Game {

    // game variables

    private final int playersNumber;
    private boolean isFinished;
    private final Player players[];
    private int roundNumber;
    private Gameboard board;
    private Player currentPlayer;
    private final int CHARACTER_NUM;
    private final CharacterCard characters[];
    private final int PLAYER_MOVES; // defines how many students you can normally move in a turn from your hall


    public Game(int playersNumber) {
        this.CHARACTER_NUM = 3;
        this.playersNumber = playersNumber;
        players = new Player[playersNumber];
        characters = new CharacterCard[CHARACTER_NUM];
        board = new Gameboard(playersNumber);
        this.PLAYER_MOVES = 3;
    }

    //Getter and setter section

    public int getPlayersNumber() {
        return playersNumber;
    }

    public boolean isFinished() {
        return isFinished;
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
    }

    public void coinCheck(){} // coinCheck may be a Table method, need to discuss the solution

    public void profCheck(){} // similar to coinCheck

    public void playerPlaysCharacterCard(){}

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

    public Gameboard getBoard() {
        return board;
    }
}
