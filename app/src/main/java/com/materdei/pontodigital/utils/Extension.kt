package com.materdei.pontodigital.utils

import android.util.Patterns

fun String.isEmailValid(): Boolean{
    return Patterns.EMAIL_ADDRESS.matcher(this).matches()
}

fun String.isPasswordValid(): Boolean{
    return this.length > 6
}