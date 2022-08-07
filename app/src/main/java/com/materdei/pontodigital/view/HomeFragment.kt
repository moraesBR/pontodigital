package com.materdei.pontodigital.view

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.materdei.pontodigital.R
import com.materdei.pontodigital.adapters.RegisterAdapter
import com.materdei.pontodigital.databinding.FragmentHomeBinding
import com.materdei.pontodigital.di.Authentication
import com.materdei.pontodigital.domain.model.Response
import com.materdei.pontodigital.utils.toRegister
import com.materdei.pontodigital.viewmodel.AuthenticationViewModel
import com.materdei.pontodigital.viewmodel.PunchViewModel

class HomeFragment : Fragment() {

    /* 001.5: declaração da classe de vinculação  */
    private lateinit var binding: FragmentHomeBinding
    private val authenticationViewModel: AuthenticationViewModel by viewModels()

    /* 004:18: instanciamento do viewmodel registers */
    private val punchViewModel: PunchViewModel by viewModels()

    /* 004:19: instanciamento do adaptador registers */
    private var registerAdapter = RegisterAdapter(listOf(), Authentication())

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

        /* 004:20: Configuração inicial do recycler view */
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

        /* 004.21: Atualização do adaptador do recycler view */
        authenticationViewModel.getAuthentication().observe(viewLifecycleOwner){
            registerAdapter.updateUser(it)
        }

        /* 004.22: Atualização do adaptador do recycler view */
        punchViewModel.fetching.observe(viewLifecycleOwner){ result ->
            when(result){
                is Response.Loading -> {
                    showingData(false)
                }
                is Response.Success -> {
                    Handler(Looper.getMainLooper()).postDelayed(
                        {
                            showingData(true)
                            registerAdapter.updateRegisters(result.data.toRegister())
                        },
                        1500 // value in milliseconds
                    )
                }
                is Response.Error -> {
                    Log.i("INFO","TCHAU")
                }
            }
        }

    }

    /* 004.23: Gerencia a visibilidade do recyclerview e progressbar */
    private fun showingData(isAvailable: Boolean){
        if (isAvailable){
            binding.registerRecyclerView.visibility = View.VISIBLE
            binding.waitingData.visibility = View.GONE
        }
        else{
            binding.registerRecyclerView.visibility = View.GONE
            binding.waitingData.visibility = View.VISIBLE
        }
    }
}