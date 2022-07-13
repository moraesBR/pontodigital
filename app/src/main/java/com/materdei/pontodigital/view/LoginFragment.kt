package com.materdei.pontodigital.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import com.materdei.pontodigital.R
import com.materdei.pontodigital.databinding.FragmentLoginBinding
import com.materdei.pontodigital.viewmodel.UserViewModel

class LoginFragment : Fragment() {

    /* TODO 001.5: instanciar a classe de vinculação  */
    private lateinit var binding: FragmentLoginBinding

    /* TODO 002.5: instanciar a classe de dados, no caso via ViewModel */
    private lateinit var login: UserViewModel

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
        /* TODO 002.4: atrelar o binding ao ciclo de vida do fragment */
        binding.setLifecycleOwner(this)

        /* TODO 002.5: instanciar a classe de dados, no caso via ViewModel */
        login = ViewModelProvider(this)[UserViewModel::class.java]

        /* TODO 002.6: vincular o dado do binding ao dado real */
        binding.userViewModel = login

        /* TODO 001.5: instanciar a classe de vinculação  */
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        navigate()
    }

    private fun navigate(){
        /* TODO 002.7: teste de navegação */
        binding.loginButton.setOnClickListener { view ->
            //Navigation.findNavController(binding.root).navigate(R.id.action_loginFragment_to_homeFragment)
            view.findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
        }
    }
}