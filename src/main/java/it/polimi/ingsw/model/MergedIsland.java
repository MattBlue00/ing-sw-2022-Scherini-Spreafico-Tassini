package it.polimi.ingsw.model;

public class MergedIsland extends Island{

    public MergedIsland(int id) {
        super(id);
    }

    @Override
    public int influenceCalc(Player currentPlayer) {
        return super.influenceCalc(currentPlayer);
    }
}
