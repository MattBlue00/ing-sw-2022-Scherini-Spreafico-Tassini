package it.polimi.ingsw.network.message;

/**
 * This {@link Message} is used to communicate which Character Card requesting a String parameter has been chosen,
 * along with the given parameters.
 */

public class CharacterCardMessageString extends CharacterCardMessage {

    private final String par;

    /**
     * Default constructor.
     *
     * @param nickname client's nickname.
     * @param cardID the chosen card id.
     * @param par the String to send.
     */

    public CharacterCardMessageString(String nickname, int cardID, String par) {
        super(nickname, cardID);
        this.par = par;
    }

    /**
     * Returns the given parameter.
     *
     * @return the given parameter as a {@link String}.
     */

    public String getPar() {
        return par;
    }
}
