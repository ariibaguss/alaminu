package com.example.alaminu

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.util.Log
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.RetryPolicy
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.alaminu.databinding.NewPasswordBinding

class NewPassword : AppCompatActivity() {

    private lateinit var binding: NewPasswordBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = NewPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        val email = intent.getStringExtra("email")
        val newpass = findViewById<EditText>(R.id.newpassEditText)
        val otp = findViewById<EditText>(R.id.otpEditText)
        val progressBar = findViewById<ProgressBar>(R.id.progress)

        binding.btnmasuk.setOnClickListener {
            val queue = Volley.newRequestQueue(applicationContext)
            val url = "${DbContract.urlNewPass}"

            val stringRequest = object : StringRequest(
                Request.Method.POST, url,
                Response.Listener<String> { response ->
                    if (response == "success") {
                        Toast.makeText(applicationContext, "Password telah diatur", Toast.LENGTH_SHORT).show()
                        val intent = Intent(applicationContext, LoginActivity::class.java)
                        startActivity(intent)
                        Log.d("NewPassword", "Navigating to LoginActivity")
                    } else {
                        Toast.makeText(applicationContext, response, Toast.LENGTH_SHORT).show()
                    }
                },
                Response.ErrorListener { error ->
                    error.printStackTrace()
                }) {
                override fun getParams(): Map<String, String> {
                    val params: MutableMap<String, String> = HashMap()
                    params["email"] = email ?: ""
                    params["otp"] = otp.text.toString()
                    params["new_password"] = newpass.text.toString()
                    return params
                }
            }

            // Set a custom retry policy
            stringRequest.retryPolicy = object : RetryPolicy {
                override fun getCurrentTimeout(): Int {
                    return 3000
                }

                override fun getCurrentRetryCount(): Int {
                    return 3000
                }

                @Throws(VolleyError::class)
                override fun retry(error: VolleyError) {
                    // Handle retry logic here
                }
            }
            queue.add(stringRequest)
        }
    }
}
