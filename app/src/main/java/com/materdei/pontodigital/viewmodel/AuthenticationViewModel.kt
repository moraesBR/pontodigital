package com.materdei.pontodigital.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.materdei.pontodigital.dto.AuthState
import com.materdei.pontodigital.dto.DataSharedPreferences
import com.materdei.pontodigital.repository.AuthenticationRepository

/* TODO 003.07: a classe UserViewModel é a porta de comunicação entre a view os dados */
class AuthenticationViewModel: ViewModel() {

    var email = MutableLiveData("")
    var password = MutableLiveData("")
    /* TODO 003.10: Variável atrelada ao checkbox que indica se o login deve ou não ser salvo */
    var isChecked = MutableLiveData(false)

    private val authentication = AuthenticationRepository()
    fun getAuthentication() = authentication

    fun login(action: (AuthState) -> Unit){
        authentication.login(email.value!!,password.value!!,action)
    }

    /* TODO 003.11: Retorna o dado para ser salvo pelo SharedPreferences */
    fun saveData(): DataSharedPreferences.MainPreferences{
        return DataSharedPreferences.MainPreferences(email.value!!,password.value!!, isChecked.value!!)
    }

    /* TODO 003.12: Atualiza os mutablelivedata com os dados salvos no sharedpreferences */
    fun restoreData(data: DataSharedPreferences.MainPreferences?){
        data?.let {
                email = MutableLiveData(it.email)
                password = MutableLiveData(it.password)
                isChecked = MutableLiveData(it.isChecked)
        }
    }
}