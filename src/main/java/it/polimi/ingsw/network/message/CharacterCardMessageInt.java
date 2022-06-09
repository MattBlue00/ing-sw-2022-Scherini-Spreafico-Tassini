package it.polimi.ingsw.network.message;

/**
 * The message extends {@link CharacterCardMessage}.
 * It is used when the chosen card has an Integer as parameter.
 */
public class CharacterCardMessageInt extends CharacterCardMessage {

    private final int par;

    /**
     * Default constructor.
     *
     * @param nickname client's nickname.
     * @param cardID the chosen card id.
     * @param par the integer to send.
     */
    public CharacterCardMessageInt(String nickname, int cardID, int par) {
        super(nickname, cardID);
        this.par = par;
    }

    public int getPar() {
        return par;
    }
}
