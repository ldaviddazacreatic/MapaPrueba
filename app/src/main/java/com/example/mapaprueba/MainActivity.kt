package com.example.mapaprueba

import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.lifecycle.lifecycleScope
import com.example.mapaprueba.data.AppDatabase
import com.example.mapaprueba.data.FavoritePoint
import com.example.mapaprueba.map.GeoJsonLoader
import com.example.mapaprueba.map.LocationManager
import com.example.mapaprueba.map.MapManager
import com.example.mapaprueba.map.MarkerManager
import com.mapbox.geojson.Point
import com.mapbox.maps.MapView
import com.mapbox.maps.plugin.gestures.gestures
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : ComponentActivity() {

    private lateinit var mapView: MapView
    private lateinit var mapManager: MapManager
    private lateinit var locationManager: LocationManager
    private lateinit var geoJsonLoader: GeoJsonLoader
    private lateinit var markerManager: MarkerManager

    private lateinit var database: AppDatabase

    private val geoJsonUrl =
        "https://d2ad6b4ur7yvpq.cloudfront.net/naturalearth3.3.0/ne_50m_populated_places_simple.geojson"

    private val locationPermissionRequest = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            enableLocation()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mapView = findViewById(R.id.mapView)
        mapManager = MapManager(mapView)
        locationManager = LocationManager(mapView)
        geoJsonLoader = GeoJsonLoader()
        markerManager = MarkerManager(this, mapView)
        database = AppDatabase.getDatabase(this)

        mapManager.loadMapStyle(this) {
            mapView.getMapboxMap().getStyle()?.let { style ->
                geoJsonLoader.loadGeoJson(geoJsonUrl, style)
            }
            enableLocation()
            setupMapClickListener()
            loadSavedFavoritePoints()
        }

        findViewById<Button>(R.id.btnCambiarEstilo).setOnClickListener {
            mapManager.toggleMapStyle(this) {
                mapView.getMapboxMap().getStyle()?.let { style ->
                    geoJsonLoader.loadGeoJson(geoJsonUrl, style)
                }
            }
        }

        findViewById<Button>(R.id.btnCentrarUbicacion).setOnClickListener {
            locationManager.lastKnownLocation?.let { location ->
                mapView.getMapboxMap().setCamera(
                    com.mapbox.maps.CameraOptions.Builder()
                        .center(location)
                        .zoom(14.0)
                        .build()
                )
            }
        }

        findViewById<Button>(R.id.btnVerFavoritos)?.setOnClickListener {
            val intent = Intent(this, FavoritesActivity::class.java)
            startActivityForResult(intent, 100)
        }
    }

    private fun setupMapClickListener() {
        mapView.gestures.addOnMapClickListener { point ->
            val opciones = arrayOf("Punto normal", "Punto tipo alerta")
            AlertDialog.Builder(this)
                .setTitle("¿Qué tipo de punto desea agregar?")
                .setItems(opciones) { _, tipoSeleccionado ->
                    val esAlerta = tipoSeleccionado == 1

                    val input = EditText(this)
                    input.hint = "Nombre del punto"

                    AlertDialog.Builder(this)
                        .setTitle("Asignar nombre")
                        .setView(input)
                        .setPositiveButton("Guardar") { _, _ ->
                            val nombre = input.text.toString().trim()
                            if (nombre.isEmpty()) {
                                Toast.makeText(
                                    this,
                                    "El nombre no puede estar vacío",
                                    Toast.LENGTH_SHORT
                                ).show()
                                return@setPositiveButton
                            }

                            val favorito = FavoritePoint(
                                name = nombre,
                                latitude = point.latitude(),
                                longitude = point.longitude(),
                                isAlert = esAlerta
                            )

                            lifecycleScope.launch {
                                val id = withContext(Dispatchers.IO) {
                                    database.favoritePointDao().insert(favorito)
                                }

                                Toast.makeText(
                                    this@MainActivity,
                                    "Punto guardado",
                                    Toast.LENGTH_SHORT
                                ).show()

                                val mapPoint = Point.fromLngLat(point.longitude(), point.latitude())
                                val title = "ID: $id - $nombre"

                                if (esAlerta) {
                                    markerManager.addAlertMarker(mapPoint, title)
                                } else {
                                    markerManager.addMarker(mapPoint, title)
                                }
                            }
                        }
                        .setNegativeButton("Cancelar", null)
                        .show()
                }
                .show()
            true
        }
    }

    private fun loadSavedFavoritePoints() {
        lifecycleScope.launch {
            val favoritePoints = withContext(Dispatchers.IO) {
                database.favoritePointDao().getAll()
            }

            favoritePoints.forEach { point ->
                val mapPoint = Point.fromLngLat(point.longitude, point.latitude)
                val title = " ${point.id} - ${point.name}"

                if (point.isAlert) {
                    markerManager.addAlertMarker(mapPoint, title)
                } else {
                    markerManager.addMarker(mapPoint, title)
                }
            }
        }
    }

    private fun enableLocation() {
        if (ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            locationPermissionRequest.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        } else {
            locationManager.enableLocation(this)

            val location = locationManager.lastKnownLocation
            if (location != null) {
                val point = Point.fromLngLat(location.longitude(), location.latitude())
                markerManager.addMarker(point, "Mi ubicación")
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 100 && resultCode == RESULT_OK) {
            val lat = data?.getDoubleExtra("latitude", 0.0) ?: return
            val lon = data.getDoubleExtra("longitude", 0.0)
            mapView.getMapboxMap().setCamera(
                com.mapbox.maps.CameraOptions.Builder()
                    .center(Point.fromLngLat(lon, lat))
                    .zoom(14.0)
                    .build()
            )
        }
    }
}
