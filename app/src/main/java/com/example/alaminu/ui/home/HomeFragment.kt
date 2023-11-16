package com.example.alaminu.ui.home

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
import com.example.alaminu.databinding.FragmentHomeBinding
import com.example.alaminu.ui.home.adapter.aktivitas.Aktivitas
import com.example.alaminu.ui.home.adapter.mapel.Mapel
import com.example.alaminu.ui.home.adapter.mentor.Mentor
import org.json.JSONArray

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: Mapel
    private lateinit var adapter2: Mentor
    private lateinit var adapter3: Aktivitas

    private lateinit var recyclerView: RecyclerView
    private lateinit var recyclerView2: RecyclerView
    private lateinit var recyclerView3: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        recyclerView = root.findViewById(R.id.mapel)
        recyclerView2 = root.findViewById(R.id.mentor)
        recyclerView3 = root.findViewById(R.id.aktivitas)

        adapter = Mapel()
        adapter2 = Mentor()
        adapter3 = Aktivitas()

        recyclerView.layoutManager =
            LinearLayoutManager(root.context, LinearLayoutManager.HORIZONTAL, false)
        recyclerView2.layoutManager =
            LinearLayoutManager(root.context, LinearLayoutManager.HORIZONTAL, false)
        recyclerView3.layoutManager =
            LinearLayoutManager(root.context, LinearLayoutManager.VERTICAL, false)

        recyclerView.adapter = adapter
        recyclerView2.adapter = adapter2
        recyclerView3.adapter = adapter3

        // Make network requests to retrieve data from different tables or URLs
        fetchData(DbContract.urlRecymapel, adapter)
        fetchData(DbContract.urlRecymentor, adapter2)

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun fetchData(url: String, adapter: RecyclerView.Adapter<*>) {
        val requestQueue: RequestQueue = Volley.newRequestQueue(requireContext())

        val jsonArrayRequest = JsonArrayRequest(
            Request.Method.GET,
            url,
            null,
            { response ->
                // Parse the JSON array and update your UI with the retrieved data
                when (adapter) {
                    is Mapel -> {
                        val mapels = parseMapels(response)
                        adapter.setMapels(mapels)
                    }

                    is Mentor -> {
                        val mapels = parseUsernames(response)
                        adapter.setMapels(mapels)
                    }
                }
            },
            { error ->
                // Handle errors
            }
        )
        requestQueue.add(jsonArrayRequest)
    }

    private fun parseMapels(jsonArray: JSONArray): List<Map<String, String>> {
        val mapels = mutableListOf<Map<String, String>>()

        for (i in 0 until jsonArray.length()) {
            val jsonObject = jsonArray.getJSONObject(i)
            // Assuming your JSON structure has keys "nama_mapel" and "path_gambar"
            val mapel = mapOf(
                "nama" to jsonObject.getString("nama"),
                "image" to jsonObject.getString("image")
            )
            mapels.add(mapel)
        }

        return mapels
    }

    private fun parseUsernames(jsonArray: JSONArray): List<Map<String, String>> {
        val mapels = mutableListOf<Map<String, String>>()

        for (i in 0 until jsonArray.length()) {
            val jsonObject = jsonArray.getJSONObject(i)
            // Assuming your JSON structure has keys "nama_mapel" and "path_gambar"
            val mapel = mapOf(
                "nama" to jsonObject.getString("nama"),
                "image" to jsonObject.getString("image")
            )
            mapels.add(mapel)
        }

        return mapels
    }
}
