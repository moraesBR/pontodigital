package com.materdei.pontodigital.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.materdei.pontodigital.R
import com.materdei.pontodigital.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    /* 001.5: declaração da classe de vinculação  */
    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {

        /* 001.5: instância da classe de vinculação  */
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_home,
            container,
            false
        )

        /* 001.5: retorna layout com binding ao fragment  */
        return binding.root
    }

}