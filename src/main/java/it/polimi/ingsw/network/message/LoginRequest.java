package it.polimi.ingsw.network.message;

/**
 * The message extends {@link Message}.
 * Message used by the client to request a login to the server.
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
