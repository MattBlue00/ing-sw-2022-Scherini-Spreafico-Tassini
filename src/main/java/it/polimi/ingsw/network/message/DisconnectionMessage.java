package it.polimi.ingsw.network.message;

public class DisconnectionMessage extends Message{

    /*
     * Message to notify a disconnection to the other players.
     */
        private static final long serialVersionUID = -5422965079989607600L;
        private final String messageStr;

        public DisconnectionMessage(String messageStr) {
            super(null, MessageType.DISCONNECTION);
            this.messageStr = messageStr;
        }

        public String getMessageStr() {
            return messageStr;
        }
    }
