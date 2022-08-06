package com.materdei.pontodigital.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.materdei.pontodigital.di.AuthState
import com.materdei.pontodigital.di.DataSharedPreferences
import com.materdei.pontodigital.repository.AuthenticationRepository

/* 001.3: viewmodel para mediar a comunicação entre ui e classe de dados */
/* 003.13: Agora possui triplo papel. Por um lado, faz o binding via viewmodel para a view, obtendo
 dados para o login. Com estes dados, a viewmodel realiza a autenticação junto ao Firebase
 Authentication, sinalizando para a view se o login foi bem sucedido ou não. Por fim, trata do
 salvamento ou não dos dados obtidos na autenticação. */
class AuthenticationViewModel: ViewModel() {

    var email = MutableLiveData("")
    var password = MutableLiveData("")
    /* 003.14: Variável atrelada ao checkbox que indica se o login deve ou não ser salvo */
    var isChecked = MutableLiveData(false)

    private val authentication = AuthenticationRepository()
    fun getAuthentication() = authentication

    /* Realiza o login, sinalizando o resultado para uma ação. */
    fun login(action: (AuthState) -> Unit){
        authentication.login(email.value!!,password.value!!,action)
    }

    /* 003.15: Retorna o dado para ser salvo pelo SharedPreferences */
    fun saveData(): DataSharedPreferences.MainPreferences{
        return DataSharedPreferences.MainPreferences(email.value!!,password.value!!, isChecked.value!!)
    }

    /* 003.16: Atualiza os mutablelivedata com os dados salvos no sharedpreferences */
    fun restoreData(data: DataSharedPreferences.MainPreferences?){
        data?.let {
                email = MutableLiveData(it.email)
                password = MutableLiveData(it.password)
                isChecked = MutableLiveData(it.isChecked)
        }
    }
}