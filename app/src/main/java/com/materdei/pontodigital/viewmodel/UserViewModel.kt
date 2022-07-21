package com.materdei.pontodigital.viewmodel

import android.view.View
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.materdei.pontodigital.model.User
import com.materdei.pontodigital.utils.Constants.Companion.LOGIN_EMPTY_DATA
import com.materdei.pontodigital.utils.Constants.Companion.LOGIN_INVALID_DATA
import com.materdei.pontodigital.utils.isEmailValid
import com.materdei.pontodigital.utils.isPasswordValid

/* 001.3: viewmodel para mediar a comunicação entre ui e classe de dados */
class UserViewModel : ViewModel() {
    var email = MutableLiveData<String>("")
    var password = MutableLiveData<String>("")

    private var _user = MutableLiveData<User> ()
    val user: LiveData<User>
        get() = _user

    fun onClick(view: View){
        if (email.value.isNullOrBlank() && password.value.isNullOrBlank())
            if(email.value!!.isEmailValid() && password.value!!.isPasswordValid())
                _user.value = User(email.value!!, password.value!!)
            else
                Toast.makeText(view.context,LOGIN_INVALID_DATA,Toast.LENGTH_SHORT).show()
        else
            Toast.makeText(view.context,LOGIN_EMPTY_DATA,Toast.LENGTH_SHORT).show()
    }
}