package com.materdei.pontodigital.di

/* 003.08: Sealed Class para sinalizar o status da autenticação do usuário */
sealed class AuthState {
    object Success: AuthState()
    class Error(val msg: String): AuthState()
}
