package com.materdei.pontodigital.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.materdei.pontodigital.R
import com.materdei.pontodigital.adapters.RegisterAdapter
import com.materdei.pontodigital.databinding.FragmentHomeBinding
import com.materdei.pontodigital.dto.Authentication
import com.materdei.pontodigital.viewmodel.AuthenticationViewModel
import com.materdei.pontodigital.viewmodel.RegisterViewModel

class HomeFragment : Fragment() {

    /* 001.5: declaração da classe de vinculação  */
    private lateinit var binding: FragmentHomeBinding
    private val authenticationViewModel: AuthenticationViewModel by viewModels()

    /* TODO 004:17: instanciamento do viewmodel registers */
    private val registerViewModel: RegisterViewModel by viewModels()
    /* TODO 004:18: instanciamento do adaptador registers */
    private var registerAdapter = RegisterAdapter(mutableListOf(), Authentication())

    override fun onStop() {
        super.onStop()
        registerViewModel.onStop(viewLifecycleOwner)
    }

    override fun onStart() {
        super.onStart()
        registerViewModel.onStart(viewLifecycleOwner)
    }

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

        binding()
        setting()

        /* 001.5: retorna layout com binding ao fragment  */
        return binding.root
    }

    private fun binding(){

        /* 002.4: binding associado ao ciclo de vida do fragment */
        binding.lifecycleOwner = this

        /* 002.5: passagem da referência da instância viewmodel (responsável pela comunicação
        *         entre ui e dados) ao binding do fragment */
        binding.userViewModel = authenticationViewModel

        /* TODO 004:19: Configuração inicial do recycler view */
        with(binding.registerRecyclerView){
            layoutManager = LinearLayoutManager(
                requireContext(),
                LinearLayoutManager.VERTICAL,
                false
            )

            adapter = registerAdapter
        }

    }

    private fun setting(){

        /* TODO 004.20: Atualização do adaptador do recycler view */
        authenticationViewModel.getAuthentication().observe(viewLifecycleOwner){
            registerAdapter.updateUser(it)
        }

        /* TODO 004.21: Atualização do adaptador do recycler view */
        registerViewModel.registers.observe(viewLifecycleOwner){
            Log.i("INFO",it.map { it.value }.toMutableList().toString())
            registerAdapter.updateRegisters(it.map { it.value }.toMutableList())
        }

    }

}