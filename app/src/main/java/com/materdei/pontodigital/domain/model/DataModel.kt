package com.materdei.pontodigital.domain.model

/* 004.10: Modelo de dado para registro de ponto */
sealed class DataModel {

    abstract fun getKey(): String
    abstract fun toHashMap(): HashMap<String, String>

    data class Punch(val date: String, val time: String, val punch: String) : DataModel() {
        constructor() : this("", "", "")

        override fun getKey() = "$date@$time"

        override fun toHashMap(): HashMap<String, String> {
            return hashMapOf(
                "date" to date,
                "time" to time,
                "punch" to punch
            )
        }

        /* TODO 005.09: teste de igual entre dois punch em função da data e horário */
        override fun equals(other: Any?): Boolean {
            other as Punch
            if( date == other.date && time == other.time) return true
            return false
        }

        override fun hashCode(): Int {
            var result = date.hashCode()
            result = 31 * result + time.hashCode()
            return result
        }
    }

    data class Workday(val date: String, val hours: String, val justification: String = "") :
        DataModel() {
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

    data class Register(
        val workday: Workday,
        val punches: List<Punch>,
        val isNew: Boolean
    ): DataModel(){

        override fun getKey() = workday.date

        override fun toHashMap(): HashMap<String, String> {
            return hashMapOf(
                "workday" to workday.toHashMap().toString(),
                "punches" to punches.map { it.toHashMap() }.toString()
            )
        }

    }

}



