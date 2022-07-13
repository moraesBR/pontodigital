package com.materdei.pontodigital.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.materdei.pontodigital.R
import com.materdei.pontodigital.databinding.FragmentHomeBinding
import com.materdei.pontodigital.databinding.FragmentLoginBinding

class LoginFragment : Fragment() {

    /* TODO 001.5: instanciar a classe de vinculação  */
    private lateinit var binding: FragmentLoginBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {

        /* TODO 001.5: instanciar a classe de vinculação  */
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_login,
            container,
            false
        )

        /* TODO 001.5: instanciar a classe de vinculação  */
        return binding.root
    }

}