package com.materdei.pontodigital.repository

import android.content.Context
import android.content.SharedPreferences
import com.materdei.pontodigital.dto.DataSharedPreferences
import com.materdei.pontodigital.utils.Constants.Companion.SHAREDPREFERENCES
import com.materdei.pontodigital.utils.Constants.Companion.SHAREDPREFERENCES_LOGIN_EMAIL
import com.materdei.pontodigital.utils.Constants.Companion.SHAREDPREFERENCES_LOGIN_PASSWORD
import com.materdei.pontodigital.utils.Constants.Companion.SHAREDPREFERENCES_LOGIN_SAVE
import com.materdei.pontodigital.utils.FragmentsID

/* 003.12: Singleton que salva e restaura os dados em função do fragment */
object AppSharedPreferences {

    fun save(context: Context, fragmentsID: FragmentsID, data: DataSharedPreferences){
        val editor = context
            .getSharedPreferences(SHAREDPREFERENCES, Context.MODE_PRIVATE).edit()

        when(fragmentsID){
            FragmentsID.USER -> saveUserPreferences(editor,data)
            FragmentsID.PONTO -> savePontoPreferences(editor,data)
            FragmentsID.HOME -> saveHomePreferences(editor,data)
            FragmentsID.LOGIN -> saveMainPreferences(editor,data)
        }
    }

    private fun saveUserPreferences(editor: SharedPreferences.Editor,
                                     data: DataSharedPreferences){

    }

    private fun savePontoPreferences(editor: SharedPreferences.Editor,
                                     data: DataSharedPreferences){

    }

    private fun saveHomePreferences(editor: SharedPreferences.Editor, data: DataSharedPreferences){

    }

    private fun saveMainPreferences(editor: SharedPreferences.Editor, data: DataSharedPreferences){
        val mainPreferences = data as DataSharedPreferences.MainPreferences

        editor.apply {
            putString(SHAREDPREFERENCES_LOGIN_EMAIL, mainPreferences.email)
            putString(SHAREDPREFERENCES_LOGIN_PASSWORD, mainPreferences.password)
            putBoolean(SHAREDPREFERENCES_LOGIN_SAVE, mainPreferences.isChecked)
        }.apply()

    }

    fun restore(context: Context, fragmentsID: FragmentsID): DataSharedPreferences {
        val sharedPreferences = context
            .getSharedPreferences(SHAREDPREFERENCES, Context.MODE_PRIVATE)

        return when(fragmentsID){
            FragmentsID.USER -> loadUserPreferences(sharedPreferences)
            FragmentsID.PONTO -> loadPontoPreferences(sharedPreferences)
            FragmentsID.HOME -> loadHomePreferences(sharedPreferences)
            FragmentsID.LOGIN -> loadMainPreferences(sharedPreferences)
        }
    }

    private fun loadUserPreferences(sharedPreferences: SharedPreferences):
            DataSharedPreferences.UserPreferences {
        return DataSharedPreferences.UserPreferences()
    }

    private fun loadPontoPreferences(sharedPreferences: SharedPreferences):
            DataSharedPreferences.PontoPreferences {
        return DataSharedPreferences.PontoPreferences()
    }

    private fun loadHomePreferences(sharedPreferences: SharedPreferences):
            DataSharedPreferences.HomePrefererences {
        return DataSharedPreferences.HomePrefererences()
    }

    private fun loadMainPreferences(sharedPreferences: SharedPreferences):
            DataSharedPreferences.MainPreferences {

        with(sharedPreferences) {
            val email = getString(SHAREDPREFERENCES_LOGIN_EMAIL, "").toString()
            val password = getString(SHAREDPREFERENCES_LOGIN_PASSWORD, "").toString()
            val isChecked = getBoolean(SHAREDPREFERENCES_LOGIN_SAVE,false)

            return DataSharedPreferences.MainPreferences(email,password,isChecked)
        }

    }

}