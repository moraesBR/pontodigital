package com.materdei.pontodigital.viewmodel

import android.app.Application
import android.content.Context
import android.location.Location
import androidx.lifecycle.*
import com.materdei.pontodigital.domain.model.LocationDetails
import com.materdei.pontodigital.domain.model.Response
import com.materdei.pontodigital.domain.model.Response.*
import com.materdei.pontodigital.repository.LocationRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/* TODO 005.06: */
class LocationViewModel: ViewModel() {

    private val locationRepository = LocationRepository()

    fun get(context:Context): LiveData<Response<Location>> = liveData {
        emit(Loading)
        try {
            emitSource(locationRepository.get(context).asLiveData())
        } catch (e: Exception) {
            emit(Error(e.message ?: e.toString()))
        }
    }

    fun isNear(context: Context): LiveData<Response<Boolean>> = liveData {
        emit(Loading)
        try {
            val data = locationRepository.get(context).map { response ->
                when(response){
                    is Loading -> Loading
                    is Success -> Success(LocationDetails(response.data).isNear())
                    is Error -> Error(response.msg)
                }
            }.asLiveData()
            emitSource(data)
        } catch (e: Exception) {
            emit(Error(e.message ?: e.toString()))
        }
    }

}