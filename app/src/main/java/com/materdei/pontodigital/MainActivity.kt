package com.materdei.pontodigital

import android.database.DatabaseUtils
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.materdei.pontodigital.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    /* TODO 001.5: instanciar a classe de vinculação  */
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        /* TODO 001.5: instanciar a classe de vinculação  */
        binding = DataBindingUtil.setContentView(this,R.layout.activity_main)

    }
}