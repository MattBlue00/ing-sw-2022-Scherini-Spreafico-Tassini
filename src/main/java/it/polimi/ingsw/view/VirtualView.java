package it.polimi.ingsw.view;

import it.polimi.ingsw.network.message.ErrorMessage;
import it.polimi.ingsw.network.message.LoginReply;
import it.polimi.ingsw.network.message.Message;
import it.polimi.ingsw.network.message.PlayerLoginRequest;
import it.polimi.ingsw.network.server.ClientHandler;
import it.polimi.ingsw.network.server.Server;
import it.polimi.ingsw.observers.Observer;

public class VirtualView implements View, Observer<Message> {

    private final ClientHandler clientHandler;

    public VirtualView(ClientHandler clientHandler) {
        this.clientHandler = clientHandler;
    }

    // Getter method

    public ClientHandler getClientHandler() {
        return clientHandler;
    }

    // VirtualView methods

    @Override
    public void askPlayerData(){
        clientHandler.sendMessage(new LoginReply(false, true));
    }


    @Override
    public void showLoginResult(boolean nicknameAccepted, boolean connectionSuccessful, String nickname) {
        clientHandler.sendMessage(new LoginReply(nicknameAccepted, connectionSuccessful));
    }

    @Override
    public void showErrorAndExit(String error) {
        clientHandler.sendMessage(new ErrorMessage(Server.NICKNAME, error));
    }

    @Override
    public void update(Message message) {
        clientHandler.sendMessage(message);
    }
}
