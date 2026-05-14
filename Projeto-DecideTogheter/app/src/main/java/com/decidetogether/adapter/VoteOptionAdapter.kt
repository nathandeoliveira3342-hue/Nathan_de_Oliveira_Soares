package com.decidetogether.adapter

import android.animation.ObjectAnimator
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.decidetogether.R
import com.decidetogether.databinding.ItemVoteOptionBinding
import com.decidetogether.model.VoteOption

/**
 * Adapter para o RecyclerView de opções de votação.
 *
 * Exibe cada opção com:
 * - Texto da opção
 * - Barra de progresso animada com o percentual de votos
 * - Percentual numérico
 * - Contagem de votos
 * - Ícone de vencedor (opção com mais votos)
 * - Ícone de check (opção votada pelo usuário)
 * - Estado selecionado quando o usuário já votou
 *
 * @param onOptionClick Callback chamado quando o usuário clica em uma opção
 */
class VoteOptionAdapter(
    private val onOptionClick: (VoteOption) -> Unit
) : RecyclerView.Adapter<VoteOptionAdapter.VoteOptionViewHolder>() {

    private var options: List<VoteOption> = emptyList()
    private var userVotedOption: String? = null
    private var totalVotes: Int = 0

    /**
     * Atualiza a lista de opções e o estado do voto do usuário.
     *
     * @param newOptions Lista atualizada de opções
     * @param votedOption Texto da opção votada pelo usuário (null se não votou)
     */
    fun submitOptions(newOptions: List<VoteOption>, votedOption: String?) {
        options = newOptions
        userVotedOption = votedOption
        totalVotes = newOptions.sumOf { it.votes }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VoteOptionViewHolder {
        val binding = ItemVoteOptionBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return VoteOptionViewHolder(binding)
    }

    override fun onBindViewHolder(holder: VoteOptionViewHolder, position: Int) {
        holder.bind(options[position])
    }

    override fun getItemCount(): Int = options.size

    inner class VoteOptionViewHolder(
        private val binding: ItemVoteOptionBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(option: VoteOption) {
            val context = binding.root.context
            val percentage = option.getPercentage(totalVotes)

            // Texto da opção
            binding.tvOptionText.text = option.text

            // Percentual e contagem de votos
            binding.tvPercentage.text = "$percentage%"
            binding.tvVoteCount.text = "(${option.votes})"

            // Determina se esta opção é a vencedora (mais votos)
            val isWinner = totalVotes > 0 &&
                    options.maxByOrNull { it.votes }?.id == option.id &&
                    option.votes > 0

            // Ícone de vencedor
            binding.ivWinner.visibility = if (isWinner) View.VISIBLE else View.GONE

            // Ícone de voto do usuário
            val isUserVote = userVotedOption == option.text
            binding.ivUserVote.visibility = if (isUserVote) View.VISIBLE else View.GONE

            // Estado selecionado da view
            binding.root.isSelected = isUserVote

            // Cor do percentual: destaque para o vencedor
            if (isWinner) {
                binding.tvPercentage.setTextColor(
                    ContextCompat.getColor(context, R.color.vote_winner)
                )
                binding.progressVotes.progressDrawable =
                    ContextCompat.getDrawable(context, R.drawable.vote_progress_winner)
            } else {
                binding.tvPercentage.setTextColor(
                    ContextCompat.getColor(context, R.color.text_secondary)
                )
                binding.progressVotes.progressDrawable =
                    ContextCompat.getDrawable(context, R.drawable.vote_progress_bg)
            }

            // Animação da barra de progresso
            val animator = ObjectAnimator.ofInt(binding.progressVotes, "progress", 0, percentage)
            animator.duration = 600
            animator.interpolator = DecelerateInterpolator()
            animator.start()

            // Listener de clique — só permite votar se o usuário ainda não votou
            binding.root.setOnClickListener {
                if (userVotedOption == null) {
                    onOptionClick(option)
                }
            }

            // Feedback visual: cursor de clique apenas se não votou
            binding.root.isClickable = userVotedOption == null
        }
    }
}
