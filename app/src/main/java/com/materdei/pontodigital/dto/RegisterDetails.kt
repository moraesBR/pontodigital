package com.materdei.pontodigital.dto

/* TODO 004.13: classe de dados para tratar o recyclerview. */
data class RegisterDetails(
    val workday: Register.Workday,
    val punches: MutableList<Register.Punch>,
    var isNew: Boolean
){
    fun getKey() = workday.date
}