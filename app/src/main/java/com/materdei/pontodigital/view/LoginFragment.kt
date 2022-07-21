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

    /* 001.5: declaração da classe de vinculação  */
    private lateinit var binding: FragmentLoginBinding

    /* 003.04: declaração de viewmodel para tratar do status de rede */
    private lateinit var networkStatus: NetworkStatusViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {

        /* 001.5: instância da classe de vinculação  */
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_login,
            container,
            false
        )

        setting()
        checkRequirement()
        clickable()

        /* 001.5: retorna layout com binding ao fragment  */
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        /* as preferências são restauradas em onStart porque o fragment entrará no estado on Stop
        * após o login ser efetuado. Quando retorna ao foreground, onStart será invocado. */
        restorePreferences()
    }

    private fun setting(){
        /* 002.4: binding associado ao ciclo de vida do fragment */
        binding.lifecycleOwner = this

        /* 003.05: armazena a referência da instância viewmodel para tratar o status de rede */
        networkStatus = ViewModelProvider(this)[NetworkStatusViewModel::class.java]

        /* 002.5: passagem da referência da instância viewmodel (responsável pela comunicação
        *         entre ui e dados) ao binding do fragment */
        binding.authentication = ViewModelProvider(this)[AuthenticationViewModel::class.java]

    }

    /* 003.06: Checa se há rede disponível.  */
    private fun checkRequirement(){
        networkStatus.getNetworkStatus().observe(viewLifecycleOwner){
            when(it){
                /* Se houver, libera o login; */
                is NetworkStatus.Available -> {
                    binding.loginLayout.visibility = View.VISIBLE
                    binding.warningLayout.visibility = View.GONE
                }
                /* senão, oculta o login e informa que não conexão  */
                is NetworkStatus.Unavailable -> {
                    binding.loginLayout.visibility = View.GONE
                    binding.warningLayout.visibility = View.VISIBLE
                    binding.warningMsg.text = LOGIN_NO_INTERNET
                }
            }
        }
    }

    /* 003.19: Efetua o login com/sem salvamentos dos dados */
    private fun clickable() {
        with(binding){
            loginButton.setOnClickListener {

                /* Esconde botão e apresenta loading para evitar multiplas requisições */
                it.visibility = View.GONE
                waitingLogin.visibility = View.VISIBLE

                authentication!!.login{ authState ->

                    /* Esconde loading e apresenta novamente o botão de login, seguido do resultado
                    * da requisição anterior */
                    loginButton.visibility = View.VISIBLE
                    waitingLogin.visibility = View.GONE

                    when(authState){
                        /* Se obteve sucesso, realiza salvamento do login (se requisitado
                        pelo usuário) e navController navega para o home fragment  */
                        is AuthState.Success -> {
                            savePreferences()
                            this@LoginFragment.findNavController()
                                .navigate(R.id.action_loginFragment_to_homeFragment)
                        }
                        /* Caso contrário, será informado qual erro de login ocorreu */
                        is AuthState.Error -> {
                            printmsg(requireContext(),authState.msg)
                        }
                    }
                }
            }
        }
    }

    /* 003.17: restaura os dados de login */
    private fun restorePreferences(){
        val savedPreferences = AppSharedPreferences.restore(requireContext(), FragmentsID.LOGIN)
                as DataSharedPreferences.MainPreferences
        binding.invalidateAll()
        binding.authentication!!.restoreData(savedPreferences)
    }

    /* 003.18: salva os dados de login */
    private fun savePreferences(){
        /* Se o checkbox for marcado, os dados no viewmodel serão salvos */
        if(binding.saveLogin.isChecked){
            AppSharedPreferences.save(
                requireContext(),
                FragmentsID.LOGIN,
                binding.authentication!!.saveData()
            )
        } else{
            /* Caso contrário, os valores defaults serão salvos */
            AppSharedPreferences.save(
                requireContext(),
                FragmentsID.LOGIN,
                DataSharedPreferences.MainPreferences("","",false)
            )
        }
    }
}