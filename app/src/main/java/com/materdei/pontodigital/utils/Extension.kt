package com.materdei.pontodigital.utils

import android.content.Context
import android.util.Patterns
import android.widget.Toast

fun String.isEmailValid(): Boolean{
    return Patterns.EMAIL_ADDRESS.matcher(this).matches()
}

fun String.isPasswordValid(): Boolean{
    return this.length > 6
}
fun printmsg(context: Context, msg: String) {
    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
}