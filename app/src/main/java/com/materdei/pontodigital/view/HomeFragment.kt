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

    /* TODO 001.5: instanciar a classe de vinculação  */
    private lateinit var binding: FragmentHomeBinding

    /* TODO 002.5: instanciar a classe de dados, no caso via ViewModel */
    private lateinit var login: AuthenticationViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {

        /* TODO 001.5: instanciar a classe de vinculação  */
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_home,
            container,
            false
        )

        /* TODO 002.4: atrelar o binding ao ciclo de vida do fragment */
        binding.lifecycleOwner = this

        /* TODO 002.5: instanciar a classe de dados, no caso via ViewModel */
        login = ViewModelProvider(this)[AuthenticationViewModel::class.java]

        /* TODO 002.6: vincular o dado do binding ao dado real */
        binding.userViewModel = login

        binding.userViewModel!!.getAuthentication().observe(viewLifecycleOwner){
            binding.name.text = it!!.name
        }

        /* TODO 001.5: instanciar a classe de vinculação  */
        return binding.root
    }

}