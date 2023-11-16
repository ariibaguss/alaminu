package com.example.alaminu

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.alaminu.databinding.ActivityLoginBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val etUsername: EditText by lazy { binding.username }
    private val etPassword: EditText by lazy { binding.pass }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        binding.btnmasuk.setOnClickListener {
            val username = etUsername.text.toString()
            val password = etPassword.text.toString()

            if (!(username.isEmpty() || password.isEmpty())) {
                loginUser(username, password)
            } else {
                Toast.makeText(applicationContext, R.string.invalid_credentials, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun loginUser(username: String, password: String) {
        GlobalScope.launch(Dispatchers.IO) {
            val requestQueue: RequestQueue = Volley.newRequestQueue(applicationContext)

            val stringRequest = StringRequest(
                Request.Method.GET,
                "${DbContract.urlLogin}?username=$username&password=$password",
                { response ->
                    Log.d("LoginActivity", "Respon Server: $response")
                    if (response == "Selamat datang") {
                        Log.d("LoginActivity", "Login Berhasil")
                        Toast.makeText(applicationContext, R.string.login_success, Toast.LENGTH_SHORT).show()
                        startActivity(Intent(applicationContext, MainActivity::class.java))
                    } else {
                        Log.d("LoginActivity", "Login Gagal")
                        Toast.makeText(applicationContext, R.string.login_failed, Toast.LENGTH_SHORT).show()
                    }
                },
                { error ->
                    Log.e("LoginActivity", "Kesalahan Volley: ${error.message}")
                    Toast.makeText(applicationContext, "Login Gagal: ${error.message}", Toast.LENGTH_SHORT).show()
                }
            )
            requestQueue.add(stringRequest)
        }
    }
}