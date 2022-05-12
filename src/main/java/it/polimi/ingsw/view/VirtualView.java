package it.polimi.ingsw.view;

import it.polimi.ingsw.network.message.LoginRequest;
import it.polimi.ingsw.network.server.ClientHandler;
import it.polimi.ingsw.observers.Observer;

public class VirtualView implements View, Observer {

    private final ClientHandler clientHandler;

    public VirtualView(ClientHandler clientHandler) {
        this.clientHandler = clientHandler;
    }

    public ClientHandler getClientHandler() {
        return clientHandler;
    }

    @Override
    public void askNickname() {
    }

    @Override
    public void askCreateOrJoin() {

    }

    @Override
    public void askGameInfo() {

    }

    @Override
    public void askWizardID() {

    }

    @Override
    public void update(Object message) {

    }

}
