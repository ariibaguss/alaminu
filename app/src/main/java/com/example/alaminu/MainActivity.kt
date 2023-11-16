package com.example.alaminu

import ModulFragment
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.alaminu.databinding.ActivityMainBinding
import com.example.alaminu.ui.absen.AbsenFragment
import com.example.alaminu.ui.home.HomeFragment
import com.example.alaminu.ui.jadwal.JadwalFragment
import com.example.alaminu.ui.notif.NotifFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        replaceFragment(HomeFragment())
        binding.bottomNavigationView.background = null
        binding.bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> replaceFragment(HomeFragment())
                R.id.navigation_modul -> replaceFragment(ModulFragment())
                R.id.navigation_jadwal -> replaceFragment(JadwalFragment())
                R.id.navigation_notifikasi -> replaceFragment(NotifFragment())
            }
            true
        }
        binding.btnabsen.setOnClickListener() {
            replaceFragment(AbsenFragment())
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager: FragmentManager = supportFragmentManager
        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frame_layout, fragment)
        fragmentTransaction.commit()
    }
}
