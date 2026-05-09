package com.decidetogether.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.decidetogether.databinding.ItemDecisionHistoryBinding
import com.decidetogether.model.DecisionRecord

/**
 * Adapter para o RecyclerView de histórico de decisões.
 *
 * Utiliza ListAdapter com DiffUtil para atualizações eficientes da lista.
 * Cada item exibe a pergunta, o vencedor com percentual e o horário da decisão.
 */
class DecisionHistoryAdapter :
    ListAdapter<DecisionRecord, DecisionHistoryAdapter.HistoryViewHolder>(DecisionDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val binding = ItemDecisionHistoryBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return HistoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class HistoryViewHolder(
        private val binding: ItemDecisionHistoryBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(record: DecisionRecord) {
            // Pergunta da votação
            binding.tvHistoryQuestion.text = record.question

            // Vencedor com percentual
            binding.tvHistoryWinner.text = "${record.winnerOption} (${record.winnerPercentage}%)"

            // Horário da decisão
            binding.tvHistoryTime.text = record.getFormattedTime()
        }
    }

    /**
     * DiffCallback para comparação eficiente de itens do histórico.
     */
    private class DecisionDiffCallback : DiffUtil.ItemCallback<DecisionRecord>() {
        override fun areItemsTheSame(oldItem: DecisionRecord, newItem: DecisionRecord): Boolean {
            return oldItem.timestamp == newItem.timestamp
        }

        override fun areContentsTheSame(oldItem: DecisionRecord, newItem: DecisionRecord): Boolean {
            return oldItem == newItem
        }
    }
}
