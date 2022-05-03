package it.polimi.ingsw.network.message;

public class GameModeMessage extends Message{

        private final boolean gameExpertMode;

        public GameModeMessage(String nickname, boolean gameExpertMode) {
            super(nickname, MessageType.GAME_MODE_REPLY);
            this.gameExpertMode = gameExpertMode;
        }

        public boolean isGameExpertMode() {
            return gameExpertMode;
        }
    }
