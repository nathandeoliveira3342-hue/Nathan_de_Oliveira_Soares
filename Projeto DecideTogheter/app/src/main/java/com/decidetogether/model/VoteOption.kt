package com.decidetogether.model

/**
 * Modelo de dados representando uma opção de votação.
 *
 * @property id Identificador único da opção
 * @property text Texto descritivo da opção
 * @property votes Número de votos recebidos
 */
data class VoteOption(
    val id: Int,
    val text: String,
    var votes: Int = 0
) {
    /**
     * Calcula o percentual de votos desta opção em relação ao total.
     *
     * @param totalVotes Total de votos na votação
     * @return Percentual de 0 a 100
     */
    fun getPercentage(totalVotes: Int): Int {
        if (totalVotes == 0) return 0
        return ((votes.toFloat() / totalVotes) * 100).toInt()
    }
}
