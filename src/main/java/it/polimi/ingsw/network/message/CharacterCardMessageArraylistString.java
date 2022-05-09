package it.polimi.ingsw.network.message;

import java.util.List;

public class CharacterCardMessageArraylistString extends CharacterCardMessage{

    List<String> par;

    public CharacterCardMessageArraylistString(String nickname, int cardID,  List<String> par) {
        super(nickname, cardID);
        this.par = par;
    }

    public List<String> getPar() {
        return par;
    }
}
