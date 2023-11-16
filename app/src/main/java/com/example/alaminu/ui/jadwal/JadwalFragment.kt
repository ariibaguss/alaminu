package com.example.alaminu.ui.jadwal

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CalendarView
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.alaminu.R
import com.example.alaminu.databinding.FragmentJadwalBinding

class JadwalFragment : Fragment() {

    private var _binding: FragmentJadwalBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel = ViewModelProvider(this).get(JadwalViewModel::class.java)

        _binding = FragmentJadwalBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Mendapatkan referensi ke CalendarView
        val calendarView: CalendarView = root.findViewById(R.id.calendarView)

        // Menetapkan listener untuk OnDateChangeListener
        calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            // replaceFragment(year, month, dayOfMonth) // hapus baris ini jika tidak diperlukan
            // Panggil metode untuk menggantikan ScheduleFragment
        }

        if (savedInstanceState == null) {
            // Di sini Anda dapat memutuskan apakah ingin langsung menampilkan ScheduleFragment atau tidak
            // childFragmentManager.beginTransaction()
            //     .replace(R.id.fragment_container, ScheduleFragment())
            //     .commit()
        }

        val btnSetReminder: Button = root.findViewById(R.id.btnSetReminder)

        btnSetReminder.setOnClickListener {
            // Panggil metode untuk menampilkan popup memasukkan hari, bulan, dan jam
            showReminderPopup()
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showReminderPopup() {
        val inflater = LayoutInflater.from(requireContext())
        val dialogView = inflater.inflate(R.layout.pop_up, null)
        val editTextReminder = dialogView.findViewById<EditText>(R.id.editTextReminder)

        val builder = AlertDialog.Builder(requireContext())
        builder.setView(dialogView)
            .setTitle("Masukkan Data Reminder")
            .setPositiveButton("OK") { _, _ ->
                // Tangani logika saat pengguna menekan OK
                val reminderData = editTextReminder.text.toString()
                // Lakukan sesuatu dengan data reminder, seperti memperbarui TextView
                // Misalnya: textView.text = reminderData
            }
            .setNegativeButton("Batal") { _, _ ->
                // Tangani logika saat pengguna menekan Batal
            }
            .show()
    }
}
