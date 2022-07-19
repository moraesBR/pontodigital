package com.materdei.pontodigital.dto

/* TODO 003.07: Classes relacionadas ao sharedpreferences por fragments */
sealed class DataSharedPreferences{
    class UserPreferences: DataSharedPreferences()
    class PontoPreferences: DataSharedPreferences()
    class HomePrefererences : DataSharedPreferences()
    class MainPreferences(val email: String, val password: String, val isChecked: Boolean) : DataSharedPreferences()
}