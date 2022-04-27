package it.polimi.ingsw.network.message;

public class GameModeReply extends Message{

        private final boolean gameExpertMode;

        public GameModeReply(String nickname, boolean gameExpertMode) {
            super(nickname, MessageType.GAME_MODE_REPLY);
            this.gameExpertMode = gameExpertMode;
        }

        public boolean isGameExpertMode() {
            return gameExpertMode;
        }
    }
