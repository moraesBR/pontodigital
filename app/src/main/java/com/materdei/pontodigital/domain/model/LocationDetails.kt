package com.materdei.pontodigital.domain.model

import android.location.Location
import android.location.LocationManager
import com.materdei.pontodigital.utils.Constants.Companion.LOCATION_MAX_DISTANCE
import com.materdei.pontodigital.utils.Constants.Companion.LOCATION_REFERENCE_LAT
import com.materdei.pontodigital.utils.Constants.Companion.LOCATION_REFERENCE_LON

/* TODO 005.02: dado de localização */
data class LocationDetails(val latitude: String, val longitude: String){

    constructor(location: Location) : this(location.latitude.toString(),location.longitude.toString())

    private val reference: Location = Location(LocationManager.GPS_PROVIDER).apply {
        latitude = LOCATION_REFERENCE_LAT
        longitude = LOCATION_REFERENCE_LON
    }

    private val maxDistance: Float = LOCATION_MAX_DISTANCE

    fun isNear(): Boolean{
        val local = Location(LocationManager.GPS_PROVIDER)
        local.latitude = this.latitude.toDouble()
        local.longitude = this.longitude.toDouble()

        return local.distanceTo(reference) <= maxDistance
    }
}