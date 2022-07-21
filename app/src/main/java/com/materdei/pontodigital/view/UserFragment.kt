package com.materdei.pontodigital.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.materdei.pontodigital.R
import com.materdei.pontodigital.databinding.FragmentUserBinding
import com.materdei.pontodigital.dto.Authentication
import com.materdei.pontodigital.viewmodel.AuthenticationViewModel

class UserFragment : Fragment() {

    /* 001.5: declaração da classe de vinculação  */
    private lateinit var binding: FragmentUserBinding
    private lateinit var authentication: AuthenticationViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {

        /* 001.5: instância da classe de vinculação  */
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_user,
            container,
            false
        )

        setting()

        /* 001.5: retorna layout com binding ao fragment  */
        return binding.root
    }

    private fun setting() {
        Log.i("LOGIN","Entrando em User")
        authentication = ViewModelProvider(this)[AuthenticationViewModel::class.java]
    }

}