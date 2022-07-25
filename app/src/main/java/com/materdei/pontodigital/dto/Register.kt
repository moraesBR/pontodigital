package com.materdei.pontodigital.dto

/* TODO 004.10: Modelo de dado para registro de ponto */
sealed class Register {

    abstract fun getKey(): String
    abstract fun toHashMap(): HashMap<String, String>

    data class Punch(val date: String, val time: String, val punch: String) : Register() {
        constructor() : this("", "", "")

        override fun getKey() = "$date@$time"

        override fun toHashMap(): HashMap<String, String> {
            return hashMapOf(
                "date" to date,
                "time" to time,
                "punch" to punch
            )
        }
    }

    data class Workday(val date: String, val hours: String, val justification: String = "") :
        Register() {
        constructor() : this("", "", "")

        override fun getKey() = date
        override fun toHashMap(): HashMap<String, String> {
            return hashMapOf(
                "date" to date,
                "hours" to hours,
                "justification" to justification
            )
        }
    }
}