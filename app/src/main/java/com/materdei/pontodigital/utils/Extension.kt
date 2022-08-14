package com.materdei.pontodigital.utils

import android.content.Context
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import com.materdei.pontodigital.domain.model.DataModel
import java.text.DecimalFormat
import java.time.LocalDateTime

fun String.isEmailValid(): Boolean{
    return Patterns.EMAIL_ADDRESS.matcher(this).matches()
}

fun String.isPasswordValid(): Boolean{
    return this.length > 6
}
fun printmsg(context: Context, msg: String) {
    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
}

fun Int2String(time: Int):String {
    val hour = DecimalFormat("00").format(time/60)
    val minutes = DecimalFormat("00").format(time%60)
    Log.i("TIME","$time   $hour   $minutes")
    return "$hour:$minutes"
}

fun List<DataModel.Punch>.workedTime():String{
    var minutes = 0
    val isCorrect = this.checkPunches{ before, current ->
        minutes += workedMinutes(this[before].time,this[current].time)
    }
    return if (isCorrect) Int2String(minutes) else "ERROR"
}

private fun List<DataModel.Punch>.checkPunches(action: (before: Int, current: Int) -> Unit):Boolean{
    if (this.size%2 != 0 || this.size == 0 ) return false
    var result = true

    val lastIndex = this.lastIndex

    this.forEachIndexed { index, data ->
        if((index % 2 == 0 && PunchCard.OUT.value.equals(data.punch)) ||
            (index % 2 == 1 && PunchCard.IN.value.equals(data.punch))) {
            result = false
        }
        if(index <= lastIndex && index % 2 == 1)
            action(index - 1, index)
    }

    return result
}

private fun workedMinutes(start: String, end: String): Int {
    val mStart = start.substringBefore("-").toInt().times(60) +
            start.substringAfter("-").toInt()

    val mEnd = end.substringBefore("-").toInt().times(60) +
            end.substringAfter("-").toInt()

    return (mEnd - mStart)
}


fun List<DataModel.Punch>.toRegister(): List<DataModel.Register> = this
    .groupBy { it.date }
    .map { data ->
        DataModel.Register(
            DataModel.Workday(data.key, data.value.workedTime(), ""),
            data.value,
            false
        )
    }.toList()


fun getDate() = LocalDateTime.now().let {
    val mFormat = DecimalFormat("00")
    "${it.year}-${mFormat.format(it.month.value)}-${mFormat.format(it.dayOfMonth)}"
}

fun getTime() = LocalDateTime.now().let {
    val mFormat = DecimalFormat("00")
    "${mFormat.format(it.hour)}-${mFormat.format(it.minute)}"
}


