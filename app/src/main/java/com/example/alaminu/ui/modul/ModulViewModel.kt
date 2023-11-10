package com.example.alaminu.ui.modul

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ModulViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is module Fragment"
    }
    val text: LiveData<String> = _text
}