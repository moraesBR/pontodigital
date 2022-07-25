package com.materdei.pontodigital.repository

import androidx.lifecycle.LiveData
import com.materdei.pontodigital.dto.Register
import com.materdei.pontodigital.utils.Constants
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/* TODO 004.12: Repositório para obter os dados de workday que são encapsulados em livedata. */
class WorkdayRepository : LiveData<MutableList<Register.Workday>>(mutableListOf()) {

    private val pathCollection by lazy {
            Constants.REGISTERVIEWMODEL_ROOT_COLLECTION +
            "/${FirebaseConnection.firebaseUser!!.uid}/" +
            Constants.REGISTERVIEWMODEL_WORKDAY_COLLECTION
    }

    fun get(){
        FirebaseConnection.getDocuments(pathCollection){ task ->
            CoroutineScope(Dispatchers.IO).launch {
                withContext(Dispatchers.Main) {
                    if(task.isSuccessful){
                        value = task.result.toObjects(Register.Workday::class.java).toMutableList()
                    }
                }
            }
        }
    }

    fun add(workday: Register.Workday){
        FirebaseConnection.addDocument(pathCollection, workday){ task ->
            CoroutineScope(Dispatchers.IO).launch {
                withContext(Dispatchers.Main) {
                    if (task.isSuccessful) {
                        value!!.add(workday)
                    }
                }
            }
        }
    }

    fun listening(){
        FirebaseConnection.checkForNewDocuments(pathCollection){ snapshot ->
            CoroutineScope(Dispatchers.IO).launch {
                withContext(Dispatchers.Main) {
                    snapshot.toObject(Register.Workday::class.java)?.let { value!!.add(it) }
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