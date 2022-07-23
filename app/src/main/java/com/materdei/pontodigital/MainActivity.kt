package com.materdei.pontodigital

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.materdei.pontodigital.databinding.ActivityMainBinding
import com.materdei.pontodigital.repository.AuthenticationRepository

class MainActivity : AppCompatActivity() {

    /* 001.5: declaração da classe de vinculação  */
    lateinit var binding: ActivityMainBinding
    /* 002.3: declaração da classe de navegação */
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        /* 001.5: instância da classe de vinculação  */
        binding = DataBindingUtil.setContentView(this,R.layout.activity_main)

        /* 002.3: instância da classe de navegação */
        navController = this.findNavController(R.id.nav_host_fragment)

        /* TODO 004.06: atrelar o navController ao bottom navigation */
        binding.bottomNavigation.setupWithNavController(navController)

        supportActionBar?.hide()

    }

    override fun onDestroy() {
        super.onDestroy()
    }
}