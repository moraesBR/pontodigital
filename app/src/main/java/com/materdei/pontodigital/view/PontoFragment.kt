package com.materdei.pontodigital.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.materdei.pontodigital.R
import com.materdei.pontodigital.databinding.FragmentLoginBinding
import com.materdei.pontodigital.databinding.FragmentPontoBinding

class PontoFragment : Fragment() {

    /* TODO 001.5: instanciar a classe de vinculação  */
    private lateinit var binding: FragmentPontoBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {

        /* TODO 001.5: instanciar a classe de vinculação  */
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_ponto,
            container,
            false
        )

        /* TODO 001.5: instanciar a classe de vinculação  */
        return binding.root
    }

}