package it.polimi.ingsw.network.message;

public class CharacterCardReplyInt extends CharacterCardReply{

    int par;

    public CharacterCardReplyInt(String nickname, int cardID, int par) {
        super(nickname, cardID);
        this.par = par;
    }

    public int getPar() {
        return par;
    }
}
