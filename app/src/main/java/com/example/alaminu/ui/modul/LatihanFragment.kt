package com.example.alaminu.ui.modul

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.example.alaminu.databinding.FragmentLatihanBinding

class LatihanFragment : Fragment() {

    private var _binding: FragmentLatihanBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLatihanBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.btnupload.setOnClickListener {
            showOptionsDialog()
        }

        return root
    }

    private fun showOptionsDialog() {
        val options = arrayOf("Kamera", "Pilih File")
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Pilih Sumber")
            .setItems(options) { _, which ->
                when (which) {
                    0 -> openCamera()
                    1 -> pickFile()
                }
            }
            .show()
    }

    private fun openCamera() {
        val cameraIntent = Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(cameraIntent, OPEN_CAMERA_REQUEST_CODE)
    }

    private fun pickFile() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "*/*" // Semua jenis file
        startActivityForResult(intent, PICK_FILE_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            OPEN_CAMERA_REQUEST_CODE -> {
                if (resultCode == Activity.RESULT_OK) {
                    val imageBitmap = data?.extras?.get("data") as Bitmap
                    binding.file.text = "Photo_${System.currentTimeMillis()}.jpg"
                    binding.btnupload.visibility = View.GONE
                }
            }
            PICK_FILE_REQUEST_CODE -> {
                if (resultCode == Activity.RESULT_OK) {
                    val selectedFileUri = data?.data
                    if (selectedFileUri != null) {
                        val fileName = selectedFileUri.lastPathSegment
                        binding.file.text = "$fileName"
                        binding.btnupload.visibility = View.GONE
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val OPEN_CAMERA_REQUEST_CODE = 2
        private const val PICK_FILE_REQUEST_CODE = 1
    }
}
