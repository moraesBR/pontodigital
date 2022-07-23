package com.materdei.pontodigital.utils

import android.content.Context
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import java.text.DecimalFormat

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