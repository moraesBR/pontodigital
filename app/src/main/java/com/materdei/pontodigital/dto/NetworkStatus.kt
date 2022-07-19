package com.materdei.pontodigital.dto

/* TODO 003.01: Sealed Class com duas classes para acompanhar o status da rede */
sealed class NetworkStatus {
    object Available : NetworkStatus()
    object Unavailable: NetworkStatus()
}
