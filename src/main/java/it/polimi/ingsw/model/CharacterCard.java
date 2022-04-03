package it.polimi.ingsw.model;

public abstract class CharacterCard {

    private final int id;
    private int cost;
    private boolean isActive;

    public CharacterCard(int id, int initialCost) {
        this.id = id;
        this.cost = initialCost;
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

    public boolean getIsActive(){
        return isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }

    public abstract void doEffect(GameExpertMode game);

}