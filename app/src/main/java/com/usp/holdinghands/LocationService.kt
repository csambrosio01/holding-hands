package com.usp.holdinghands

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Location
import android.os.Looper
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.usp.holdinghands.activities.PERMISSION_GRANTED_REQUEST_CODE
import java.util.*

const val TEN_MINUTES = 10*60*1000

interface LocationService {
    var fusedLocationClient: FusedLocationProviderClient
    var locationCallback: LocationCallback

    fun onRequestPermissionsResult(requestCode: Int, grantResults: IntArray) {
        when (requestCode) {
            PERMISSION_GRANTED_REQUEST_CODE -> {
                if ((grantResults.isNotEmpty() &&
                            grantResults[0] == PackageManager.PERMISSION_GRANTED)
                ) {
                    getLocation()
                } else {
                    //TODO: Maybe set a default location (Like São Paulo)?
                }
            }
        }
    }

    fun getLocation() {
        if (ActivityCompat.checkSelfPermission(
                App.context!!,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                App.context!!,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            //In the future, we should implement this
//            ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)

            requestPermissions()
            return
        }

        fusedLocationClient.lastLocation
            .addOnSuccessListener { location ->
                if (location != null && (Date().time - location.time) <= TEN_MINUTES) {
                    onLocationResult(location)
                } else {
                    requestLocation()
                }
            }
    }

    @SuppressLint("MissingPermission")
    fun requestLocation() {
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                getLocation()
                fusedLocationClient.removeLocationUpdates(locationCallback)
            }
        }

        val locationRequest = LocationRequest.create()?.apply {
            interval = 10000
            fastestInterval = 5000
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }

        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.getMainLooper()
        )
    }

    fun requestPermissions()

    fun onLocationResult(location: Location)
}
