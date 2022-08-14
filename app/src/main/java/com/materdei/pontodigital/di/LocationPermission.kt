package com.materdei.pontodigital.di

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.materdei.pontodigital.utils.Constants.Companion.LOCATION_REQUIREMENT

/* TODO 005.03: avalia se as permissões para o uso dos recursos de geolocalização são atendidas */
class LocationPermission(private val context: Context) {

    private val locationManager: LocationManager =
        context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

    private val deviceEnabled: Boolean =
        locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) &&
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)

    private val checkPermission = (ContextCompat
        .checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) ==
            PackageManager.PERMISSION_GRANTED
            )

    fun requestLocation(request:(isAllowed:Boolean) -> Unit ) {
        if(deviceEnabled)
            request(checkPermission)
        else
            Toast.makeText(context,LOCATION_REQUIREMENT,Toast.LENGTH_SHORT).show()
    }

}