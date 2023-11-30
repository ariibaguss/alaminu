package com.example.alaminu.ui.notif

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.example.alaminu.DbContract
import com.example.alaminu.R
import com.example.alaminu.UserPreferences
import com.example.alaminu.databinding.FragmentNotifBinding
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject

class NotifFragment : Fragment() {

    private var _binding: FragmentNotifBinding? = null
    private val binding get() = _binding!!
    private val userPreferences by lazy { UserPreferences(requireContext()) }

    private lateinit var notificationsAdapter: NotificationsAdapter
    private lateinit var requestQueue: RequestQueue

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNotifBinding.inflate(inflater, container, false)
        val view = binding.root

        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Inisialisasi adapter dengan tglPembayaran
        notificationsAdapter = NotificationsAdapter("tgl_pembayaran")

        // Set the adapter to the RecyclerView
        recyclerView.adapter = notificationsAdapter
        // Initialize the RequestQueue
        requestQueue = Volley.newRequestQueue(requireContext())

        // Make a network request to fetch JSON data
        fetchData()

        return view
    }

    private fun fetchData() {
        MainScope().launch {
            val userId = userPreferences.getUserId() ?: ""
            val url = "${DbContract.urlCicilan}?id_pengguna=$userId"

            val jsonArrayRequest = JsonArrayRequest(
                Request.Method.GET, url, null,
                { response ->
                    // Parse the JSON response
                    val notificationsList = parseJson(response)
                    // Update the RecyclerView with the parsed data
                    notificationsAdapter.setData(notificationsList)

                    if (notificationsList.isNotEmpty()) {
                        binding.recyclerView.visibility = View.VISIBLE
                        binding.textStatusZero.visibility = View.GONE
                    } else {
                        binding.recyclerView.visibility = View.GONE
                        binding.textStatusZero.visibility = View.VISIBLE
                    }
                },
                { error ->
                    // Handle error
                    error.printStackTrace()
                }
            )

            // Add the request to the RequestQueue
            requestQueue.add(jsonArrayRequest)
        }
    }

    private fun parseJson(jsonArray: JSONArray): List<Notifications> {
        val notificationsList = mutableListOf<Notifications>()

        for (i in 0 until jsonArray.length()) {
            val jsonObject: JSONObject = jsonArray.getJSONObject(i)

            // Parse the JSON data and create Notifications object
            val idPembayaran = jsonObject.getInt("id_pembayaran")
            val bayar = jsonObject.getInt("bayar")
            val status = jsonObject.getInt("status")
            val idProgram = jsonObject.getString("id_program")
            val tglPembayaran = jsonObject.getString("tgl_pembayaran")
            val idPengguna = jsonObject.getInt("id_pengguna")

            val notification = Notifications(idPembayaran, bayar, status, idProgram, tglPembayaran, idPengguna)
            notificationsList.add(notification)
        }

        return notificationsList
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
