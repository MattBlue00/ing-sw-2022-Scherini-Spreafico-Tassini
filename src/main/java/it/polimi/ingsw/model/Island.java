package it.polimi.ingsw.model;

import java.util.ArrayList;

public class Island {

    private Player owner;
    private final int id;
    private int numOfTowers;
    private ArrayList<Student> students;


    public Island(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public Player getOwner() {
        return owner;
    }

    public void setOwner(Player owner) {
        this.owner = owner;
    }


    // TODO: island methods
    public int influenceCalc(){
        return 0;
    }
}
