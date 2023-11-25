package com.example.alaminu.ui.modul

import android.content.Intent
import android.net.Uri
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
import com.example.alaminu.databinding.FragmentMateriBinding
import com.example.alaminu.ui.modul.MateriAdapter
import org.json.JSONArray

class MateriFragment : Fragment(), MateriAdapter.MateriClickListener {

    private var _binding: FragmentMateriBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: MateriAdapter
    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMateriBinding.inflate(inflater, container, false)
        val root: View = binding.root

        recyclerView = root.findViewById(R.id.materi)
        adapter = MateriAdapter(this)

        recyclerView.layoutManager =
            LinearLayoutManager(root.context, LinearLayoutManager.VERTICAL, false)
        recyclerView.adapter = adapter

        fetchData(DbContract.urlRecymateri, adapter)

        return root
    }

    override fun onMateriClick(mapel: Map<String, String>) {
        val materiLink = mapel["materi"]
        if (!materiLink.isNullOrBlank()) {
            bukaLink(materiLink)
        }
    }

    private fun bukaLink(url: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(intent)
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
                    is MateriAdapter -> {
                        val mapels = parseMapels(response)
                        adapter.setMapels(mapels)
                    }
                }
            },
            { error ->
                // Handle errors
                error.printStackTrace()
            }
        )
        requestQueue.add(jsonArrayRequest)
    }

    private fun parseMapels(jsonArray: JSONArray): List<Map<String, String>> {
        val mapels = mutableListOf<Map<String, String>>()

        for (i in 0 until jsonArray.length()) {
            val jsonObject = jsonArray.getJSONObject(i)
            val mapel = mapOf(
                "nama_module" to jsonObject.getString("nama_module"),
                "materi" to jsonObject.getString("materi")
            )
            mapels.add(mapel)
        }

        return mapels
    }
}
