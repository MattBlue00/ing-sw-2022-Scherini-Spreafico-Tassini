package it.polimi.ingsw.network.message;

/**
 * This enum contains all the message type available and used by the server and clients.
 */
public enum MessageType {
    LOGIN_REQUEST,
    JOIN_GAME,
    CREATE_GAME,
    PLAYER_NUMBER_REPLY,
    ASSISTANT_CARD_REPLY,
    MOTHER_NATURE_STEPS_REPLY,
    CLOUD_CHOICE_REPLY,
    MOVE_TO_TABLE_REPLY,
    MOVE_TO_ISLAND_REPLY,
    CHARACTER_CARD_REPLY,
    WIZARD_ID,
    ERROR_MESSAGE,
    GENERIC,
    ASK_TYPE,
    GAME_STATUS_FIRST_ACTION_PHASE,
    GAME_STATUS,
    SHOW_DECK_MESSAGE,
    ACTION_CHOICE,
    EXISTING_GAMES,
    DISCONNECTION,
    UPDATE,
    GAME_PHASE,
    END_GAME
}
