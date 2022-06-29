package it.polimi.ingsw.network.message;

import java.util.List;

/**
 * This {@link Message} is used to communicate which Character Card requesting an ArrayList parameter has been chosen,
 * along with the given parameters.
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

    /**
     * Returns a list containing the given parameters.
     *
     * @return a list containing the given parameters.
     */

    public List<String> getPar() {
        return par;
    }
}