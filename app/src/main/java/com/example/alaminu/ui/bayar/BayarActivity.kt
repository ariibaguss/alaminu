package com.example.alaminu.ui.bayar

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.alaminu.databinding.FragmentBayarBinding

class BayarActivity : AppCompatActivity() {

    private lateinit var binding: FragmentBayarBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentBayarBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        
    }
}