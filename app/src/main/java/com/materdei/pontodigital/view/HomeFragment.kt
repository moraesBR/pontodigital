package com.materdei.pontodigital.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.materdei.pontodigital.R
import com.materdei.pontodigital.databinding.FragmentHomeBinding
import com.materdei.pontodigital.viewmodel.AuthenticationViewModel

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

        /* 002.4: binding associado ao ciclo de vida do fragment */
        binding.lifecycleOwner = this

        /* 002.5: passagem da referência da instância viewmodel (responsável pela comunicação
        *         entre ui e dados) ao binding do fragment */
        binding.userViewModel = ViewModelProvider(this)[AuthenticationViewModel::class.java]

        binding.userViewModel!!.getAuthentication().observe(viewLifecycleOwner){
            binding.name.text = it!!.name
        }

        /* 001.5: retorna layout com binding ao fragment  */
        return binding.root
    }

}