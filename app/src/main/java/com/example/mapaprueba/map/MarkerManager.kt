package com.example.mapaprueba.map

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.os.Handler
import android.os.Looper
import androidx.core.content.ContextCompat
import com.example.mapaprueba.R
import com.mapbox.geojson.Point
import com.mapbox.maps.MapView
import com.mapbox.maps.extension.style.layers.properties.generated.TextAnchor
import com.mapbox.maps.plugin.annotation.annotations
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationOptions
import com.mapbox.maps.plugin.annotation.generated.createPointAnnotationManager
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationManager

class MarkerManager(private val context: Context, private val mapView: MapView) {

    private val annotationManager: PointAnnotationManager by lazy {
        mapView.annotations.createPointAnnotationManager()
    }

    // Agregar un marcador normal con texto
    fun addMarker(point: Point, title: String) {
        val markerBitmap = getBitmapFromDrawable(context, R.drawable.default_marker, 36)

        markerBitmap?.let { bitmap ->
            val pointAnnotationOptions = PointAnnotationOptions()
                .withPoint(point)
                .withIconImage(bitmap)
                .withTextField(title)
                .withTextSize(12.0)
                .withTextAnchor(TextAnchor.TOP)

            annotationManager.create(pointAnnotationOptions)
        }
    }

    // Agregar un marcador de alerta pulsante con texto
    fun addAlertMarker(point: Point, title: String) {
        val alertBitmap = getBitmapFromDrawable(context, R.drawable.pulse_circle, 36)
        val normalBitmap = getBitmapFromDrawable(context, R.drawable.default_marker, 36)

        if (alertBitmap != null && normalBitmap != null) {
            val pointAnnotationOptions = PointAnnotationOptions()
                .withPoint(point)
                .withIconImage(alertBitmap)
                .withTextField(title)
                .withTextSize(12.0)
                .withTextAnchor(TextAnchor.TOP)

            val annotation = annotationManager.create(pointAnnotationOptions)

            // Animación intermitente
            val handler = Handler(Looper.getMainLooper())
            var showAlert = false

            val runnable = object : Runnable {
                override fun run() {
                    val nextBitmap = if (showAlert) alertBitmap else normalBitmap
                    annotation.iconImageBitmap = nextBitmap
                    annotationManager.update(annotation)
                    showAlert = !showAlert
                    handler.postDelayed(this, 500) // Cambia cada 500ms
                }
            }

            handler.post(runnable)
        }
    }

    // Obtener un bitmap desde drawable escalado
    private fun getBitmapFromDrawable(context: Context, drawableResId: Int, sizeDp: Int): Bitmap? {
        val drawable: Drawable = ContextCompat.getDrawable(context, drawableResId) ?: return null
        val scale = context.resources.displayMetrics.density
        val sizePx = (sizeDp * scale).toInt()

        val bitmap = Bitmap.createBitmap(sizePx, sizePx, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, sizePx, sizePx)
        drawable.draw(canvas)

        return bitmap
    }
}
