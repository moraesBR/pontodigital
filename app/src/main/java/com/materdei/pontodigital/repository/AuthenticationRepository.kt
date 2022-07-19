package com.materdei.pontodigital.repository

import android.net.Uri
import androidx.lifecycle.LiveData
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.materdei.pontodigital.dto.AuthState
import com.materdei.pontodigital.dto.Authentication
import com.materdei.pontodigital.utils.Constants.Companion.LOGIN_INVALID_DATA
import com.materdei.pontodigital.utils.isEmailValid
import com.materdei.pontodigital.utils.isPasswordValid
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/* TODO 003.06: Criar a classe que busca as informações remotamente via FirebaseAuthentication.
               A classe FirebaseAuthentication alimenta o LiveData<User> */
class AuthenticationRepository: LiveData<Authentication>() {

    private lateinit var firebaseAuth: FirebaseAuth
    var firebaseUser: FirebaseUser? = null

    init {
        connect()
    }

    fun connect(){
        firebaseAuth = FirebaseAuth.getInstance()
    }

    fun disconnect(){
        firebaseUser = null
        firebaseAuth.signOut()
    }

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

    fun login(email: String, password: String, action: (AuthState) -> Unit){
        if (!email.isEmailValid() || !password.isPasswordValid()) {
            firebaseUser = null
            action(AuthState.Error(LOGIN_INVALID_DATA))
            return
        }

        firebaseAuth.signInWithEmailAndPassword(email,password)
            .addOnCompleteListener { task ->
                if(task.isSuccessful){
                    CoroutineScope(Dispatchers.IO).launch{
                        withContext(Dispatchers.Main){
                            firebaseUser = firebaseAuth.currentUser
                            value = firebaseUser.adapter(password)
                            action(AuthState.Success)
                        }
                    }
                }
                else{
                    task.exception?.let {
                        firebaseUser = null
                        action(AuthState.Error(it.localizedMessage?:""))
                    }
                }
            }
    }

    /*fun register(email: String, password: String){
        firebaseAuth.createUserWithEmailAndPassword(email,password)
            .addOnCompleteListener (app.mainExecutor) { task ->
                if (task.isSuccessful){
                    Toast.makeText(app.baseContext, LOGIN_CREATE_USER_SUCCESS,
                        Toast.LENGTH_SHORT).show()
                }
                else{
                    Toast.makeText(app.baseContext, LOGIN_CREATE_USER_FAILED,
                        Toast.LENGTH_SHORT).show()
                }
            }
    }*/

    /*fun delete(){
        val user = Firebase.auth.currentUser
        if (user != null)
            user.delete()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(app.baseContext, LOGIN_DELETE_OK,
                            Toast.LENGTH_SHORT).show()
                        postValue(Authentication())
                    }
                }
        else
            Toast.makeText(app.baseContext, LOGIN_DELETE_FAILED,
                Toast.LENGTH_SHORT).show()
    }*/

    fun updateUser(name: String, uriPath: String){
        val user = firebaseAuth.currentUser
        val profileUpdates = userProfileChangeRequest {
            displayName = name
            photoUri = Uri.parse(uriPath)

        }

        user!!.updateProfile(profileUpdates).addOnSuccessListener {
            value?.let {
                it.photouri = uriPath
                it.name = name
            }
        }
    }

/*    fun reauthentication(){
        val user = firebaseAuth.currentUser!!

        val credential = EmailAuthProvider
            .getCredential(value!!.email, value!!.password)

        user.reauthenticate(credential)
            .addOnCompleteListener { task ->
                if(task.isSuccessful)

                else

            }
    }*/
}