package com.mobile.heroes.mytournament.ui.paypal

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class PayPalViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "Fragment Paypal"
    }
    val text: LiveData<String> = _text
}