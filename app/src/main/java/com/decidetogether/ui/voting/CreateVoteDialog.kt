package com.decidetogether.ui.voting

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import com.decidetogether.R
import com.decidetogether.databinding.DialogCreateVoteBinding
import com.decidetogether.databinding.ItemVoteOptionInputBinding
import com.decidetogether.model.Vote
import com.decidetogether.model.VoteOption
import com.decidetogether.util.Constants

/**
 * Dialog para criação de uma nova votação.
 *
 * Permite ao usuário:
 * - Inserir a pergunta da votação
 * - Adicionar entre 2 e 6 opções de resposta
 * - Ativar um timer opcional (30s, 60s ou 2min)
 *
 * Ao confirmar, valida os campos e chama o callback [onVoteCreated]
 * com o objeto [Vote] criado.
 *
 * @param context Contexto da Activity pai
 * @param onVoteCreated Callback chamado com o Vote criado ao confirmar
 */
class CreateVoteDialog(
    context: Context,
    private val onVoteCreated: (Vote) -> Unit
) : Dialog(context, R.style.Theme_DecideTogether) {

    private lateinit var binding: DialogCreateVoteBinding

    // Lista de bindings dos campos de opção (para leitura dos valores)
    private val optionBindings = mutableListOf<ItemVoteOptionInputBinding>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DialogCreateVoteBinding.inflate(LayoutInflater.from(context))
        setContentView(binding.root)

        // Configura o dialog para ocupar a largura total da tela
        window?.apply {
            setLayout(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT
            )
            setBackgroundDrawableResource(android.R.color.transparent)
        }

        setupInitialOptions()
        setupClickListeners()
    }

    /**
     * Adiciona as 2 opções iniciais obrigatórias ao formulário.
     */
    private fun setupInitialOptions() {
        addOptionField(removable = false)
        addOptionField(removable = false)
    }

    /**
     * Configura os listeners de clique nos botões do dialog.
     */
    private fun setupClickListeners() {
        // Botão: Adicionar opção
        binding.btnAddOption.setOnClickListener {
            if (optionBindings.size < Constants.MAX_VOTE_OPTIONS) {
                addOptionField(removable = true)
            } else {
                Toast.makeText(
                    context,
                    "Máximo de ${Constants.MAX_VOTE_OPTIONS} opções",
                    Toast.LENGTH_SHORT
                ).show()
            }

            // Esconde o botão quando atingir o máximo
            binding.btnAddOption.visibility =
                if (optionBindings.size >= Constants.MAX_VOTE_OPTIONS) View.GONE else View.VISIBLE
        }

        // Switch do timer
        binding.switchTimer.setOnCheckedChangeListener { _, isChecked ->
            binding.layoutTimerDuration.visibility = if (isChecked) View.VISIBLE else View.GONE
        }

        // Botão: Cancelar
        binding.btnCancelVote.setOnClickListener {
            dismiss()
        }

        // Botão: Iniciar Votação
        binding.btnStartVote.setOnClickListener {
            attemptCreateVote()
        }
    }

    /**
     * Adiciona um campo de texto para uma nova opção ao formulário.
     *
     * @param removable Se true, exibe o botão de remover esta opção
     */
    private fun addOptionField(removable: Boolean) {
        val optionBinding = ItemVoteOptionInputBinding.inflate(
            LayoutInflater.from(context), binding.layoutOptions, false
        )

        val optionIndex = optionBindings.size + 1
        optionBinding.tilOption.hint = context.getString(R.string.hint_option, optionIndex)

        // Exibe o botão de remover apenas para opções adicionais
        optionBinding.btnRemoveOption.visibility = if (removable) View.VISIBLE else View.GONE

        // Listener do botão de remover
        optionBinding.btnRemoveOption.setOnClickListener {
            binding.layoutOptions.removeView(optionBinding.root)
            optionBindings.remove(optionBinding)
            // Reabilita o botão de adicionar
            binding.btnAddOption.visibility = View.VISIBLE
            // Atualiza os hints das opções restantes
            updateOptionHints()
        }

        binding.layoutOptions.addView(optionBinding.root)
        optionBindings.add(optionBinding)
    }

    /**
     * Atualiza os hints dos campos de opção após remoção.
     */
    private fun updateOptionHints() {
        optionBindings.forEachIndexed { index, optionBinding ->
            optionBinding.tilOption.hint = context.getString(R.string.hint_option, index + 1)
        }
    }

    /**
     * Valida os campos e cria a votação se tudo estiver correto.
     */
    private fun attemptCreateVote() {
        val question = binding.etQuestion.text?.toString()?.trim() ?: ""

        // Valida a pergunta
        if (question.isEmpty()) {
            binding.tilQuestion.error = "Por favor, insira a pergunta"
            binding.etQuestion.requestFocus()
            return
        }
        binding.tilQuestion.error = null

        // Coleta e valida as opções
        val optionTexts = mutableListOf<String>()
        var hasError = false

        for ((index, optionBinding) in optionBindings.withIndex()) {
            val text = optionBinding.etOption.text?.toString()?.trim() ?: ""
            if (text.isEmpty()) {
                optionBinding.tilOption.error = "Preencha esta opção"
                if (!hasError) {
                    optionBinding.etOption.requestFocus()
                    hasError = true
                }
            } else {
                optionBinding.tilOption.error = null
                optionTexts.add(text)
            }
        }

        if (hasError) return

        // Verifica se há pelo menos 2 opções preenchidas
        if (optionTexts.size < Constants.MIN_VOTE_OPTIONS) {
            Toast.makeText(context, "Adicione pelo menos 2 opções", Toast.LENGTH_SHORT).show()
            return
        }

        // Verifica opções duplicadas
        if (optionTexts.distinct().size != optionTexts.size) {
            Toast.makeText(context, "Remova as opções duplicadas", Toast.LENGTH_SHORT).show()
            return
        }

        // Determina a duração do timer
        val timerSeconds = if (binding.switchTimer.isChecked) {
            when (binding.rgTimerDuration.checkedRadioButtonId) {
                R.id.rb30s -> 30
                R.id.rb120s -> 120
                else -> 60 // padrão: 60 segundos
            }
        } else {
            0 // sem timer
        }

        // Cria o objeto Vote
        val vote = Vote(
            question = question,
            options = optionTexts.mapIndexed { index, text ->
                VoteOption(id = index, text = text)
            }.toMutableList(),
            timerSeconds = timerSeconds
        )

        // Fecha o dialog e notifica o callback
        dismiss()
        onVoteCreated(vote)
    }
}
