package com.example.alaminu.ui.profil

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.alaminu.R
import com.example.alaminu.UserPreferences
import com.example.alaminu.databinding.FragmentProfileBinding
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class ProfilActivity : AppCompatActivity() {

    private lateinit var binding: FragmentProfileBinding
    private val userPreferences by lazy { UserPreferences(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        MainScope().launch {
            val username = userPreferences.getUsername()
            val email = userPreferences.getEmail()
            val noWa = userPreferences.getNowa()
            val jenisKelamin = userPreferences.getJk()

            // Update UI elements with user data
            binding.editUsername.setText(username)
            binding.editEmail.setText(email)
            binding.editJk.setText(jenisKelamin)
            binding.editNowa.setText(noWa)
            // Set other fields as needed
        }
    }
}
