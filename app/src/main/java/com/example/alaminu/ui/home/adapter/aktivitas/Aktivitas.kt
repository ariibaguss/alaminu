package com.example.alaminu.ui.home.adapter.aktivitas

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.alaminu.R

class Aktivitas: RecyclerView.Adapter<Aktivitas.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_aktivitas, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

    }

    override fun getItemCount(): Int {
        return 4
    }

    class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

    }
}