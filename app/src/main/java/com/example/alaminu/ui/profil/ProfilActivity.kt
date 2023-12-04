package com.example.alaminu.ui.profil

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.RadioGroup
import android.widget.TextView
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.alaminu.DbContract
import com.example.alaminu.LoginActivity
import com.example.alaminu.R
import com.example.alaminu.UserPreferences
import com.example.alaminu.databinding.FragmentProfileBinding
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject

class ProfilActivity : AppCompatActivity() {

    private lateinit var binding: FragmentProfileBinding
    private val userPreferences by lazy { UserPreferences(this) }
    private lateinit var editBoxz: com.google.android.material.textfield.TextInputEditText
    private lateinit var editBoxy: com.google.android.material.textfield.TextInputEditText
    private lateinit var dialog: AlertDialog
    private lateinit var requestQueue: RequestQueue

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        requestQueue = Volley.newRequestQueue(this)

        MainScope().launch {
            val username = userPreferences.getUsername() ?: ""
            val email = userPreferences.getEmail() ?: ""
            val noWa = userPreferences.getNowa() ?: ""

            binding.editUsername.setText(username)
            binding.editEmail.setText(email)
            binding.editNowa.setText(noWa)
        }

        binding.btnbayar.setOnClickListener {
            // Make an HTTP request to the PHP script using Volley
            MainScope().launch {
                val userId = userPreferences.getUserId() ?: ""
                val unpaidUrl = "${DbContract.urlCicilan}?id_pengguna=$userId"
                Log.d("UnpaidUrl", unpaidUrl)
                val unpaidJsonArrayRequest = JsonArrayRequest(
                    Request.Method.GET, unpaidUrl, null,
                    { unpaidResponse ->
                        if (unpaidResponse != null && unpaidResponse.length() > 0) {
                            // Handle the JSON response here
                            val unpaidPaymentData = parseJsonArray(unpaidResponse)

                            if (unpaidPaymentData.isNotEmpty()) {
                                // Get the first unpaid id_pembayaran
                                val unpaidIdPembayaran = unpaidPaymentData.first()["id_pembayaran"]

                                // Get the status value
                                val statusValue = unpaidPaymentData.first()["status"].toString()

                                showDialog(unpaidIdPembayaran.toString(), statusValue)
                            } else {
                                // Handle the case where there is no unpaid id_pembayaran
                                Toast.makeText(applicationContext, "Tidak ada pembayaran yang belum lunas", Toast.LENGTH_SHORT).show()
                            }
                        } else {
                            // Handle the case where the response is null or empty
                            Toast.makeText(applicationContext, "Data pembayaran tidak ditemukan", Toast.LENGTH_SHORT).show()
                        }
                    },
                    { unpaidError ->
                        unpaidError.printStackTrace()
                        Toast.makeText(applicationContext, "Terjadi kesalahan. Silakan coba lagi nanti.", Toast.LENGTH_SHORT).show()
                    }
                )

                requestQueue.add(unpaidJsonArrayRequest)
            }
        }

