package com.example.alaminu.ui.absen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.alaminu.databinding.FragmentAbsenBinding

class AbsenFragment : Fragment() {

    private var _binding: FragmentAbsenBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(AbsenViewModel::class.java)

        _binding = FragmentAbsenBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textAbsen
        homeViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}