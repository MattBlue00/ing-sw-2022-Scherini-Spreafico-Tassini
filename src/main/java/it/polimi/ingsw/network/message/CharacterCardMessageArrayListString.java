package it.polimi.ingsw.network.message;

import java.util.List;

/**
 * The message extends {@link CharacterCardMessage}.
 * It is used when the chosen card has a list of String as parameter
 * (Jester Card).
 */
public class CharacterCardMessageArrayListString extends CharacterCardMessage{

    private final List<String> par;

    /**
     * Default constructor.
     *
     * @param nickname client's nickname.
     * @param cardID the chosen card id.
     * @param par the list of Strings to send.
     */
    public CharacterCardMessageArrayListString(String nickname, int cardID, List<String> par) {
        super(nickname, cardID);
        this.par = par;
    }

    public List<String> getPar() {
        return par;
    }
}