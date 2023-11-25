package com.example.alaminu.ui.modul

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.alaminu.R

class MateriAdapter(private val clickListener: MateriClickListener) : RecyclerView.Adapter<MateriAdapter.MyViewHolder>() {

    private var mapels: List<Map<String, String>> = emptyList()

    fun setMapels(mapels: List<Map<String, String>>) {
        this.mapels = mapels
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_materi, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(mapels[position], clickListener)
    }

    override fun getItemCount(): Int {
        return mapels.size
    }

    interface MateriClickListener {
        fun onMateriClick(mapel: Map<String, String>)
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textView: TextView = itemView.findViewById(R.id.item_materi)

        fun bind(mapel: Map<String, String>, clickListener: MateriClickListener) {
            textView.text = mapel["nama_module"]

            // Menambahkan onClickListener di dalam bind
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    clickListener.onMateriClick(mapel)
                }
            }
        }
    }
}
