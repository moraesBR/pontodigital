package com.materdei.pontodigital.repository

import android.content.Context
import android.location.Location
import com.materdei.pontodigital.di.LocationData
import com.materdei.pontodigital.domain.model.LocationDetails
import com.materdei.pontodigital.domain.model.Response
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/* TODO 005.05: repositório dos dados de localização */
class LocationRepository {

    suspend fun get(context: Context): Flow<Response<Location>> = LocationData.get(context)

}