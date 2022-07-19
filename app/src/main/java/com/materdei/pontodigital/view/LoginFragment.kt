package com.materdei.pontodigital.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.materdei.pontodigital.R
import com.materdei.pontodigital.databinding.FragmentLoginBinding
import com.materdei.pontodigital.dto.AuthState
import com.materdei.pontodigital.dto.DataSharedPreferences
import com.materdei.pontodigital.dto.NetworkStatus
import com.materdei.pontodigital.repository.AppSharedPreferences
import com.materdei.pontodigital.utils.Constants.Companion.LOGIN_NO_INTERNET
import com.materdei.pontodigital.utils.FragmentsID
import com.materdei.pontodigital.utils.printmsg
import com.materdei.pontodigital.viewmodel.AuthenticationViewModel
import com.materdei.pontodigital.viewmodel.NetworkStatusViewModel

class LoginFragment : Fragment() {

    /* TODO 001.5: instanciar a classe de vinculação  */
    private lateinit var binding: FragmentLoginBinding
    private lateinit var networkStatus: NetworkStatusViewModel

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

        setting()
        checkRequirement()
        clickable()

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        restorePreferences()
    }

    private fun setting(){
        /* TODO 002.4: atrelar o binding ao ciclo de vida do fragment */
        binding.lifecycleOwner = this

        networkStatus = ViewModelProvider(this)[NetworkStatusViewModel::class.java]

        /* TODO 002.6: vincular o dado do binding ao dado real */
        binding.authentication = ViewModelProvider(this)[AuthenticationViewModel::class.java]

    }

    /* TODO 003.04: Verifica se há rede disponível */
    private fun checkRequirement(){
        networkStatus.getNetworkStatus().observe(viewLifecycleOwner){
            when(it){
                is NetworkStatus.Available -> {
                    binding.loginLayout.visibility = View.VISIBLE
                    binding.warningLayout.visibility = View.GONE
                }
                is NetworkStatus.Unavailable -> {
                    binding.loginLayout.visibility = View.GONE
                    binding.warningLayout.visibility = View.VISIBLE
                    binding.warningMsg.text = LOGIN_NO_INTERNET
                }
            }
        }
    }

    /* TODO 003.15: efetua o login com/sem salvamentos dos dados */
    private fun clickable() {
        with(binding){
            loginButton.setOnClickListener {
                it.visibility = View.GONE
                waitingLogin.visibility = View.VISIBLE

                authentication!!.login{ authState ->
                    loginButton.visibility = View.VISIBLE
                    waitingLogin.visibility = View.GONE
                    when(authState){
                        is AuthState.Success -> {
                            savePreferences()
                            this@LoginFragment.findNavController()
                                .navigate(R.id.action_loginFragment_to_homeFragment)
                        }
                        is AuthState.Error -> {
                            printmsg(requireContext(),authState.msg)
                        }
                    }
                }
            }
        }
    }

    /* TODO 003.13: restaura os dados de login */
    private fun restorePreferences(){
        val savedPreferences = AppSharedPreferences.restore(requireContext(), FragmentsID.LOGIN)
                as DataSharedPreferences.MainPreferences
        binding.invalidateAll()
        binding.authentication!!.restoreData(savedPreferences)
    }

    /* TODO 003.14: salva os dados de login */
    private fun savePreferences(){
        if(binding.saveLogin.isChecked){
            AppSharedPreferences.save(
                requireContext(),
                FragmentsID.LOGIN,
                binding.authentication!!.saveData()
            )
        } else{
            AppSharedPreferences.save(
                requireContext(),
                FragmentsID.LOGIN,
                DataSharedPreferences.MainPreferences("","",false)
            )
        }
    }
}