package com.decidetogether.model

/**
 * Modelo de dados representando uma votação completa.
 *
 * @property id Identificador único da votação (timestamp de criação)
 * @property question Pergunta da votação
 * @property options Lista de opções disponíveis para votação
 * @property timerSeconds Duração do timer em segundos (0 = sem timer)
 * @property isActive Indica se a votação está em andamento
 * @property totalVotes Contador total de votos registrados
 * @property createdAt Timestamp de criação da votação
 */
data class Vote(
    val id: Long = System.currentTimeMillis(),
    val question: String,
    val options: MutableList<VoteOption>,
    val timerSeconds: Int = 0,
    var isActive: Boolean = true,
    var totalVotes: Int = 0,
    val createdAt: Long = System.currentTimeMillis()
)
