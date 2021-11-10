package com.mobile.heroes.mytournament.ui.paypal

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.mobile.heroes.mytournament.databinding.FragmentPaypalBinding

class PayPalFragment : Fragment() {

    private lateinit var payPalViewModel: PayPalViewModel
    private var _binding: FragmentPaypalBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        payPalViewModel =
            ViewModelProvider(this).get(PayPalViewModel::class.java)

        _binding = FragmentPaypalBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textPaypal
        payPalViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}