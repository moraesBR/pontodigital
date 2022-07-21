package com.materdei.pontodigital

import android.database.DatabaseUtils
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.materdei.pontodigital.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    /* 001.5: declaração da classe de vinculação  */
    private lateinit var binding: ActivityMainBinding
    /* TODO 002.3: instanciar a classe de navegação */
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        /* 001.5: instância da classe de vinculação  */
        binding = DataBindingUtil.setContentView(this,R.layout.activity_main)

        /* TODO 002.3: instanciar a classe de navegação */
        navController = this.findNavController(R.id.nav_host_fragment)

        supportActionBar?.hide()
    }
}