package it.polimi.ingsw.model;

public class CharacterCard {

    private final int id;
    private int cost;
    private CharacterCardStrategy strategy;

    public CharacterCard(int id, CharacterCardStrategy strategy) {
        this.id = id;
        this.strategy = strategy;
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
}
