package com.example.alaminu

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.alaminu.databinding.ActivityMainBinding
import com.example.alaminu.ui.home.HomeFragment
import com.example.alaminu.ui.jadwal.JadwalFragment
import com.example.alaminu.ui.notif.NotifFragment
import com.example.alaminu.ui.modul.ModulFragment
import com.example.alaminu.ui.profil.ProfilActivity
import com.example.alaminu.ui.profil.UserData
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import org.json.JSONArray

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val userPreferences by lazy { UserPreferences(this) }
    private val generateIdLock = Any()
    private var absenCounter = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        val userData = intent.getSerializableExtra("userData") as? UserData
        if (userData != null) {
            // Start ProfilActivity and pass user data
            val intent = Intent(applicationContext, ProfilActivity::class.java)
            intent.putExtra("userData", userData)
            startActivity(intent)
        } else {
            // Handle the case where user data is not available
        }

        replaceFragment(HomeFragment())
        binding.bottomNavigationView.background = null
        binding.bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> replaceFragment(HomeFragment())
                R.id.navigation_modul -> replaceFragment(ModulFragment())
                R.id.navigation_jadwal -> replaceFragment(JadwalFragment())
                R.id.navigation_notifikasi -> replaceFragment(NotifFragment())
            }
            true
        }

        binding.btnabsen.setOnClickListener {
            MainScope().launch {
                generateIdAbsen { idAbsen ->
                    Log.d("GeneratedIdAbsen", "Generated ID Absen: $idAbsen")

                    MainScope().launch {
                        val idPengguna = userPreferences.getUserId() ?: ""

                        getLastIdJadwalFromServer { lastIdJadwalList ->
                            val idJadwal = if (lastIdJadwalList.length() > 0) {
                                lastIdJadwalList.getJSONObject(0).optString("id_jadwal", "")
                            } else {
                                Toast.makeText(
                                    this@MainActivity,
                                    "Tidak ada jadwal ditemukan",
                                    Toast.LENGTH_SHORT
                                ).show()
                                return@getLastIdJadwalFromServer
                            }
                            sendAttendanceRequest(idAbsen, idJadwal, idPengguna, "hadir")
                        }
                    }
                }
            }
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager: FragmentManager = supportFragmentManager
        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frame_layout, fragment)
        fragmentTransaction.commit()
    }

    private fun getLastIdAbsenFromServer(callback: (JSONArray) -> Unit) {
        val lastIdUrl = "${DbContract.urlAbsener}"  // Replace with your actual URL

        val lastIdRequest = JsonArrayRequest(
            Request.Method.GET, lastIdUrl, null,
            { response ->
                // Check if the response is a JSON array
                if (response is JSONArray) {
                    callback.invoke(response)
                } else {
                    // Try to parse the response as a JSON array
                    try {
                        val jsonArray = JSONArray(response)
                        callback.invoke(jsonArray)
                    } catch (e: Exception) {
                        // Handle parsing error
                        Toast.makeText(this, "Error parsing response", Toast.LENGTH_SHORT).show()
                        callback.invoke(JSONArray())
                    }
                }
            },
            { error ->
                // Handle errors
                Toast.makeText(this, "Error Aoccurred: ${error.message}", Toast.LENGTH_SHORT).show()
                // If the response is null, invoke the callback with an empty array
                callback.invoke(JSONArray())
            }
        )

        // Add the request to the RequestQueue
        Volley.newRequestQueue(this).add(lastIdRequest)
    }

    private fun getLastIdJadwalFromServer(callback: (JSONArray) -> Unit) {
        val lastIdJadwalUrl = "${DbContract.urlJadwal}"  // Replace with your actual URL

        val lastIdJadwalRequest = JsonArrayRequest(
            Request.Method.GET, lastIdJadwalUrl, null,
            { response ->
                // Check if the response is a JSON array
                if (response is JSONArray) {
                    callback.invoke(response)
                } else {
                    // Try to parse the response as a JSON array
                    try {
                        val jsonArray = JSONArray(response)
                        callback.invoke(jsonArray)
                    } catch (e: Exception) {
                        // Handle parsing error
                        Toast.makeText(this, "Error parsing response", Toast.LENGTH_SHORT).show()
                        callback.invoke(JSONArray())
                    }
                }
            },
            { error ->
                // Handle errors
                Toast.makeText(this, "Error Aoccurred: ${error.message}", Toast.LENGTH_SHORT).show()
                // If the response is null, invoke the callback with an empty array
                callback.invoke(JSONArray())
            }
        )

        // Add the request to the RequestQueue
        Volley.newRequestQueue(this).add(lastIdJadwalRequest)
    }

    private fun generateIdAbsen(callback: (String) -> Unit) {
        synchronized(generateIdLock) {
            // Make a network request using Volley to get the last ID from the server
            getLastIdAbsenFromServer { lastIdAbsenList ->
                // Generate unique ID using timestamp
                val newId = generateUniqueAbsenId()

                // Convert newId to a string with a length of 3 digits
                val formattedId = newId.padStart(3, '0')

                callback.invoke(formattedId)
            }
        }
    }

    private fun generateUniqueAbsenId(): String {
        return "0${absenCounter++}".padStart(3, '0')
    }

    private fun sendAttendanceRequest(idAbsen: String, idJadwal: String, idPengguna: String, status: String) {

        val absenUrl = "${DbContract.urlAbsen}?id_absen=$idAbsen&id_jadwal=$idJadwal&id_pengguna=$idPengguna&status=$status"  // Replace with your actual URL
        Log.d("RequestURL", "URL: $absenUrl") // Tambahkan log ini

        val params = HashMap<String, String>()
        params["id_absen"] = idAbsen
        params["id_jadwal"] = idJadwal
        params["id_pengguna"] = idPengguna
        params["status"] = status

        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, absenUrl, null,
            { absenResponse ->
                Log.d("AbsenResponse", absenResponse.toString())
                // Handle the response from the server
                val message = absenResponse.optString("message", "")
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
            },
            { error ->
                // Handle errors
                Toast.makeText(this, "Error occurred: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        )

        Volley.newRequestQueue(this).add(jsonObjectRequest)
    }
}
