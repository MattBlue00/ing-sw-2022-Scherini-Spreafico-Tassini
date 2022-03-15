package it.polimi.ingsw.model;

public class CharacterCard {
    private int cost;
    private final int id;

    public CharacterCard(int id) {
        this.id = id;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public int getId() {
        return id;
    }
}
