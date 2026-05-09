package com.decidetogether.util

/**
 * Constantes globais do aplicativo DecideTogether.
 * Centraliza todas as chaves de Intent extras, modos e configurações.
 */
object Constants {

    // -------------------------------------------------------------------------
    // Modos da RoomActivity
    // -------------------------------------------------------------------------

    /** Modo para criar uma nova sala */
    const val ROOM_MODE_CREATE = "CREATE"

    /** Modo para entrar em uma sala existente */
    const val ROOM_MODE_JOIN = "JOIN"

    // -------------------------------------------------------------------------
    // Chaves de Intent Extras
    // -------------------------------------------------------------------------

    /** Extra: modo da sala (CREATE ou JOIN) */
    const val EXTRA_ROOM_MODE = "extra_room_mode"

    /** Extra: nome do usuário participante */
    const val EXTRA_USERNAME = "extra_username"

    /** Extra: nome da sala Jitsi */
    const val EXTRA_ROOM_NAME = "extra_room_name"

    // -------------------------------------------------------------------------
    // Configurações do Jitsi Meet
    // -------------------------------------------------------------------------

    /**
     * URL base do servidor Jitsi Meet público.
     * Para usar um servidor privado, altere esta URL.
     */
    const val JITSI_SERVER_URL = "https://meet.jit.si"

    /**
     * Prefixo adicionado ao nome da sala para evitar conflitos com salas públicas.
     * Ex: "decidetogether_MinhaReuniao"
     */
    const val JITSI_ROOM_PREFIX = "decidetogether_"

    // -------------------------------------------------------------------------
    // Configurações de Votação
    // -------------------------------------------------------------------------

    /** Número máximo de opções por votação */
    const val MAX_VOTE_OPTIONS = 6

    /** Número mínimo de opções por votação */
    const val MIN_VOTE_OPTIONS = 2

    /** Duração padrão do timer de votação em segundos (0 = sem timer) */
    const val DEFAULT_VOTE_TIMER_SECONDS = 60

    /** Número máximo de caracteres na pergunta de votação */
    const val MAX_QUESTION_LENGTH = 120

    /** Número máximo de caracteres em cada opção de votação */
    const val MAX_OPTION_LENGTH = 50

    // -------------------------------------------------------------------------
    // Chaves de SharedPreferences
    // -------------------------------------------------------------------------

    const val PREFS_NAME = "decide_together_prefs"
    const val PREF_LAST_USERNAME = "last_username"
    const val PREF_LAST_ROOM = "last_room"

    // -------------------------------------------------------------------------
    // Tags de Log
    // -------------------------------------------------------------------------

    const val TAG_CONFERENCE = "ConferenceActivity"
    const val TAG_VOTING = "VotingSystem"
    const val TAG_JITSI = "JitsiMeet"
}
