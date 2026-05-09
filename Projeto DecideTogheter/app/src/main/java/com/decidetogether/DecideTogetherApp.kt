package com.decidetogether

import android.app.Application
import android.util.Log

/**
 * Classe Application principal do DecideTogether.
 * Responsável pela inicialização global do app.
 */
class DecideTogetherApp : Application() {

    companion object {
        private const val TAG = "DecideTogetherApp"
        lateinit var instance: DecideTogetherApp
            private set
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        Log.d(TAG, "DecideTogether Application iniciado")
    }
}
