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

class LatihanFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var recyclerViewAdapter: Adapter
    private lateinit var percentageCircleView1: PrecentageCircleView
    private lateinit var percentageText1: TextView
    private lateinit var incrementButton1: Button

    private var currentPercentage = 0f

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_latihan, container, false)

        recyclerView = view.findViewById(R.id.recyclerView2)
        recyclerView.layoutManager = LinearLayoutManager(context)
        percentageCircleView1 = view.findViewById(R.id.percentageCircleView1)
        incrementButton1 = view.findViewById(R.id.incrementButton1)
        percentageText1 = view.findViewById(R.id.percentageText1)

        val dataList = listOf("Item 1", "Item 2", "Item 3", "item 4", "item 5") // Gantilah dengan data Anda
        recyclerViewAdapter = Adapter(dataList)
        recyclerView.adapter = recyclerViewAdapter

        incrementButton1.setOnClickListener {
            incrementPercentage(10f)
    }
        return view
}
    private fun incrementPercentage(incrementValue: Float) {
        currentPercentage += incrementValue

        if (currentPercentage > 100) {
            currentPercentage = 100f
        }

        // Update presentase di dalam lingkaran
        percentageText1.text = "${currentPercentage.toInt()}%"

        // Update presentase dan warna pada lingkaran
        percentageCircleView1.setPercentage(currentPercentage)
    }
}
