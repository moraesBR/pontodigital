package com.materdei.pontodigital.di

import android.net.Uri
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.*
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import kotlinx.coroutines.tasks.await
import com.google.firebase.firestore.ktx.toObjects
import com.google.firebase.ktx.Firebase
import com.materdei.pontodigital.domain.model.DataModel
import com.materdei.pontodigital.domain.model.Response
import com.materdei.pontodigital.domain.model.Response.Error
import com.materdei.pontodigital.domain.model.Response.Success
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow

/* 004.01: As conexões com o firebase e as requisições são tratadas via Singleton. Cada
    requisição recebe uma ação via função lambda, diminuindo, assim, o bloilerplate em relação aos
    códigos do firebase */
/* 004.12: Firebase connection trata também da comunicação com o firestore utizando flow */
object FirebaseConnection {
    private val firebaseAuth: FirebaseAuth
        get() = FirebaseAuth.getInstance()

    var firebaseUser: FirebaseUser? = null

    fun disconnect(){
        firebaseUser = null
        firebaseAuth.signOut()
    }

    fun login(email: String, password:String, action: (Task<AuthResult>, FirebaseUser?) -> Unit){
        firebaseAuth.signInWithEmailAndPassword(email,password)
            .addOnCompleteListener { task ->
                if(task.isSuccessful)
                    firebaseUser = firebaseAuth.currentUser
                else
                    firebaseUser = null

                action(task, firebaseUser)
            }
    }

    fun updateUser(name: String, uriPath: String, action:(Task<Void>, UserProfileChangeRequest) -> Unit){
        firebaseUser?.apply{
            val profileUpdates = userProfileChangeRequest {
                displayName = name
                photoUri = Uri.parse(uriPath)
            }

            updateProfile(profileUpdates).addOnCompleteListener { task ->
                action(task, profileUpdates)
            }
        }
    }

    fun reauthentication(email: String, password:String, action: (Task<Void>) -> Unit){
        val user = firebaseAuth.currentUser!!

        val credential = EmailAuthProvider.getCredential(email, password)

        user.reauthenticate(credential)
            .addOnCompleteListener {  task ->
                action(task)
            }
    }

    suspend inline fun <reified T: DataModel> getDocuments(pathCollection: String):
            Flow<Response<List<T>>> = callbackFlow {
        val subscription = Firebase.firestore
            .collection(pathCollection)
            .addSnapshotListener{ snapshot, e ->

                val response = if (snapshot != null){
                    val data = snapshot.toObjects<T>()
                    Success(data)
                } else {
                    Error(e?.message ?: e.toString())
                }

                trySend(response).isSuccess
            }

        awaitClose {
            subscription.remove()
        }
    }

    suspend inline fun <reified T: DataModel> addDocument(pathCollection: String, document: T) = flow {
        try {
            emit(Response.Loading)
            val addition = Firebase.firestore.collection(pathCollection)
                .document(document.getKey())
                .set(document.toHashMap())
                .await()
            emit(Success(addition))
        } catch (e: Exception){
            emit(Error(e.message ?: e.toString()))
        }

    }

    suspend fun deleteDocument(pathCollection: String, key:String) = flow {
        try {
            emit(Response.Loading)
            val deletion = Firebase.firestore.collection(pathCollection)
                .document(key)
                .delete()
                .await()
            emit(Success(deletion))
        } catch (e: Exception){
            emit(Error(e.message ?: e.toString()))
        }
    }

    /*
    fun getDocuments(pathCollection: String, action:(Task<QuerySnapshot>) -> Unit){
        firebaseUser?.let {
            Firebase.firestore.collection(pathCollection).get().addOnCompleteListener{ task ->
                action(task)
            }
        }
    }

    fun addDocument(pathCollection: String, document: Register, action: (Task<Void>) -> Unit){
        firebaseUser?.let {
            Firebase.firestore.collection(pathCollection)
                .document(document.getKey())
                .set(document.toHashMap())
                .addOnCompleteListener { task ->
                    action(task)
                }
        }
    }

    fun checkForNewDocuments(pathCollection: String, action: (DocumentSnapshot) -> Unit){
        firebaseUser?.let {
            Firebase.firestore.collection(pathCollection).document().addSnapshotListener{ snapshot, e ->
                if (e != null){
                    return@addSnapshotListener
                }
                if (snapshot != null && snapshot.exists()){
                    action(snapshot)
                }
            }
        }
    }
    */

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