package com.materdei.pontodigital.repository

import androidx.lifecycle.LiveData
import com.materdei.pontodigital.dto.Register
import com.materdei.pontodigital.utils.Constants.Companion.REGISTERVIEWMODEL_REGISTERS_COLLECTION
import com.materdei.pontodigital.utils.Constants.Companion.REGISTERVIEWMODEL_ROOT_COLLECTION
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/* TODO 004.11: Repositório para obter os dados de punch que são encapsulados em livedata. */
class PunchRepository : LiveData<MutableList<Register.Punch>>() {

    private val pathCollection by lazy {
            REGISTERVIEWMODEL_ROOT_COLLECTION +
            "/${FirebaseConnection.firebaseUser!!.uid}/" +
            REGISTERVIEWMODEL_REGISTERS_COLLECTION
    }

    fun get(){
        FirebaseConnection.getDocuments(pathCollection){ task ->
            CoroutineScope(Dispatchers.IO).launch {
                withContext(Dispatchers.Main) {
                    if(task.isSuccessful){
                        value = task.result.toObjects(Register.Punch::class.java).toMutableList()
                    }
                }
            }
        }
    }

    fun add(punch: Register.Punch){
        FirebaseConnection.addDocument(pathCollection, punch){ task ->
            CoroutineScope(Dispatchers.IO).launch {
                withContext(Dispatchers.Main) {
                    if (task.isSuccessful) {
                        value!!.add(punch)
                    }
                }
            }
        }
    }

    fun listening(){
        FirebaseConnection.checkForNewDocuments(pathCollection){ snapshot ->
            CoroutineScope(Dispatchers.IO).launch {
                withContext(Dispatchers.Main) {
                    snapshot.toObject(Register.Punch::class.java)?.let { value!!.add(it) }
                }
            }
        }
    }

    override fun onActive() {
        super.onActive()
        if (value == null) { get() }
        else { if (value!!.isNotEmpty()) { listening() } else { get() } }
    }
}