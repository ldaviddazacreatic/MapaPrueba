package com.example.mapaprueba

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mapaprueba.data.FavoritePoint

class FavoriteAdapter(
    private var items: List<FavoritePoint>,
    private val onClick: (FavoritePoint) -> Unit,
    private val onDelete: (FavoritePoint) -> Unit
) : RecyclerView.Adapter<FavoriteAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvName: TextView = itemView.findViewById(R.id.txtNombrePunto)
        val tvCoordenadas: TextView = itemView.findViewById(R.id.txtCoordenadas)
        val tvTipoPunto: TextView = itemView.findViewById(R.id.txtTipoPunto) // Nuevo TextView
        val btnVer: Button = itemView.findViewById(R.id.btnIrAlMapa)
        val btnEliminar: Button = itemView.findViewById(R.id.btnEliminarFavorito)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_favorite, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val point = items[position]
        holder.tvName.text = point.name
        holder.tvCoordenadas.text = "Lat: ${point.latitude}, Lon: ${point.longitude}"

        // Mostrar el tipo de punto (Alerta o Normal)
        val tipo = if (point.isAlert) "Alerta" else "Normal"
        holder.tvTipoPunto.text = "Tipo: $tipo"

        holder.btnVer.setOnClickListener { onClick(point) }
        holder.btnEliminar.setOnClickListener { onDelete(point) }
    }

    fun updateList(newItems: List<FavoritePoint>) {
        this.items = newItems
        notifyDataSetChanged()
    }
}

