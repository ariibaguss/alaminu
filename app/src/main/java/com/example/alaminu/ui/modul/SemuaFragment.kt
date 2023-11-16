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
import com.example.alaminu.R

class SemuaFragment : Fragment() {

    private lateinit var percentageCircleView: PrecentageCircleView
    private lateinit var percentageText: TextView
    private lateinit var incrementButton: Button
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: HorizontalAdapter
    private lateinit var textView: TextView

    private var currentPercentage = 0f

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_semua, container, false)
        textView = view.findViewById<TextView>(R.id.textView2)

        percentageCircleView = view.findViewById(R.id.percentageCircleView)
        incrementButton = view.findViewById(R.id.incrementButton)
        percentageText = view.findViewById(R.id.percentageText)
        recyclerView = view.findViewById(R.id.horizontalRecyclerView)


        // Set up RecyclerView
        recyclerView = view.findViewById(R.id.horizontalRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        // Inisialisasi adapter (ganti dengan adapter Anda)
        val data = listOf("Item 1", "Item 2", "Item 3") // Gantilah dengan data Anda
        adapter = HorizontalAdapter(data)

        recyclerView.adapter = adapter

        // TODO: Tambahkan adapter untuk RecyclerView sesuai kebutuhan Anda

        incrementButton.setOnClickListener {
            incrementPercentage(10f)
            textView.setBackgroundResource(R.drawable.textpressed)
        }

        return view
    }

    private fun incrementPercentage(incrementValue: Float) {
        currentPercentage += incrementValue

        if (currentPercentage > 100) {
            currentPercentage = 100f
        }

        // Update presentase di dalam lingkaran
        percentageText.text = "${currentPercentage.toInt()}%"

        // Update presentase dan warna pada lingkaran
        percentageCircleView.setPercentage(currentPercentage)
    }
}
