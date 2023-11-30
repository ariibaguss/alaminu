package com.example.alaminu

import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.EditText
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.alaminu.databinding.ActivityLoginBinding
import com.example.alaminu.ui.profil.UserData
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth
import org.json.JSONException
import org.json.JSONObject

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private val userPreferences by lazy { UserPreferences(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        firebaseAuth = FirebaseAuth.getInstance()

        binding.btnmasuk.setOnClickListener {
            val username = binding.userEditText.text.toString()
            val password = binding.passEditText.text.toString()

            if (!(username.isEmpty() || password.isEmpty())) {
                loginUser(username, password)
            } else {
                Toast.makeText(applicationContext, R.string.invalid_credentials, Toast.LENGTH_SHORT)
                    .show()
            }
        }

        binding.lupa.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            val view = layoutInflater.inflate(R.layout.dialog_forgot, null)
            val userEmail = view.findViewById<EditText>(R.id.emailEditText)
            builder.setView(view)
            val dialog = builder.create()
            view.findViewById<Button>(R.id.btnReset).setOnClickListener {
                compareEmail(userEmail)
                dialog.dismiss()
            }
            view.findViewById<Button>(R.id.btnCancel).setOnClickListener {
                dialog.dismiss()
            }
            if (dialog.window != null) {
                dialog.window!!.setBackgroundDrawable(ColorDrawable(0))
            }
            dialog.show()
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

                    try {
                        val jsonResponse = JSONObject(response)
                        val status = jsonResponse.getString("status")
                        val message = jsonResponse.getString("message")

                        if (status == "success") {
                            Log.d("LoginActivity", "Login Berhasil")

                            val userDataJson = jsonResponse.getJSONObject("data")
                            val userData = Gson().fromJson(userDataJson.toString(), UserData::class.java)

                            MainScope().launch {
                                userPreferences.saveUsername(username)
                                userPreferences.savePassword(password)

                                userPreferences.saveUserData(userData)
                            }

                            Toast.makeText(applicationContext, R.string.login_success, Toast.LENGTH_SHORT).show()
                            startActivity(Intent(applicationContext, MainActivity::class.java))
                        } else {
                            Log.d("LoginActivity", "Login Gagal: $message")
                            Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
                        }
                    } catch (e: JSONException) {
                        Log.e("LoginActivity", "Kesalahan parsing JSON: ${e.message}")
                        Toast.makeText(applicationContext, "Kesalahan parsing JSON", Toast.LENGTH_SHORT).show()
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

    private fun compareEmail(email: EditText){
        if (email.text.toString().isEmpty()){
            return
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email.text.toString()).matches()){
            return
        }

        firebaseAuth.sendPasswordResetEmail(email.text.toString())
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Check your email", Toast.LENGTH_SHORT).show()
                }
            }
    }
}
