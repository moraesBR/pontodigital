package com.materdei.pontodigital.viewmodel

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.materdei.pontodigital.dto.Register.*
import com.materdei.pontodigital.dto.RegisterDetails
import com.materdei.pontodigital.repository.PunchRepository
import com.materdei.pontodigital.repository.WorkdayRepository
import com.materdei.pontodigital.utils.Int2String
import com.materdei.pontodigital.utils.PunchCard

/* TODO 004.14: ViewModel para fazer comunicação entre UI e os Dados. Com base nos dados obtidos
*   nos repositórios punch e worday, este viewmodel prepara o dado register para ser utilizado
*   no recyclerview.  */
class RegisterViewModel : ViewModel() {

    private val punches = PunchRepository()
    private val workday = WorkdayRepository()

    /* Mapeia os dados do recyclerview */
    private val _registers = MutableLiveData<HashMap<String,RegisterDetails>>(hashMapOf())
    val registers: LiveData<HashMap<String,RegisterDetails>>
        get() = _registers

    /* Inicia a preparação do dado register através da observação dos livedatas. Deve ser chamada
    no onCreate do fragment/activity */
    fun onStart(owner: LifecycleOwner){

        workday.observe(owner){ workdayList ->
            val mapWorkday = hashMapOf<String, Workday>()
            workdayList.map {
                mapWorkday[it.date] = it
            }

            punches.observe(owner){ punchList ->
                val mapPunch = punchList.groupBy { it.date }

                mapPunch.keys.forEach { date ->
                    val pc = mapPunch[date] as MutableList<Punch>
                    val wd = mapWorkday[date]?: Workday(date,pc.workedTime())
                    val isNew = mapWorkday[date] == null

                    _registers.value!![date] = RegisterDetails(wd,pc,isNew)
                }

            }
        }
    }

    /* Encerra a observação dos dados. Deve ser chamada no onDestroy do fragment/activity */
    fun onStop(owner: LifecycleOwner){
        workday.removeObservers(owner)
        punches.removeObservers(owner)
    }

    private fun List<Punch>.workedTime(): String{
        var minutes = 0
        val isCorrect = this.checkPunches{ before, current ->
            minutes += workedMinutes(this[before].time,this[current].time)
        }

        return if (isCorrect) Int2String(minutes) else "ERROR"
    }

    private fun List<Punch>.checkPunches(action: (before: Int, current: Int) -> Unit):Boolean{
        if (this.size%2 != 0 || this.isEmpty()) return false

        val lastIndex = this.lastIndex

        this.forEachIndexed { index, punch ->
            if((index % 2 == 0 && PunchCard.OUT.value == punch.punch) ||
                (index % 2 == 1 && PunchCard.IN.value == punch.punch)) {
                return false
            }
            if(index <= lastIndex && index % 2 == 1)
                action(index - 1, index)
        }

        return true
    }

    private fun workedMinutes(start: String, end: String): Int {
        val mStart = start.substringBefore("-").toInt().times(60) +
                start.substringAfter("-").toInt()

        val mEnd = end.substringBefore("-").toInt().times(60) +
                end.substringAfter("-").toInt()

        return (mEnd - mStart)
    }
}