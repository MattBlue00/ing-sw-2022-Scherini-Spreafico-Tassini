package it.polimi.ingsw.utils;

import java.io.Serializable;

public class Constants implements Serializable {

    public static final int CHARACTERS_NUM = 3;
    public static final int NUM_COLORS = 5;
    public static final int NUM_TABLES = 5;
    public static final int TABLE_LENGTH = 10;
    public static final int COINS_AVAILABLE_PER_TABLE = 3;
    public static final int OFFSET_COINS = 3;
    public static final int MIN_NUM_OF_STEPS = 1;
    public static final int MAX_NUM_OF_ISLANDS = 12;
    public static final int STUDENTS_PER_COLOR = 26;

    public int MAX_HALL_STUDENTS;
    public int MAX_TOWERS;
    public int MAX_CLOUD_STUDENTS;
    public int PLAYER_MOVES;

    public Constants(int playersNumber){
        if(playersNumber == 2){
            MAX_HALL_STUDENTS = 7;
            MAX_TOWERS = 8;
            MAX_CLOUD_STUDENTS = 3;
            PLAYER_MOVES = 3;
        }
        else{
            MAX_HALL_STUDENTS = 9;
            MAX_TOWERS = 6;
            MAX_CLOUD_STUDENTS = 4;
            PLAYER_MOVES = 4;
        }
    }

}
