package com.mobile.heroes.mytournament.ui.mistorneos

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.mobile.heroes.mytournament.databinding.FragmentMistorneosBinding

class MisTorneosFragment : Fragment() {

    private lateinit var misTorneosViewModel: MisTorneosViewModel
    private var _binding: FragmentMistorneosBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        misTorneosViewModel =
            ViewModelProvider(this).get(MisTorneosViewModel::class.java)

        _binding = FragmentMistorneosBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}