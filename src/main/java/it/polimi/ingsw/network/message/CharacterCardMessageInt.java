package it.polimi.ingsw.network.message;

/**
 * This {@link Message} is used to communicate which Character Card requesting an {@code int} parameter has been
 * chosen, along with the given parameters.
 */

public class CharacterCardMessageInt extends CharacterCardMessage {

    private final int par;

    /**
     * Default constructor.
     *
     * @param nickname client's nickname.
     * @param cardID the chosen card id.
     * @param par the {@code int} to send.
     */

    public CharacterCardMessageInt(String nickname, int cardID, int par) {
        super(nickname, cardID);
        this.par = par;
    }

    /**
     * Returns the given parameter.
     *
     * @return an {@code int} representing the given parameter.
     */

    public int getPar() {
        return par;
    }
}
