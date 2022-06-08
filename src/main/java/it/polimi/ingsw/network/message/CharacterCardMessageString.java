package it.polimi.ingsw.network.message;


/**
 * The message extends {@link CharacterCardMessage}.
 * It is used when the chosen card has a String as parameter.
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

    public String getPar() {
        return par;
    }
}
