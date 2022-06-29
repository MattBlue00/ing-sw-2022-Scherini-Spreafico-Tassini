package it.polimi.ingsw.network.message;

import it.polimi.ingsw.network.server.Server;

/**
 * This enumeration contains the values that represent what the {@link Server} may be asking for.
 */
public enum AskType {
    NICKNAME_NOT_UNIQUE,
    GAME_ID,
    WIZARD_ID,
    ASSISTANT_CARD,
    MOVE_STUDENT,
    MOVE_MOTHER_NATURE,
    CLOUD_CHOICE,
    CHARACTER_CARD,
    ACTION_CHOICE
}
