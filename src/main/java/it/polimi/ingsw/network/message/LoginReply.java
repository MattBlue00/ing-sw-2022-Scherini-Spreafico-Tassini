package it.polimi.ingsw.network.message;

import it.polimi.ingsw.network.server.Server;

public class LoginReply extends Message{

    private final boolean nicknameAccepted;
    private final boolean connectionSuccessful;

    public LoginReply(boolean nicknameAccepted, boolean connectionSuccessful) {
        super(Server.NICKNAME, MessageType.LOGIN_REPLY);
        this.nicknameAccepted = nicknameAccepted;
        this.connectionSuccessful = connectionSuccessful;
    }

    public boolean isNicknameAccepted() {
        return nicknameAccepted;
    }

    public boolean isConnectionSuccessful() {
        return connectionSuccessful;
    }

}
