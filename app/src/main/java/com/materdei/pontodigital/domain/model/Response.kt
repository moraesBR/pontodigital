package com.materdei.pontodigital.domain.model

/* TODO 004.11: Dado para informar o estado da transmissão assíncrona */
sealed class Response<out T>{
    object Loading: Response<Nothing>()
    data class Success<out T>(val data: T): Response<T>()
    data class Error(val msg: String): Response<Nothing>()
}
