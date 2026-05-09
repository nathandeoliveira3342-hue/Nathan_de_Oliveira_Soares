package com.decidetogether.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import com.decidetogether.R
import com.decidetogether.databinding.ActivityHomeBinding
import com.decidetogether.ui.room.RoomActivity
import com.decidetogether.util.Constants

/**
 * Tela 2: Home Screen
 *
 * Ponto de entrada principal do app após a splash.
 * Oferece dois caminhos:
 *   - Criar Sala: abre RoomActivity no modo CREATE
 *   - Entrar em Sala: abre RoomActivity no modo JOIN
 *
 * Aplica animações de entrada nas views para melhorar a UX.
 */
class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupClickListeners()
        playEntranceAnimations()
    }

    /**
     * Configura os listeners de clique nos botões principais.
     */
    private fun setupClickListeners() {
        // Botão "Criar Sala"
        binding.btnCreateRoom.setOnClickListener {
            navigateToRoom(Constants.ROOM_MODE_CREATE)
        }

        // Botão "Entrar em Sala"
        binding.btnJoinRoom.setOnClickListener {
            navigateToRoom(Constants.ROOM_MODE_JOIN)
        }
    }

    /**
     * Navega para a RoomActivity com o modo especificado.
     *
     * @param mode Constante indicando se é CREATE ou JOIN
     */
    private fun navigateToRoom(mode: String) {
        val intent = Intent(this, RoomActivity::class.java).apply {
            putExtra(Constants.EXTRA_ROOM_MODE, mode)
        }
        startActivity(intent)
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
    }

    /**
     * Aplica animações de entrada suaves nas views da tela.
     */
    private fun playEntranceAnimations() {
        val fadeSlideUp = AnimationUtils.loadAnimation(this, R.anim.fade_slide_up)

        // Aplica animação com delays progressivos para efeito cascata
        binding.layoutContent.apply {
            alpha = 0f
            animate()
                .alpha(1f)
                .translationYBy(-30f)
                .setDuration(600)
                .setStartDelay(100)
                .start()
        }

        binding.layoutFeatures.apply {
            alpha = 0f
            animate()
                .alpha(1f)
                .setDuration(500)
                .setStartDelay(400)
                .start()
        }
    }
}
