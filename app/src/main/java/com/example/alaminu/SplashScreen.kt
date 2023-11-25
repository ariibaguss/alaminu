package com.example.alaminu

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import com.example.alaminu.ui.slider.Onboarding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class SplashScreen : AppCompatActivity() {

    private val userPreferences by lazy { UserPreferences(this) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash_screen)
        supportActionBar?.hide()

        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed({
            checkLoginStatus()
        }, 3000)
    }

    private fun checkLoginStatus() {
        GlobalScope.launch(Dispatchers.IO) {
            val username = userPreferences.getUsername()
            val password = userPreferences.getPasssword()

            launch(Dispatchers.Main) {
                if (username.isNotEmpty() && password.isNotEmpty()) {
                    Log.d("SplashScreen", "Username: $username, Password: $password")
                    // Pengguna masih login, pergi ke MainActivity
                    val intent = Intent(this@SplashScreen, MainActivity::class.java)
                    startActivity(intent)
                } else {
                    // Pengguna belum login, pergi ke LoginActivity
                    val intent = Intent(this@SplashScreen, Onboarding::class.java)
                    startActivity(intent)
                }
                finish()
            }
        }
    }
}
