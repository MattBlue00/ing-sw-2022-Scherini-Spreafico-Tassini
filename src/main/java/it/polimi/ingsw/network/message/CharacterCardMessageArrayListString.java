package it.polimi.ingsw.network.message;

import java.util.List;

public class CharacterCardMessageArrayListString extends CharacterCardMessage{

    private List<String> par;

    public CharacterCardMessageArrayListString(String nickname, int cardID, List<String> par) {
        super(nickname, cardID);
        this.par = par;
    }

    public List<String> getPar() {
        return par;
    }
}
