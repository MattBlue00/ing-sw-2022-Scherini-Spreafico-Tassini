package it.polimi.ingsw.model;

public class CharacterCard {

    private final int id;
    private int cost;
    private CharacterCardStrategy strategy;
    private boolean isActive;

    public CharacterCard(int id, CharacterCardStrategy strategy) {
        this.id = id;
        this.strategy = strategy;
        this.isActive = false;
    }

    public int getId() {
        return id;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public CharacterCardStrategy getStrategy() {
        return strategy;
    }

    public boolean getIsActive(){
        return isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }
}