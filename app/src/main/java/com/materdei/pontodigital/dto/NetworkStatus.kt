package com.materdei.pontodigital.dto

/* 003.01: Classe para sinalizar o status da rede */
sealed class NetworkStatus {
    object Available : NetworkStatus()
    object Unavailable: NetworkStatus()
}
