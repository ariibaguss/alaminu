package com.example.alaminu.ui.notif

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.alaminu.R
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class NotificationsAdapter(
    private val tglPembayaran: String
) : RecyclerView.Adapter<NotificationsAdapter.ViewHolder>() {

    private var notifications = emptyList<Notifications>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_notifikasi, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val notification = notifications[position]

        // Mengambil hasil dari getNotificationMessage
        val messageResult = getNotificationMessage(notification, tglPembayaran)

        // Menetapkan teks ke textTitle dan textMessage
        holder.textTitle.text = messageResult.first
        holder.textMessage.text = messageResult.second
    }

    override fun getItemCount(): Int {
        return notifications.size
    }

    // Metode untuk mengganti data dalam adapter
    fun setData(newNotifications: List<Notifications>) {
        notifications = newNotifications
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textTitle: TextView = itemView.findViewById(R.id.textTitle)
        val textMessage: TextView = itemView.findViewById(R.id.textMessage)
    }

    private fun getNotificationMessage(notification: Notifications, tglPembayaran: String): Pair<String, String> {
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        try {
            val paymentDate = sdf.parse(tglPembayaran)
            val currentDate = Date()

            val daysDifference = ((currentDate.time - paymentDate.time) / (1000 * 60 * 60 * 24)).toInt()

            // Perubahan kondisi: notifikasi hanya muncul pada hari ke-25
            if (notification.status != 0 && daysDifference == 25) {
                val title = "Halo, ${notification}, nampaknya cicilan Anda bulan ini belum lunas"
                val message = "Masa paket program Anda bulan ini akan segera berakhir dalam beberapa hari. Segera lunasi cicilan Anda untuk melanjutkan."
                return Pair(title, message)
            }
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        // Jika tidak memenuhi kondisi, kembalikan pasangan nilai kosong
        return Pair("", "")
    }
}
