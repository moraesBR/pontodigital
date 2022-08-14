package com.materdei.pontodigital.di

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.os.Looper
import com.google.android.gms.location.*
import com.materdei.pontodigital.domain.model.Response
import com.materdei.pontodigital.utils.Constants.Companion.LOCATION_NOT_FOUND
import com.materdei.pontodigital.utils.Constants.Companion.LOCATION_PERMISSION_PROBLEM
import com.materdei.pontodigital.utils.Constants.Companion.LOCATION_TIME
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

/* TODO 005.04: captura do dado de geolocalização */
object LocationData  {

    private fun fusedLocationClient(context: Context): FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)

    private val locationRequest: LocationRequest = LocationRequest.create().apply {
        priority = Priority.PRIORITY_HIGH_ACCURACY
        interval = LOCATION_TIME
        fastestInterval = LOCATION_TIME / 4
    }

    @SuppressLint("MissingPermission")
    suspend fun get(context: Context): Flow<Response<Location>> = callbackFlow {

        val subscription = object : LocationCallback(){
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)
                for (location in locationResult.locations){

                    val result = if(location != null)
                        Response.Success(location)
                    else
                        Response.Error(LOCATION_NOT_FOUND)

                    trySend(result)

                }
            }
        }

        LocationPermission(context).requestLocation { isPermitted ->
            if (isPermitted)
                fusedLocationClient(context).requestLocationUpdates(locationRequest,subscription, Looper.myLooper())
            else{
                trySend(Response.Error(LOCATION_PERMISSION_PROBLEM))
            }
        }

        awaitClose {
            fusedLocationClient(context).removeLocationUpdates(subscription)
        }
    }

}