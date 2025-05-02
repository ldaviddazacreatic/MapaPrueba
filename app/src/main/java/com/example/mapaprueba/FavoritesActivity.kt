package com.example.mapaprueba

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mapaprueba.data.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FavoritesActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorites)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerViewFavoritos)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val db = AppDatabase.getDatabase(this)

        lifecycleScope.launch {
            try {
                // Obtener todos los favoritos de la base de datos
                val favorites = withContext(Dispatchers.IO) {
                    db.favoritePointDao().getAll()
                }

                // Si no hay favoritos, mostrar un mensaje
                if (favorites.isEmpty()) {
                    Toast.makeText(this@FavoritesActivity, "No hay favoritos guardados", Toast.LENGTH_SHORT).show()
                }

                // Configurar el adaptador con la lista de favoritos y la lógica de eliminar
                recyclerView.adapter = FavoriteAdapter(favorites,
                    onClick = { point ->
                        val intent = Intent()
                        intent.putExtra("latitude", point.latitude)
                        intent.putExtra("longitude", point.longitude)
                        setResult(RESULT_OK, intent)
                        finish()
                    },
                    onDelete = { point ->
                        // Eliminar el favorito de la base de datos
                        lifecycleScope.launch {
                            withContext(Dispatchers.IO) {
                                db.favoritePointDao().delete(point)
                            }

                            // Actualizar la lista después de eliminar el favorito
                            val updatedList = withContext(Dispatchers.IO) {
                                db.favoritePointDao().getAll()
                            }

                            // Actualizar el adaptador
                            (recyclerView.adapter as FavoriteAdapter).updateList(updatedList)

                            // Mostrar mensaje de eliminación
                            Toast.makeText(this@FavoritesActivity, "Favorito eliminado", Toast.LENGTH_SHORT).show()
                        }
                    }
                )
            } catch (e: Exception) {
                Toast.makeText(this@FavoritesActivity, "Error al cargar favoritos: ${e.message}", Toast.LENGTH_LONG).show()
                e.printStackTrace()
            }
        }

        // Botón para volver al mapa
        findViewById<Button>(R.id.btnVolverMapa).setOnClickListener {
            finish()
        }
    }
}
