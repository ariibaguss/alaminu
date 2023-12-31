package com.example.alaminu.ui.home.adapter.mapel

import android.graphics.BitmapFactory
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.alaminu.R

class Mapel : RecyclerView.Adapter<Mapel.MyViewHolder>() {

    private var mapels: List<Map<String, String>> = emptyList()

    fun setMapels(mapels: List<Map<String, String>>) {
        this.mapels = mapels
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_mapel, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        // Bind data to views based on the position in the mapels list
        holder.bind(mapels[position])
    }

    override fun getItemCount(): Int {
        // Return the size of the mapels list
        return mapels.size
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // Add a bind function to bind data to views
        fun bind(mapel: Map<String, String>) {
            val textView: TextView = itemView.findViewById(R.id.nama)
            val imageView: ImageView = itemView.findViewById(R.id.image)

            // Set the text from MapelModel to the TextView
            textView.text = mapel["nama"]

            // Load the image using Picasso into the ImageView if the path is not empty
            val imageBase64 = mapel["image"]
            if (!imageBase64.isNullOrBlank()) {
                val imageBytes = Base64.decode(imageBase64, Base64.DEFAULT)
                val decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
                imageView.setImageBitmap(decodedImage)
            } else {
                // Handle the case where the base64-encoded string is empty or null, for example, set a placeholder image
                imageView.setImageResource(R.drawable.science)
            }
        }
    }
}
