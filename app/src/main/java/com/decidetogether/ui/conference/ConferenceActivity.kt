package com.decidetogether.ui.conference

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.AnimationUtils
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.decidetogether.R
import com.decidetogether.adapter.DecisionHistoryAdapter
import com.decidetogether.adapter.VoteOptionAdapter
import com.decidetogether.databinding.ActivityConferenceBinding
import com.decidetogether.model.DecisionRecord
import com.decidetogether.model.Vote
import com.decidetogether.model.VoteOption
import com.decidetogether.ui.voting.CreateVoteDialog
import com.decidetogether.util.Constants
import org.jitsi.meet.sdk.JitsiMeet
import org.jitsi.meet.sdk.JitsiMeetActivity
import org.jitsi.meet.sdk.JitsiMeetConferenceOptions
import java.net.URL

/**
 * Tela 4: Videoconferência + Sistema de Votação
 *
 * Responsabilidades:
 * 1. Inicializar e exibir a videoconferência via Jitsi Meet SDK
 * 2. Gerenciar o ciclo de vida das votações (criar, votar, encerrar)
 * 3. Manter e exibir o histórico de decisões da sessão
 * 4. Controlar os overlays de votação e histórico sobre o vídeo
 *
 * Fluxo:
 * - Ao criar: solicita permissões de câmera/microfone → inicia Jitsi → exibe controles
 * - Botão "Nova Votação": abre CreateVoteDialog → inicia votação ativa
 * - Botão "Encerrar Votação": finaliza votação, registra no histórico
 * - Botão "Histórico": exibe painel com todas as decisões da sessão
 * - Botão "Sair": confirma e encerra a chamada
 */
class ConferenceActivity : AppCompatActivity() {

    private lateinit var binding: ActivityConferenceBinding

    // Dados recebidos da RoomActivity
    private var username: String = ""
    private var roomName: String = ""
    private var roomMode: String = Constants.ROOM_MODE_CREATE

    // Estado da votação atual
    private var currentVote: Vote? = null
    private var userVotedOption: String? = null

    // Histórico de decisões da sessão
    private val decisionHistory = mutableListOf<DecisionRecord>()

    // Adapters dos RecyclerViews
    private lateinit var voteOptionAdapter: VoteOptionAdapter
    private lateinit var historyAdapter: DecisionHistoryAdapter

    // Launcher para solicitar múltiplas permissões
    private val permissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val cameraGranted = permissions[Manifest.permission.CAMERA] ?: false
        val micGranted = permissions[Manifest.permission.RECORD_AUDIO] ?: false

        if (cameraGranted && micGranted) {
            Log.d(Constants.TAG_CONFERENCE, "Permissões concedidas — iniciando Jitsi")
            startJitsiConference()
        } else {
            Log.w(Constants.TAG_CONFERENCE, "Permissões negadas — câmera: $cameraGranted, mic: $micGranted")
            showPermissionDeniedMessage()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityConferenceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Recupera os dados passados pela RoomActivity
        username = intent.getStringExtra(Constants.EXTRA_USERNAME) ?: "Participante"
        roomName = intent.getStringExtra(Constants.EXTRA_ROOM_NAME) ?: "DecideTogether"
        roomMode = intent.getStringExtra(Constants.EXTRA_ROOM_MODE) ?: Constants.ROOM_MODE_CREATE

        Log.d(Constants.TAG_CONFERENCE, "Iniciando conferência — sala: $roomName, usuário: $username")

        setupUI()
        setupRecyclerViews()
        setupClickListeners()
        checkPermissionsAndStart()
    }

    // =========================================================================
    // Configuração da Interface
    // =========================================================================

    /**
     * Configura elementos estáticos da interface.
     */
    private fun setupUI() {
        // Exibe o nome da sala no indicador superior
        binding.tvRoomName.text = "Sala: $roomName"

        // Inicialmente, o botão de encerrar votação fica oculto
        binding.btnEndVote.visibility = View.GONE
        binding.cardVotingPanel.visibility = View.GONE
        binding.cardHistoryPanel.visibility = View.GONE
    }

    /**
     * Configura os RecyclerViews de opções de votação e histórico.
     */
    private fun setupRecyclerViews() {
        // RecyclerView de opções de votação
        voteOptionAdapter = VoteOptionAdapter(
            onOptionClick = { option -> castVote(option) }
        )
        binding.rvVoteOptions.apply {
            layoutManager = LinearLayoutManager(this@ConferenceActivity)
            adapter = voteOptionAdapter
            isNestedScrollingEnabled = false
        }

        // RecyclerView do histórico de decisões
        historyAdapter = DecisionHistoryAdapter()
        binding.rvDecisionHistory.apply {
            layoutManager = LinearLayoutManager(this@ConferenceActivity)
            adapter = historyAdapter
        }
    }

