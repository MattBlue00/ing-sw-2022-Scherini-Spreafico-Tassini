package it.polimi.ingsw.network.message;

public class CharacterCardMessageString extends CharacterCardMessage {

    String par;

    public CharacterCardMessageString(String nickname, int cardID, String par) {
        super(nickname, cardID);
        this.par = par;
    }

    public String getPar() {
        return par;
    }
}
