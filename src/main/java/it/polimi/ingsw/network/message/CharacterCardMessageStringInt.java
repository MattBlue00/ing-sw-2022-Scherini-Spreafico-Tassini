package it.polimi.ingsw.network.message;

public class CharacterCardMessageStringInt extends CharacterCardMessage{

    private String par1;
    private int par2;

    public CharacterCardMessageStringInt(String nickname, int cardID, String par1, int par2) {
        super(nickname, cardID);
        this.par1 = par1;
        this.par2 = par2;
    }

    public String getPar1() {
        return par1;
    }

    public int getPar2() {
        return par2;
    }
}
