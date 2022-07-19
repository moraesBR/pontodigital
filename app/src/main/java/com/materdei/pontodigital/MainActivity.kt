package com.materdei.pontodigital

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.materdei.pontodigital.databinding.ActivityMainBinding
import com.materdei.pontodigital.repository.AuthenticationRepository

class MainActivity : AppCompatActivity() {

    /* TODO 001.5: instanciar a classe de vinculação  */
    private lateinit var binding: ActivityMainBinding
    /* TODO 002.3: instanciar a classe de navegação */
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        /* TODO 001.5: instanciar a classe de vinculação  */
        binding = DataBindingUtil.setContentView(this,R.layout.activity_main)

        /* TODO 002.3: instanciar a classe de navegação */
        navController = this.findNavController(R.id.nav_host_fragment)

        supportActionBar?.hide()

    }

    override fun onDestroy() {
        super.onDestroy()
    }
}