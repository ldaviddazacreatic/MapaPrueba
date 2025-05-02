package com.example.mapaprueba.map

import android.util.Log
import com.mapbox.geojson.FeatureCollection
import com.mapbox.maps.Style
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import com.mapbox.maps.extension.style.sources.generated.GeoJsonSource
import com.mapbox.maps.extension.style.sources.getSource

class GeoJsonLoader {
    private val client = OkHttpClient()

    fun loadGeoJson(url: String, style: Style) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val request = Request.Builder().url(url).build()
                client.newCall(request).execute().use { response ->
                    if (response.isSuccessful) {
                        val geoJson = response.body?.string()
                        geoJson?.let {
                            val source = style.getSource("populated-places")
                            if (source is GeoJsonSource) {
                                source.featureCollection(FeatureCollection.fromJson(it))
                            } else {
                                Log.e("GeoJsonLoader", "El source no es de tipo GeoJsonSource")
                            }
                        }
                    } else {
                        Log.e("GeoJsonLoader", "Respuesta no exitosa: ${response.code}")
                    }
                }
            } catch (e: Exception) {
                Log.e("GeoJsonLoader", "Error loading GeoJSON", e)
            }
        }
    }
}
