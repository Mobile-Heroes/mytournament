package com.mobile.heroes.mytournament

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter

class dropdown_types_tournament : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val typeTournament = resources.getStringArray(R.array.types)
        val arrayAdapter = ArrayAdapter(requireContext(),R.layout.dropdown_tournament_types, typeTournament)

        return inflater.inflate(R.layout.fragment_dropdown_types_tournament, container, false)
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }
}