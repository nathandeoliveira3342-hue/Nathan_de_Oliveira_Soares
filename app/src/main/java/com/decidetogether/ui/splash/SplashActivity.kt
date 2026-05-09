package com.decidetogether.ui.splash

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.appcompat.app.AppCompatActivity
import com.decidetogether.databinding.ActivitySplashBinding
import com.decidetogether.ui.home.HomeActivity

/**
 * Tela 1: Splash Screen
 *
 * Exibe o logo e nome do app com animação de fade-in suave.
 * Após 2.5 segundos, navega automaticamente para a HomeActivity.
 *
 * Utiliza ViewBinding para acesso seguro às views.
 */
class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding

    // Handler para agendar a transição para a Home
    private val handler = Handler(Looper.getMainLooper())

    // Duração total da splash em milissegundos
    private val SPLASH_DURATION = 2500L

    // Duração da animação de fade-in
    private val FADE_DURATION = 800L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Inicia as animações de entrada
        startAnimations()

        // Agenda a transição para a Home após o tempo definido
        handler.postDelayed({
            navigateToHome()
        }, SPLASH_DURATION)
    }

    /**
     * Executa as animações de fade-in e slide-up do logo e textos.
     */
    private fun startAnimations() {
        // Animação do container do logo: fade-in + translação para cima
        val containerFadeIn = ObjectAnimator.ofFloat(
            binding.layoutLogoContainer, "alpha", 0f, 1f
        ).apply {
            duration = FADE_DURATION
            interpolator = AccelerateDecelerateInterpolator()
        }

        val containerSlideUp = ObjectAnimator.ofFloat(
            binding.layoutLogoContainer, "translationY", 60f, 0f
        ).apply {
            duration = FADE_DURATION
            interpolator = AccelerateDecelerateInterpolator()
        }

        // Animação do progress bar: aparece depois do logo
        val progressFadeIn = ObjectAnimator.ofFloat(
            binding.progressBar, "alpha", 0f, 0.8f
        ).apply {
            duration = 400L
            startDelay = 600L
        }

        // Animação da versão
        val versionFadeIn = ObjectAnimator.ofFloat(
            binding.tvVersion, "alpha", 0f, 0.5f
        ).apply {
            duration = 400L
            startDelay = 800L
        }

        // Executa todas as animações em conjunto
        AnimatorSet().apply {
            playTogether(containerFadeIn, containerSlideUp, progressFadeIn, versionFadeIn)
            start()
        }
    }

    /**
     * Navega para a HomeActivity e finaliza a SplashActivity.
     */
    private fun navigateToHome() {
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
        // Animação de transição: fade-out da splash, fade-in da home
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        // Remove callbacks pendentes para evitar memory leaks
        handler.removeCallbacksAndMessages(null)
    }
}
