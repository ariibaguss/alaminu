package com.example.alaminu.ui.modul

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.example.alaminu.ui.home.adapter.mapel.Mapel
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.example.alaminu.DbContract
import com.example.alaminu.R
import org.json.JSONArray

class SemuaFragment : Fragment() {

    private lateinit var percentageCircleView: PrecentageCircleView
    private lateinit var percentageText: TextView
    private lateinit var incrementButton: Button
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: Mapel
    private lateinit var textView: TextView

    private var currentPercentage = 0f

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_semua, container, false)
        recyclerView = view.findViewById(R.id.horizontalRecyclerView)

        recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        adapter = Mapel()

        recyclerView.adapter = adapter
        fetchData(DbContract.urlRecymapel, adapter)

        return view
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
            // Assuming your JSON structure has keys "nama_mapel" and "image_blob"
            val mapel = mapOf(
                "nama" to jsonObject.getString("nama"),
                "image" to jsonObject.getString("image")
            )
            mapels.add(mapel)
        }

        return mapels
    }
}
