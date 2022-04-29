package it.polimi.ingsw.network.message;

public class CharacterCardReplyString extends CharacterCardReply{

    String par;

    public CharacterCardReplyString(String nickname, int cardID, String par) {
        super(nickname, cardID);
        this.par = par;
    }

    public String getPar() {
        return par;
    }
}
