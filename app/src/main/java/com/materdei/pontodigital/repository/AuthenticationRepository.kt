package com.materdei.pontodigital.repository

import androidx.lifecycle.LiveData
import com.google.firebase.auth.FirebaseUser
import com.materdei.pontodigital.di.AuthState
import com.materdei.pontodigital.di.Authentication
import com.materdei.pontodigital.di.FirebaseConnection
import com.materdei.pontodigital.utils.Constants.Companion.LOGIN_DATA_INVALID
import com.materdei.pontodigital.utils.isEmailValid
import com.materdei.pontodigital.utils.isPasswordValid
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/* 003.09: Repositório que busca as informações remotamente via FirebaseAuthentication e as
*          armazena no livedate Authentication. */
/* 004.02: Todos os códigos referentes ao acesso no Firebase foram removidos daqui. Agora só
*   trata dos dados de usuário e sua devida conversão para o Authentication */
class AuthenticationRepository: LiveData<Authentication>() {

    /* adaptador que converte o FirebaseUser para Authentication */
    private fun FirebaseUser?.adapter(password: String): Authentication {
        val auth = Authentication()
        this?.let {
            email?.let {
                auth.email = it
                auth.password = password
            }
            displayName?.let { auth.name = it }
            phoneNumber?.let { auth.phonenumber = it }
            photoUrl?.let { auth.photouri = it.path.toString() }
            auth.uid = uid
        }
        return auth
    }

    /* realiza o login via Firebase Authentication */
    fun login(email: String, password: String, action: (AuthState) -> Unit){

        /* Verifica se o email e password são válidos. Senão, sinaliza erro de input */
        if (!email.isEmailValid() || !password.isPasswordValid()) {
            FirebaseConnection.firebaseUser = null
            action(AuthState.Error(LOGIN_DATA_INVALID))
            return
        }

        /* 004.3: agora a função só sabe se o login teve sucesso ou não, repassando uma função
        *   lambda tratando a autenticação em função deste resultado.  */
        FirebaseConnection.login(email,password){ task, user ->
            CoroutineScope(Dispatchers.IO).launch {
                withContext(Dispatchers.Main) {
                    if (task.isSuccessful) {
                        /* Caso o login seja bem sucedido, então é disparado uma coroutine de IO (rede)
                        * para capturar os dados de authentication. Sem isso, multiplas requisições
                        * serão disparadas, sendo que algumas não terão contexto apropriado resultando
                        * no crash do aplicativo. */
                        value = user.adapter(password)
                        action(AuthState.Success)

                    } else {
                        task.exception?.let {
                            action(AuthState.Error(it.localizedMessage ?: ""))
                        }
                    }
                }
            }
        }
    }
}