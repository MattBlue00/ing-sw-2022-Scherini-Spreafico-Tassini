package it.polimi.ingsw.controller;

import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.network.message.LoginRequest;
import it.polimi.ingsw.network.message.Message;
import it.polimi.ingsw.observers.Observer;
import it.polimi.ingsw.observers.ViewObserver;
import it.polimi.ingsw.view.View;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ClientController implements ViewObserver, Observer {

    private final View view;

    private Client client;
    private String nickname;

    private final ExecutorService taskQueue;

    public ClientController(View view, Client client) {
        this.view = view;
        this.taskQueue = Executors.newSingleThreadExecutor();
        this.client = client;
    }

    @Override
    public void onUpdateNickname(String nickname) {
        this.nickname = nickname;
        client.sendMessage(new LoginRequest(this.nickname));
    }

    @Override
    public void update(Object message) {

    }
}
