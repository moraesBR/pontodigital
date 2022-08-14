package com.materdei.pontodigital.viewmodel

import androidx.lifecycle.*
import com.materdei.pontodigital.domain.model.DataModel
import com.materdei.pontodigital.domain.model.Response
import com.materdei.pontodigital.domain.model.Response.*
import com.materdei.pontodigital.repository.PunchRepository

/* 004.15: ponto de comunicação entre a ui e os dados punches */
class PunchViewModel : ViewModel() {

    val punchRepository = PunchRepository()

    /* TODO 005.11: encapsula a resposta do próximo ponto em um livedata */
    val nextPunch: LiveData<Response<DataModel.Punch>> = liveData {
        emit(Loading)
        try {
            emitSource(PunchRepository().nextPunch().asLiveData())
        } catch (e: Exception){
            Error(e.message ?: e.toString())
        }
    }

    val fetching: LiveData<Response<List<DataModel.Punch>>> = liveData {
        emit(Loading)
        try {
            emitSource(punchRepository.get().asLiveData())
        } catch (e: Exception){
            emit(Error(e.message ?: e.toString()))
        }
    }

    fun adding(punch: DataModel.Punch): LiveData<Response<Void?>> = liveData {
        emit(Loading)
        try {
            emitSource(punchRepository.add(punch).asLiveData())
        } catch (e: Exception){
            emit(Error(e.message ?: e.toString()))
        }
    }

    fun deleting(key: String): LiveData<Response<Void?>> = liveData {
        emit(Loading)
        try {
            emitSource(punchRepository.delete(key).asLiveData())
        } catch (e: Exception){
            emit(Error(e.message ?: e.toString()))
        }
    }

}