package com.decidetogether.ui.room

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import com.decidetogether.R
import com.decidetogether.databinding.ActivityRoomBinding
import com.decidetogether.ui.conference.ConferenceActivity
import com.decidetogether.util.Constants
import com.google.android.material.snackbar.Snackbar

/**
 * Tela 3: Criar / Entrar em Sala
 *
 * Formulário para inserir nome do usuário e nome da sala.
 * O modo (CREATE ou JOIN) é determinado pelo extra recebido via Intent.
 *
 * Ao confirmar, valida os campos e navega para a ConferenceActivity,
 * passando os dados necessários para iniciar a videoconferência via Jitsi.
 */
class RoomActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRoomBinding

    // Modo da tela: CREATE (criar sala) ou JOIN (entrar em sala)
    private var roomMode: String = Constants.ROOM_MODE_CREATE

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRoomBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Recupera o modo passado pela HomeActivity
        roomMode = intent.getStringExtra(Constants.EXTRA_ROOM_MODE) ?: Constants.ROOM_MODE_CREATE

        setupUI()
        setupClickListeners()
        setupTextWatchers()
    }

    /**
     * Configura a interface de acordo com o modo (CREATE ou JOIN).
     */
    private fun setupUI() {
        if (roomMode == Constants.ROOM_MODE_CREATE) {
            binding.tvRoomTitle.text = getString(R.string.title_create_room)
            binding.btnAction.text = getString(R.string.btn_create)
        } else {
            binding.tvRoomTitle.text = getString(R.string.title_join_room)
            binding.btnAction.text = getString(R.string.btn_join)
        }

        // Animação de entrada
        binding.root.apply {
            alpha = 0f
            animate().alpha(1f).setDuration(300).start()
        }
    }

    /**
     * Configura os listeners de clique.
     */
    private fun setupClickListeners() {
        // Botão voltar
        binding.btnBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        // Botão de ação principal (Criar / Entrar)
        binding.btnAction.setOnClickListener {
            attemptJoinOrCreate()
        }

        // Ação do teclado no campo de sala (pressionar "Done")
        binding.etRoomName.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                attemptJoinOrCreate()
                true
            } else {
                false
            }
        }
    }

    /**
     * Configura TextWatchers para limpar erros ao digitar.
     */
    private fun setupTextWatchers() {
        binding.etUsername.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                binding.tilUsername.error = null
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        binding.etRoomName.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                binding.tilRoomName.error = null
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    /**
     * Valida os campos e inicia a videoconferência se tudo estiver correto.
     */
    private fun attemptJoinOrCreate() {
        val username = binding.etUsername.text?.toString()?.trim() ?: ""
        val roomName = binding.etRoomName.text?.toString()?.trim() ?: ""

        // Validação do nome do usuário
        if (username.isEmpty()) {
            binding.tilUsername.error = getString(R.string.error_empty_username)
            binding.etUsername.requestFocus()
            return
        }

        // Validação do nome da sala
        if (roomName.isEmpty()) {
            binding.tilRoomName.error = getString(R.string.error_empty_room)
            binding.etRoomName.requestFocus()
            return
        }

        // Esconde o teclado virtual
        hideKeyboard()

        // Desabilita o botão para evitar duplo clique
        binding.btnAction.isEnabled = false

        // Navega para a ConferenceActivity com os dados da sala
        navigateToConference(username, roomName)
    }

    /**
     * Navega para a tela de videoconferência.
     *
     * @param username Nome do participante
     * @param roomName Nome da sala Jitsi
     */
    private fun navigateToConference(username: String, roomName: String) {
        val intent = Intent(this, ConferenceActivity::class.java).apply {
            putExtra(Constants.EXTRA_USERNAME, username)
            putExtra(Constants.EXTRA_ROOM_NAME, roomName)
            putExtra(Constants.EXTRA_ROOM_MODE, roomMode)
        }
        startActivity(intent)
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
    }

    /**
     * Esconde o teclado virtual.
     */
    private fun hideKeyboard() {
        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        currentFocus?.let { view ->
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    override fun onResume() {
        super.onResume()
        // Reabilita o botão ao voltar para esta tela
        binding.btnAction.isEnabled = true
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
    }
}
