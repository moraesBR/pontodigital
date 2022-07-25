package com.materdei.pontodigital.utils

enum class PunchCard(val value: String) {
    IN("ENTRADA"),OUT("SAÍDA");

    companion object {
        fun getPunchCardTypeByName(name: String) = when(name){
            IN.value ->IN
            OUT.value -> OUT
            else -> null
        }
    }
}
