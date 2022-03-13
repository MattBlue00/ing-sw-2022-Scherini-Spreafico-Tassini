package it.polimi.ingsw.model;

public class Island {
    private boolean hasMotherNature;
    private Player isOwned;
    private final int islandID;
    private int numOfTowers;


    public Island(int islandID) {
        this.islandID = islandID;
    }

    public void setIsOwned(Player isOwned) {
        this.isOwned = isOwned;
    }

    public Player getIsOwned() {
        return isOwned;
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
