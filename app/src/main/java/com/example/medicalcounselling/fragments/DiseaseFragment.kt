package com.example.medicalcounselling.fragments

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import com.example.medicalcounselling.Authentication.AuthenticationPageActivity
import com.example.medicalcounselling.R
import com.example.medicalcounselling.databinding.FragmentDiseaseBinding
import com.example.medicalcounselling.databinding.FragmentDiseaseBindingImpl
import com.example.medicalcounselling.databinding.FragmentHomeBinding
import com.google.firebase.auth.FirebaseAuth


class DiseaseFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binding: FragmentDiseaseBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_disease, container, false
        )

        return binding.root
    }

}