package com.example.alaminu.ui.jadwal

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CalendarView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.example.alaminu.DbContract
import com.example.alaminu.R
import com.example.alaminu.databinding.FragmentJadwalBinding
import org.json.JSONArray
import org.json.JSONException

class JadwalFragment : Fragment() {

    private var _binding: FragmentJadwalBinding? = null
    private val binding get() = _binding!!
    private lateinit var jadwalAdapter: JadwalAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentJadwalBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Initialize RecyclerView
        val recyclerView: RecyclerView = root.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        jadwalAdapter = JadwalAdapter()
        recyclerView.adapter = jadwalAdapter

        // Initialize CalendarView
        val calendarView: CalendarView = root.findViewById(R.id.calendarView)

        // Set the current date as the default selected date
        val currentDate = Calendar.getInstance()
        calendarView.date = currentDate.timeInMillis

        // Fetch and display data for the current date
        val selectedDate =
            SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(currentDate.time)
        fetchDataForDate(selectedDate)

        // Update RecyclerView on Date Change
        calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            val selectedDate = "$year-${month + 1}-$dayOfMonth"
            fetchDataForDate(selectedDate)
        }

        return root
    }

    // Fetch and display data for a given date
    private fun fetchDataForDate(selectedDate: String) {
        // Fetch data from API using Volley
        val url = "${DbContract.urlJadwal}"

        val jsonArrayRequest = JsonArrayRequest(Request.Method.GET, url, null,
            { response ->
                try {
                    val events = parseJson(response)
                    jadwalAdapter.setJadwal(events)
                    jadwalAdapter.filterByDate(selectedDate)
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            },
            { error ->
                Toast.makeText(requireContext(), "Error: ${error.message}", Toast.LENGTH_SHORT)
                    .show()
            })

        // Add the request to the request queue
        Volley.newRequestQueue(requireContext()).add(jsonArrayRequest)
    }

    private fun parseJson(jsonArray: JSONArray): List<JadwalEvent> {
        val events = mutableListOf<JadwalEvent>()
        for (i in 0 until jsonArray.length()) {
            val jsonObject = jsonArray.getJSONObject(i)
            val idJadwal = jsonObject.getString("id_jadwal")
            val idModule = jsonObject.getString("id_module")
            val hari = jsonObject.getString("hari")
            val jam = jsonObject.getString("jam")
            val tanggal = jsonObject.getString("tanggal")
            val nama = jsonObject.getString("nama")

            val event = JadwalEvent(idJadwal, tanggal, hari, jam, idModule, nama)
            events.add(event)
        }
        return events
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
