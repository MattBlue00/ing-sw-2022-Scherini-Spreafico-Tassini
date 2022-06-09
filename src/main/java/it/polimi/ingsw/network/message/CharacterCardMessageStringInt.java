package it.polimi.ingsw.network.message;


/**
 * The message extends {@link CharacterCardMessage}.
 * It is used when the chosen card has a String and an Integer as parameters.
 */
public class CharacterCardMessageStringInt extends CharacterCardMessage{

    private final String par1;
    private final int par2;

    /**
     * Default constructor.
     *
     * @param nickname client's nickname.
     * @param cardID the chosen card id.
     * @param par1 String to send.
     * @param par2 the integer to send.
     */
    public CharacterCardMessageStringInt(String nickname, int cardID, String par1, int par2) {
        super(nickname, cardID);
        this.par1 = par1;
        this.par2 = par2;
    }

    public String getPar1() {
        return par1;
    }

    public int getPar2() {
        return par2;
    }
}
