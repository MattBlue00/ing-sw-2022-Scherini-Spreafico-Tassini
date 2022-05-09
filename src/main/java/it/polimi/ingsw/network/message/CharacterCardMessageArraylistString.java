package it.polimi.ingsw.network.message;

import java.util.List;

public class CharacterCardMessageArraylistString extends Message{

    List<String> par;

    public CharacterCardMessageArraylistString(String nickname, MessageType messageType, List<String> par) {
        super(nickname, MessageType.CHARACTER_CARD_REPLY);
        this.par = par;
    }
}
