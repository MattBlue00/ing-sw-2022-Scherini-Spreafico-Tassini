package it.polimi.ingsw.network.message;

/**
 * This {@link Message} is used to communicate to the server the nickname chosen by the client, in order to enter the
 * lobby.
 */

public class LoginRequest extends Message{

    /**
     * Default constructor.
     *
     * @param nickname client's nickname.
     */

    public LoginRequest(String nickname) {
        super(nickname, MessageType.LOGIN_REQUEST);
    }
}
