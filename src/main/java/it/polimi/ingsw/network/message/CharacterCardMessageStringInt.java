package it.polimi.ingsw.network.message;

/**
 * This {@link Message} is used to communicate which Character Card requesting a {@link String} and an {@code int}
 * parameter has been chosen, along with the given parameters.
 */

public class CharacterCardMessageStringInt extends CharacterCardMessage{

    private final String par1;
    private final int par2;

    /**
     * Default constructor.
     *
     * @param nickname client's nickname.
     * @param cardID the chosen card id.
     * @param par1 the String to send.
     * @param par2 the {@code int} to send.
     */

    public CharacterCardMessageStringInt(String nickname, int cardID, String par1, int par2) {
        super(nickname, cardID);
        this.par1 = par1;
        this.par2 = par2;
    }

    /**
     * Returns the first given parameter.
     *
     * @return the first given parameter as a {@link String}
     */

    public String getPar1() {
        return par1;
    }

    /**
     * Returns the second given parameter.
     *
     * @return an {@code int} representing the second given parameter.
     */

    public int getPar2() {
        return par2;
    }
}
