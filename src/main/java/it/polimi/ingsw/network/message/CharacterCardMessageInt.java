package it.polimi.ingsw.network.message;

public class CharacterCardMessageInt extends CharacterCardMessage {

    private int par;

    public CharacterCardMessageInt(String nickname, int cardID, int par) {
        super(nickname, cardID);
        this.par = par;
    }

    public int getPar() {
        return par;
    }
}