    /**
     * Configura os listeners de clique nos botões de controle.
     */
    private fun setupClickListeners() {
        // Botão: Nova Votação
        binding.btnNewVote.setOnClickListener {
            if (currentVote != null && currentVote!!.isActive) {
                showSnackbar("Já existe uma votação em andamento")
                return@setOnClickListener
            }
            showCreateVoteDialog()
        }

        // Botão: Encerrar Votação
        binding.btnEndVote.setOnClickListener {
            endCurrentVote()
        }

        // Botão: Histórico
        binding.btnHistory.setOnClickListener {
            toggleHistoryPanel()
        }

        // Botão: Fechar histórico
        binding.btnCloseHistory.setOnClickListener {
            hideHistoryPanel()
        }

        // Botão: Sair da chamada
        binding.btnLeaveCall.setOnClickListener {
            confirmLeaveCall()
        }
    }

    // =========================================================================
    // Permissões e Inicialização do Jitsi
    // =========================================================================

    /**
     * Verifica se as permissões necessárias estão concedidas.
     * Se sim, inicia o Jitsi. Se não, solicita ao usuário.
     */
    private fun checkPermissionsAndStart() {
        val cameraPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
        val micPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)

        if (cameraPermission == PackageManager.PERMISSION_GRANTED &&
            micPermission == PackageManager.PERMISSION_GRANTED) {
            startJitsiConference()
        } else {
            permissionLauncher.launch(
                arrayOf(Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO)
            )
        }
    }

    /**
     * Inicializa e inicia a videoconferência via Jitsi Meet SDK.
     *
     * O nome da sala é prefixado com "decidetogether_" para evitar conflitos
     * com salas públicas no servidor meet.jit.si.
     *
     * Configurações aplicadas:
     * - Servidor: meet.jit.si (público, sem necessidade de conta)
     * - Nome de exibição do usuário
     * - Sala com prefixo único do app
     * - Vídeo e áudio habilitados por padrão
     */
    private fun startJitsiConference() {
        try {
            val serverURL = URL(Constants.JITSI_SERVER_URL)

            // Configuração padrão do Jitsi (aplicada uma vez)
            val defaultOptions = JitsiMeetConferenceOptions.Builder()
                .setServerURL(serverURL)
                .build()
            JitsiMeet.setDefaultConferenceOptions(defaultOptions)

            // Configuração específica desta conferência
            val jitsiRoomName = "${Constants.JITSI_ROOM_PREFIX}${sanitizeRoomName(roomName)}"

            val conferenceOptions = JitsiMeetConferenceOptions.Builder()
                .setServerURL(serverURL)
                .setRoom(jitsiRoomName)
                .setAudioMuted(false)
                .setVideoMuted(false)
                .setAudioOnly(false)
                .setFeatureFlag("add-people.enabled", false)
                .setFeatureFlag("calendar.enabled", false)
                .setFeatureFlag("call-integration.enabled", false)
                .setFeatureFlag("chat.enabled", true)
                .setFeatureFlag("invite.enabled", false)
                .setFeatureFlag("live-streaming.enabled", false)
                .setFeatureFlag("meeting-name.enabled", true)
                .setFeatureFlag("meeting-password.enabled", false)
                .setFeatureFlag("pip.enabled", true)
                .setFeatureFlag("raise-hand.enabled", true)
                .setFeatureFlag("recording.enabled", false)
                .setFeatureFlag("tile-view.enabled", true)
                .setFeatureFlag("toolbox.alwaysVisible", false)
                .build()

            Log.d(Constants.TAG_JITSI, "Iniciando sala Jitsi: $jitsiRoomName")

            // Inicia a activity do Jitsi Meet
            JitsiMeetActivity.launch(this, conferenceOptions)

        } catch (e: Exception) {
            Log.e(Constants.TAG_JITSI, "Erro ao iniciar Jitsi: ${e.message}", e)
            showSnackbar("Erro ao iniciar videoconferência: ${e.message}")
        }
    }

    /**
     * Sanitiza o nome da sala para uso na URL do Jitsi.
     * Remove espaços e caracteres especiais.
     */
    private fun sanitizeRoomName(name: String): String {
        return name.trim()
            .replace(" ", "")
            .replace("[^a-zA-Z0-9_-]".toRegex(), "")
            .lowercase()
            .take(40)
    }

    /**
     * Exibe mensagem quando as permissões são negadas.
     */
    private fun showPermissionDeniedMessage() {
        AlertDialog.Builder(this)
            .setTitle("Permissões necessárias")
            .setMessage(
                "O DecideTogether precisa de acesso à câmera e microfone para " +
                "realizar videoconferências. Por favor, conceda as permissões nas " +
                "configurações do dispositivo."
            )
            .setPositiveButton("Tentar novamente") { _, _ ->
                checkPermissionsAndStart()
            }
            .setNegativeButton("Cancelar") { _, _ ->
                finish()
            }
            .setCancelable(false)
            .show()
    }

    // =========================================================================
    // Sistema de Votação
    // =========================================================================

    /**
     * Exibe o dialog para criar uma nova votação.
     */
    private fun showCreateVoteDialog() {
        val dialog = CreateVoteDialog(this) { vote ->
            startVoting(vote)
        }
        dialog.show()
    }

    /**
     * Inicia uma nova votação.
     *
     * @param vote Objeto Vote com pergunta, opções e configurações
     */
    private fun startVoting(vote: Vote) {
        currentVote = vote
        userVotedOption = null

        Log.d(Constants.TAG_VOTING, "Votação iniciada: ${vote.question}")

        // Atualiza a UI do painel de votação
        binding.tvVoteQuestion.text = vote.question
        binding.tvVoteStatus.visibility = View.GONE

        // Atualiza o adapter com as opções
        voteOptionAdapter.submitOptions(vote.options, null)

        // Exibe o painel de votação com animação
        showVotingPanel()

        // Exibe o botão de encerrar votação
        binding.btnEndVote.visibility = View.VISIBLE

        // Inicia o timer se configurado
        if (vote.timerSeconds > 0) {
            startVoteTimer(vote.timerSeconds)
        }
    }

    /**
     * Registra o voto do usuário em uma opção.
     *
     * @param option Opção escolhida pelo usuário
     */
    private fun castVote(option: VoteOption) {
        val vote = currentVote ?: return

        if (!vote.isActive) {
            showSnackbar("Esta votação já foi encerrada")
            return
        }

        if (userVotedOption != null) {
            showSnackbar("Você já votou nesta votação")
            return
        }

        // Registra o voto
        userVotedOption = option.text
        option.votes++
        vote.totalVotes++

        Log.d(Constants.TAG_VOTING, "Voto registrado em: ${option.text}")

        // Atualiza o adapter para refletir o novo estado
        voteOptionAdapter.submitOptions(vote.options, userVotedOption)

        // Exibe confirmação do voto
        binding.tvVoteStatus.text = getString(R.string.label_your_vote, option.text)
        binding.tvVoteStatus.visibility = View.VISIBLE

        showSnackbar("Voto registrado: ${option.text}")
    }

    /**
     * Encerra a votação atual e registra no histórico.
     */
    private fun endCurrentVote() {
        val vote = currentVote ?: return

        vote.isActive = false

        // Determina a opção vencedora
        val winner = vote.options.maxByOrNull { it.votes }

        if (winner != null && vote.totalVotes > 0) {
            val percentage = ((winner.votes.toFloat() / vote.totalVotes) * 100).toInt()

            // Registra no histórico
            val record = DecisionRecord(
                question = vote.question,
                winnerOption = winner.text,
                winnerPercentage = percentage,
                totalVotes = vote.totalVotes,
                timestamp = System.currentTimeMillis()
            )
            decisionHistory.add(0, record) // Adiciona no início (mais recente primeiro)
            historyAdapter.submitList(decisionHistory.toList())

            Log.d(Constants.TAG_VOTING, "Votação encerrada. Vencedor: ${winner.text} ($percentage%)")
            showSnackbar("Votação encerrada! Vencedor: ${winner.text} ($percentage%)")
        } else {
            showSnackbar("Votação encerrada sem votos")
        }

        // Limpa o estado da votação
        currentVote = null
        userVotedOption = null

        // Esconde o painel de votação e o botão de encerrar
        hideVotingPanel()
        binding.btnEndVote.visibility = View.GONE
        binding.layoutTimer.visibility = View.GONE
    }

    /**
     * Inicia o timer regressivo da votação.
     *
     * @param seconds Duração do timer em segundos
     */
    private fun startVoteTimer(seconds: Int) {
        binding.layoutTimer.visibility = View.VISIBLE
        binding.tvTimer.text = "${seconds}s"

        var remaining = seconds
        val handler = android.os.Handler(android.os.Looper.getMainLooper())

        val timerRunnable = object : Runnable {
            override fun run() {
                remaining--
                binding.tvTimer.text = "${remaining}s"

                // Muda a cor para vermelho nos últimos 10 segundos
                if (remaining <= 10) {
                    binding.tvTimer.setTextColor(
                        ContextCompat.getColor(this@ConferenceActivity, R.color.error)
                    )
                }

                if (remaining <= 0) {
                    // Timer expirou — encerra a votação automaticamente
                    Log.d(Constants.TAG_VOTING, "Timer expirado — encerrando votação")
                    endCurrentVote()
                    showSnackbar("Tempo esgotado! Votação encerrada automaticamente.")
                } else if (currentVote?.isActive == true) {
                    handler.postDelayed(this, 1000)
                }
            }
        }

        handler.postDelayed(timerRunnable, 1000)
    }

    // =========================================================================
    // Controle dos Painéis (Votação e Histórico)
    // =========================================================================

    /**
     * Exibe o painel de votação com animação de slide-up.
     */
    private fun showVotingPanel() {
        binding.cardVotingPanel.apply {
            visibility = View.VISIBLE
            alpha = 0f
            translationY = 100f
            animate()
                .alpha(1f)
                .translationY(0f)
                .setDuration(300)
                .start()
        }
        // Esconde o histórico se estiver visível
        if (binding.cardHistoryPanel.visibility == View.VISIBLE) {
            hideHistoryPanel()
        }
    }

    /**
     * Esconde o painel de votação com animação.
     */
    private fun hideVotingPanel() {
        binding.cardVotingPanel.animate()
            .alpha(0f)
            .translationY(100f)
            .setDuration(250)
            .withEndAction {
                binding.cardVotingPanel.visibility = View.GONE
            }
            .start()
    }

    /**
     * Alterna a visibilidade do painel de histórico.
     */
    private fun toggleHistoryPanel() {
        if (binding.cardHistoryPanel.visibility == View.VISIBLE) {
            hideHistoryPanel()
        } else {
            showHistoryPanel()
        }
    }

    /**
     * Exibe o painel de histórico com animação.
     */
    private fun showHistoryPanel() {
        // Atualiza a visibilidade da mensagem "sem histórico"
        binding.tvNoHistory.visibility = if (decisionHistory.isEmpty()) View.VISIBLE else View.GONE

        binding.cardHistoryPanel.apply {
            visibility = View.VISIBLE
            alpha = 0f
            translationY = 100f
            animate()
                .alpha(1f)
                .translationY(0f)
                .setDuration(300)
                .start()
        }
        // Esconde a votação se estiver visível
        if (binding.cardVotingPanel.visibility == View.VISIBLE) {
            hideVotingPanel()
        }
    }

    /**
     * Esconde o painel de histórico com animação.
     */
    private fun hideHistoryPanel() {
        binding.cardHistoryPanel.animate()
            .alpha(0f)
            .translationY(100f)
            .setDuration(250)
            .withEndAction {
                binding.cardHistoryPanel.visibility = View.GONE
            }
            .start()
    }

    // =========================================================================
    // Ações de Saída
    // =========================================================================

    /**
     * Exibe dialog de confirmação antes de sair da chamada.
     */
    private fun confirmLeaveCall() {
        AlertDialog.Builder(this)
            .setTitle(getString(R.string.confirm_leave_title))
            .setMessage(getString(R.string.confirm_leave_message))
            .setPositiveButton(getString(R.string.btn_confirm_leave)) { _, _ ->
                leaveConference()
            }
            .setNegativeButton(getString(R.string.btn_cancel), null)
            .show()
    }

    /**
     * Encerra a conferência e volta para a tela anterior.
     */
    private fun leaveConference() {
        Log.d(Constants.TAG_CONFERENCE, "Encerrando conferência")
        finish()
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
    }

    // =========================================================================
    // Utilitários
    // =========================================================================

    /**
     * Exibe uma mensagem Snackbar na tela.
     */
    private fun showSnackbar(message: String) {
        com.google.android.material.snackbar.Snackbar.make(
            binding.root,
            message,
            com.google.android.material.snackbar.Snackbar.LENGTH_SHORT
        ).apply {
            setBackgroundTint(
                ContextCompat.getColor(this@ConferenceActivity, R.color.bg_elevated)
            )
            setTextColor(
                ContextCompat.getColor(this@ConferenceActivity, R.color.text_primary)
            )
            show()
        }
    }

    override fun onBackPressed() {
        confirmLeaveCall()
    }
}
