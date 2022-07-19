package com.materdei.pontodigital.dto

sealed class AuthState {
    object Success: AuthState()
    class Error(val msg: String): AuthState()
}