        binding.btnkeluar.setOnClickListener {
            logout()
        }
    }

    private fun parseJsonArray(jsonArray: JSONArray): List<Map<String, Any>> {
        val dataList = mutableListOf<Map<String, Any>>()

        for (i in 0 until jsonArray.length()) {
            val item = jsonArray.getJSONObject(i)
            val dataMap = mutableMapOf<String, Any>()

            // Extract data from JSON object
            dataMap["id_pembayaran"] = item.getInt("id_pembayaran")
            dataMap["bayar"] = item.getInt("bayar")
            dataMap["status"] = item.getInt("status")
            dataMap["id_program"] = item.getString("id_program")
            dataMap["tgl_pembayaran"] = item.getString("tgl_pembayaran")
            dataMap["id_pengguna"] = item.getInt("id_pengguna")

            dataList.add(dataMap)
        }

        return dataList
    }

    @SuppressLint("SetTextI18n")
    private fun showDialog(unpaidIdPembayaran: String, statusValue: String) {
        dialog = AlertDialog.Builder(this).create()
        val view = layoutInflater.inflate(R.layout.dialog_bayar, null)
        dialog.setView(view)  // Use dialog to set the view

        val radioGroup = view.findViewById<RadioGroup>(R.id.paymentOptions)
        val editBox2 =
            view.findViewById<com.google.android.material.textfield.TextInputLayout>(R.id.editBox2)
        editBoxz =
            view.findViewById<com.google.android.material.textfield.TextInputEditText>(R.id.emailEditText)
        editBoxy =
            view.findViewById<com.google.android.material.textfield.TextInputEditText>(R.id.bniEditText)
        val ivCopy = view.findViewById<ImageView>(R.id.ivCopy)
        val upload = view.findViewById<ImageView>(R.id.upload)
        val btnCancel = view.findViewById<Button>(R.id.btnCancel)
        val btnSelesai = view.findViewById<Button>(R.id.btnSelesai)
        val txtRemainingPayment = view.findViewById<TextView>(R.id.cicilan)

        // Inisialisasi awal visibility
        editBox2.visibility = View.GONE
        upload.visibility = View.GONE
        ivCopy.visibility = View.GONE

        // Listener untuk RadioButton pada dialog
        radioGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.radioPayOnSpot -> {
                    editBox2.visibility = View.GONE
                    upload.visibility = View.GONE
                    ivCopy.visibility = View.GONE
                }

                R.id.radioBankTransfer -> {
                    editBox2.visibility = View.VISIBLE
                    upload.visibility = View.VISIBLE
                    ivCopy.visibility = View.VISIBLE
                }
            }
        }

        btnCancel.setOnClickListener {
            dialog.dismiss()
        }

        btnSelesai.setOnClickListener {
            // Check if the entered nyicil amount is equal to the remaining payment amount
            val enteredNyicil = editBoxz.text.toString().toIntOrNull() ?: 0

            // Make the HTTP request to insert payment data with the unpaid id_pembayaran
            MainScope().launch {
                val userId = userPreferences.getUserId() ?: ""
                val status_pembayaran = "FALSE"
                val paymentUrl =
                    "${DbContract.urlBayar}?id_pengguna=$userId&id_pembayaran=$unpaidIdPembayaran&nyicil=$enteredNyicil&konfirmasi_admin=$status_pembayaran"
                Log.d("RequestURL", "URL: $paymentUrl")
                val paymentJsonObjectRequest = JsonObjectRequest(
                    Request.Method.GET, paymentUrl, null,
                    { paymentResponse ->
                        // Check if the response is a JSON object
                        if (paymentResponse is JSONObject) {
                            // Handle the JSON object response here
                            Log.d("PaymentResponse", paymentResponse.toString())
                            Toast.makeText(
                                applicationContext,
                                "Pembayaran Berhasil - Menuju Konfirmasi Admin",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            // Handle the case where the response is not a JSON object
                            Toast.makeText(
                                applicationContext,
                                "Respon tidak valid",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    },
                    { paymentError ->
                        // Handle the error response here
                        paymentError.printStackTrace()
                        Toast.makeText(
                            applicationContext,
                            "Terjadi kesalahana. Silakan coba lagi nanti.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                )
                requestQueue.add(paymentJsonObjectRequest)

            }
            dialog.dismiss()
        }

        if (dialog.window != null) {
            dialog.window!!.setBackgroundDrawable(ColorDrawable(0))
        }

        // Set the status value in the TextView
        txtRemainingPayment.text = "Rp. $statusValue"
        editBoxy.setText("143 00 1674952 3")

        ivCopy.setOnClickListener {
            copyTextToClipboard()
        }

        dialog.show()
    }

    private fun copyTextToClipboard() {
        val textToCopy = editBoxy.text.toString()

        val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("text", textToCopy)
        clipboard.setPrimaryClip(clip)
    }

    private fun logout() {
        MainScope().launch {
            userPreferences.clearAllPreferences()
            val intent = Intent(this@ProfilActivity, LoginActivity::class.java)
            startActivity(intent)
            finish()
            Toast.makeText(applicationContext, "Berhasil logout", Toast.LENGTH_SHORT).show()
        }
    }
}
