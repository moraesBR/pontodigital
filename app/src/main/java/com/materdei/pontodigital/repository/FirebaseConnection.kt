package com.materdei.pontodigital.repository

import android.net.Uri
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.*
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.materdei.pontodigital.dto.Register

/* TODO 004.01: As conexões com o firebase e as requisições são tratadas via Singleton. Cada
    requisição recebe uma ação via função lambda, diminuindo, assim, o bloilerplate em relação aos
    códigos do firebase */
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