package com.example.proyecto

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.proyecto.databinding.ActivityCiudadBinding
import com.squareup.picasso.Picasso

class adaptador(private val laciudad: List<ciudad>, private val onClickListener: (ciudad) -> Unit) : RecyclerView.Adapter<adaptador.ViewHolder>() {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = laciudad[position]
        holder.render(item)
    }

    override fun getItemCount(): Int {
        return laciudad.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): adaptador.ViewHolder {
        val vista = LayoutInflater.from(parent.context)
        return ViewHolder(vista.inflate(R.layout.activity_ciudad, parent, false), onClickListener)
    }

    class ViewHolder(val laVista: View, onClickListener: (ciudad) -> Unit) : RecyclerView.ViewHolder(laVista) {
        val itemB = ActivityCiudadBinding.bind(laVista)

        fun render(ciudada: ciudad) {
            itemB.elnombre.text = ciudada.nombre
            itemB.descrip.text = ciudada.descripcion
            itemB.puntuacion.text = ciudada.puntuacion.toString()
            Picasso.get().load(ciudada.foto).into(itemB.imagen)

            itemView.setOnClickListener {
                onClickListener(ciudada)
            }
        }
    }
}
