package com.materdei.pontodigital.dto

/* 003.10: Classe para separar os sharedpreferences de cada fragment se houver */
sealed class DataSharedPreferences{
    class UserPreferences: DataSharedPreferences()
    class PontoPreferences: DataSharedPreferences()
    class HomePrefererences : DataSharedPreferences()
    class MainPreferences(val email: String, val password: String, val isChecked: Boolean) : DataSharedPreferences()
}