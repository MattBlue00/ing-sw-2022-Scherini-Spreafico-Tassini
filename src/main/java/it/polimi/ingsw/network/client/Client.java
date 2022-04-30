package it.polimi.ingsw.network.client;

import it.polimi.ingsw.model.Wizard;
import it.polimi.ingsw.network.message.Message;
import it.polimi.ingsw.observers.Observable;

import java.util.logging.Logger;

public abstract class Client extends Observable<Message> {

    public static final Logger LOGGER = Logger.getLogger(Client.class.getName());

    private String nickname;
    private String wizardID;

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getWizardID() {
        return wizardID;
    }

    public void setWizardID(String wizardID) {
        this.wizardID = wizardID;
    }

    /*
            Sends a message to the server.
         */
    public abstract void sendMessage(Message message);

    /*
        Asynchronously reads a message from the server and notifies the ClientController.
     */
    public abstract void readMessage();

    /*
        Disconnects from the server.
     */
    public abstract void disconnect();

    public abstract void enablePinger(boolean enabled);

}
