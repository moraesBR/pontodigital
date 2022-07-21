package com.materdei.pontodigital.dto

/* TODO 003.08: Sealed Class com duas classes para acompanhar o status da autenticação do usuário */
sealed class AuthState {
    object Success: AuthState()
    class Error(val msg: String): AuthState()
}
