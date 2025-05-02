package com.example.mapaprueba.map

import android.content.Context
import com.mapbox.maps.MapView
import com.mapbox.maps.Style
import com.mapbox.maps.extension.style.style

class MapManager(private val mapView: MapView) {
    var currentStyleUri: String = Style.MAPBOX_STREETS

    fun loadMapStyle(context: Context, onStyleLoaded: () -> Unit) {
        mapView.getMapboxMap().loadStyle(
            styleExtension = style(currentStyleUri) {
                // Puedes agregar aquí fuentes/layers si quieres
            }
        ) { onStyleLoaded() }
    }

    fun toggleMapStyle(context: Context, onStyleLoaded: () -> Unit) {
        currentStyleUri = if (currentStyleUri == Style.MAPBOX_STREETS) {
            Style.SATELLITE_STREETS
        } else {
            Style.MAPBOX_STREETS
        }
        loadMapStyle(context, onStyleLoaded)
    }
}
