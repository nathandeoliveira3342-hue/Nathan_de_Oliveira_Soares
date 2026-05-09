package com.decidetogether.model

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Modelo de dados representando uma decisão registrada no histórico.
 *
 * Criado ao encerrar uma votação, armazena a pergunta, o vencedor
 * e os metadados da decisão para exibição no painel de histórico.
 *
 * @property question Pergunta original da votação
 * @property winnerOption Texto da opção vencedora
 * @property winnerPercentage Percentual de votos da opção vencedora
 * @property totalVotes Total de votos registrados na votação
 * @property timestamp Timestamp Unix de quando a decisão foi registrada
 */
data class DecisionRecord(
    val question: String,
    val winnerOption: String,
    val winnerPercentage: Int,
    val totalVotes: Int,
    val timestamp: Long = System.currentTimeMillis()
) {
    /**
     * Retorna o horário formatado da decisão (HH:mm).
     */
    fun getFormattedTime(): String {
        val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
        return sdf.format(Date(timestamp))
    }

    /**
     * Retorna a data e hora completa formatada.
     */
    fun getFormattedDateTime(): String {
        val sdf = SimpleDateFormat("dd/MM HH:mm", Locale.getDefault())
        return sdf.format(Date(timestamp))
    }
}
