package com.example.alaminu.ui.jadwal

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CalendarView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.alaminu.R

class ScheduleFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_jadwal, container, false)

        // Inisialisasi CalendarView
        val calendarView: CalendarView = rootView.findViewById(R.id.calendarView)

        // Atur listener untuk mendengar perubahan tanggal
        calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            // Lakukan sesuatu dengan tanggal yang dipilih
            val selectedDate = "$dayOfMonth/${month + 1}/$year"
            Toast.makeText(requireActivity(), "Tanggal dipilih: $selectedDate", Toast.LENGTH_SHORT).show()

        }

        return rootView
    }
}

