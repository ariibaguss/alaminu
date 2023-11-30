package com.example.alaminu.ui.jadwal

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.alaminu.R

class JadwalAdapter : RecyclerView.Adapter<JadwalAdapter.MyViewHolder>() {

    private var jadwal: List<JadwalEvent> = emptyList()
    private var filteredJadwal: List<JadwalEvent> = emptyList()

    fun setJadwal(jadwal: List<JadwalEvent>) {
        this.jadwal = jadwal
        filterByDate("")
    }

    fun filterByDate(selectedDate: String) {
        filteredJadwal = if (selectedDate.isNotEmpty()) {
            jadwal.filter { it.tanggal == selectedDate }
        } else {
            emptyList()
        }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_jadwal, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(filteredJadwal[position])
    }

    override fun getItemCount(): Int {
        return filteredJadwal.size
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(jadwal: JadwalEvent) {
            val textViewIdJadwal: TextView = itemView.findViewById(R.id.id_jadwal)
            val textViewJam: TextView = itemView.findViewById(R.id.jam)

            textViewIdJadwal.text = jadwal.nama
            textViewJam.text = jadwal.jam
        }
    }
}
