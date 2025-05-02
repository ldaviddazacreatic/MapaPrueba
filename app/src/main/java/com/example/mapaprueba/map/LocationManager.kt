package com.example.mapaprueba.map

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import com.mapbox.geojson.Point
import com.mapbox.maps.MapView
import com.mapbox.maps.plugin.LocationPuck2D
import com.mapbox.maps.plugin.locationcomponent.location
import com.example.mapaprueba.utils.ImageUtils

class LocationManager(private val mapView: MapView) {
    var lastKnownLocation: Point? = null

    fun enableLocation(context: Context) {
        if (ContextCompat.checkSelfPermission(
                context, Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            mapView.location.apply {
                locationPuck = LocationPuck2D().apply {
                    bearingImage = ImageUtils.drawableToImageHolder(context, com.mapbox.maps.R.drawable.mapbox_user_icon)
                    shadowImage = null
                    scaleExpression = null
                }
                enabled = true
                addOnIndicatorPositionChangedListener { point ->
                    lastKnownLocation = point
                }
            }
        }
    }
}
