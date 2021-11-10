package com.mobile.heroes.mytournament.ui.mistorneos

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MisTorneosViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "Fragment Mis torneos"
    }
    val text: LiveData<String> = _text
}