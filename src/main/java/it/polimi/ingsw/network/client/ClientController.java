package it.polimi.ingsw.network.client;

import it.polimi.ingsw.network.message.LoginReply;
import it.polimi.ingsw.network.message.LoginRequest;
import it.polimi.ingsw.network.message.Message;
import it.polimi.ingsw.network.message.PlayerLoginRequest;
import it.polimi.ingsw.observers.Observer;
import it.polimi.ingsw.observers.ViewObserver;
import it.polimi.ingsw.view.View;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ClientController implements ViewObserver, Observer<Message> {

    private final View view;

    private Client client;

    private final ExecutorService taskQueue;

    public static final int UNDO_TIME = 5000;

    public ClientController(View view) {
        this.view = view;
        taskQueue = Executors.newSingleThreadExecutor();
    }

    @Override
    public void onUpdateNickname(String nickname) {
        client.setNickname(nickname);
    }

    @Override
    public void onUpdateWizardID(String wizardID) {
        client.setWizardID(wizardID);
        client.sendMessage(new PlayerLoginRequest(client.getNickname(), client.getWizardID()));
    }

    @Override
    public void onUpdateServerInfo(Map<String, String> serverInfo) {
        try {
            client = new SocketClient(serverInfo.get("address"), Integer.parseInt(serverInfo.get("port")));
            client.addObserver(this);
            client.readMessage(); // Starts an asynchronous reading from the server.
            client.enablePinger(true);
            taskQueue.execute(view::askPlayerData);
        } catch (IOException e) {
            taskQueue.execute(() -> view.showLoginResult(false, false,
                    this.client.getNickname()));
        }
    }

    @Override
    public void update(Message message) {

        switch(message.getMessageType()){
            case LOGIN_REPLY:
                LoginReply loginReply = (LoginReply) message;
                taskQueue.execute(() -> view.showLoginResult(loginReply.isNicknameAccepted(),
                        loginReply.isConnectionSuccessful(), this.client.getNickname()));
                break;
            default:
                break;
        }

    }

    public static boolean isValidIpAddress(String ip) {
        String regex = "^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                "([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";
        return ip.matches(regex);
    }

    public static boolean isValidPort(String portStr) {
        try {
            int port = Integer.parseInt(portStr);
            return port >= 1 && port <= 65535;
        } catch (NumberFormatException e) {
            return false;
        }
    }

}
