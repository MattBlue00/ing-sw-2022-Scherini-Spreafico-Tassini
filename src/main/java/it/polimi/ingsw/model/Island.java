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

    public int getNumOfTowers() {
        return numOfTowers;
    }

    public void setNumOfTowers(int numOfTowers) {
        this.numOfTowers = numOfTowers;
    }

    public void ownCheck(){}

    public void conquerCheck(){}

    public void mergeCheck(){}
}
