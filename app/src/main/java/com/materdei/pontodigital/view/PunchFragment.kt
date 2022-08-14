package com.materdei.pontodigital.view

import android.Manifest
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.materdei.pontodigital.R
import com.materdei.pontodigital.databinding.FragmentPunchBinding
import com.materdei.pontodigital.di.Biometric
import com.materdei.pontodigital.domain.model.DataModel.Punch
import com.materdei.pontodigital.domain.model.Response.Loading
import com.materdei.pontodigital.domain.model.Response.Success
import com.materdei.pontodigital.domain.model.Response.Error
import com.materdei.pontodigital.utils.Constants.Companion.LOCATION_PERMISSION_REQUEST_CODE
import com.materdei.pontodigital.utils.Constants.Companion.LOCATION_TOO_DISTANCE
import com.materdei.pontodigital.utils.PunchCard
import com.materdei.pontodigital.viewmodel.*

class PunchFragment : Fragment() {

    /* 001.5: declaração da classe de vinculação  */
    private lateinit var binding: FragmentPunchBinding

    /* TODO 005.12: declaração dos viewmodels  */
    private val locationViewModel: LocationViewModel by viewModels()
    private val punchViewModel: PunchViewModel by viewModels()
    private val authenticationViewModel: AuthenticationViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {

        /* 001.5: instância da classe de vinculação  */
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_punch,
            container,
            false
        )

        /* 001.5: retorna layout com binding ao fragment  */
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        activity?.apply {
            findViewById<BottomNavigationView>(R.id.bottom_navigation)?.apply {
                if (!isVisible) visibility = View.VISIBLE
            }
        }

        isAtWorkplace()
    }


    /* TODO 005.13: Checa se o usuário está próximo do local de trabalho. */
    private fun isAtWorkplace(){
        locationViewModel.isNear(requireContext()).observe(viewLifecycleOwner) { result ->
                when (result) {
                    is Loading -> {
                        visibility(false, "Verificando se está próximo do local de trabalho...")
                    }
                    is Success -> {

                        if (result.data) preparePunch()
                        else visibility(false,LOCATION_TOO_DISTANCE)

                    }
                    is Error -> {
                        Log.i("INFO", "HERE 01!")
                        visibility(false,result.msg)
                        val permissionRequest =
                            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION)
                        ActivityCompat.requestPermissions(requireActivity(),
                            permissionRequest,
                            LOCATION_PERMISSION_REQUEST_CODE)
                    }
                }
            }
    }


    /* TODO 005.14: atualiza alguns componentes do fragment_punch */
    private fun visibility(isVisible: Boolean, msg: String = ""){
        if (isVisible){
            binding.punchLoading.visibility = View.INVISIBLE
            binding.punchCardviews.visibility = View.VISIBLE
        }
        else{
            binding.punchLoading.visibility = View.VISIBLE
            binding.punchCardviews.visibility = View.INVISIBLE
        }

        if (msg.isNotEmpty()){
            binding.punchButton.visibility = View.INVISIBLE
            binding.confirmPunch.visibility = View.VISIBLE
            binding.confirmPunch.text = msg
        } else {
            binding.punchButton.visibility = View.VISIBLE
            binding.confirmPunch.visibility = View.INVISIBLE
        }
        //Handler(Looper.getMainLooper()).postDelayed({ },1500)
    }

    /* TODO 005.15: prepara e exibe o próximo ponto */
    private fun preparePunch(){
        punchViewModel.lastAndNext.observe(viewLifecycleOwner){ result ->
            when(result){
                is Loading -> visibility(false,"Esperando os dados do ponto")
                is Success -> {
                    fillPunchCardView(result.data)

                    val newPunch = result.data.second
                    val punchCard = PunchCard.getPunchCardTypeByName(newPunch.punch)
                    setPunchButtonColor(punchCard!!)

                    visibility(true)
                    punchRegister(newPunch)
                }
                is Error -> {
                    visibility(false,result.msg)
                }
            }
        }
    }

    /* TODO 005.16: Ajusta a cor do botão de registro do punch em função do tipo de ponto. */
    private fun setPunchButtonColor(punchCard: PunchCard) =
        when (punchCard) {
            PunchCard.IN -> {
                binding.punchButton.apply {
                    setBackgroundColor(ContextCompat.getColor(context,R.color.blue_dark))
                }
                PunchCard.OUT
            }
            PunchCard.OUT -> {
                binding.punchButton.apply {
                    setBackgroundColor(ContextCompat.getColor(context,R.color.red))
                }
                PunchCard.IN
            }
        }

    /* TODO 005.17: preenche o formulário de ponto */
    private fun fillPunchCardView(punches: Pair<Punch,Punch>){
        authenticationViewModel.getAuthentication().observe(viewLifecycleOwner){
            binding.previouspunchNameWorker.text = it.name
            binding.nextpunchNameWorker.text = it.name
        }
        val lastPunch = punches.first

        binding.previouspunchPunchInfo.text = lastPunch.punch
        binding.previouspunchTimeInfo.text = lastPunch.time.replace("-",":")
        binding.previouspunchDateInfo.text = lastPunch.date
        when(lastPunch.punch){
            PunchCard.IN.value -> binding.previouspunchPunchImage.setImageResource(R.drawable.gowork)
            PunchCard.OUT.value -> binding.previouspunchPunchImage.setImageResource(R.drawable.gohome)
        }

        val nextPunch = punches.second
        binding.nextpunchPunchInfo.text = nextPunch.punch
        binding.nextpunchTimeInfo.text = nextPunch.time.replace("-",":")
        binding.nextpunchDateInfo.text = nextPunch.date
        when(nextPunch.punch){
            PunchCard.IN.value -> binding.nextpunchPunchImage.setImageResource(R.drawable.gowork)
            PunchCard.OUT.value -> binding.nextpunchPunchImage.setImageResource(R.drawable.gohome)
        }
    }

    /* TODO 005.18: realiza o registro do ponto no firebase */
    private fun punchRegister(data: Punch){
        binding.punchButton.setOnClickListener {
            Biometric.authentication(this){ isAllowed, msg ->
                if (isAllowed) {
                    punchViewModel.adding(data)
                        .observe(viewLifecycleOwner){ response ->
                            when(response){
                                is Loading -> visibility(true,"Registrando Ponto...")
                                is Success -> visibility(true,"Ponto registrado com sucesso!")
                                is Error   -> visibility(true,response.msg)
                            }
                        }
                }
                else visibility(true,msg)
            }
        }

    }
}