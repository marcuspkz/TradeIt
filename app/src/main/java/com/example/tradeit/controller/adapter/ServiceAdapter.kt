package com.example.tradeit.controller.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.tradeit.R
import com.example.tradeit.controller.main.detail.ServiceDetailActivity
import com.example.tradeit.controller.statics.FirebaseFunctions
import com.example.tradeit.controller.statics.GlobalFunctions
import com.example.tradeit.model.Favourite
import com.example.tradeit.model.Service
import com.google.firebase.auth.FirebaseAuth

class ServiceAdapter(private var serviceList: List<Service> = emptyList()) : RecyclerView.Adapter<ServiceViewHolder>() {
    fun updateList(list: List<Service>) {
        serviceList = list
        notifyDataSetChanged()
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ServiceViewHolder {
        return ServiceViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_service, parent, false)
        )
    }
    override fun onBindViewHolder(holder: ServiceViewHolder, position: Int) {
        var firebaseAuth: FirebaseAuth
        holder.bind(serviceList[position])
        holder.itemView.setOnLongClickListener {
            firebaseAuth = FirebaseAuth.getInstance()
            val userId = firebaseAuth.currentUser?.uid.toString()
            val fav = Favourite("", userId, serviceList[position].serviceId, false)
            val context = holder.itemView.context
            FirebaseFunctions.favouriteExists(serviceList[position].serviceId) { exists ->
                if (exists) {
                    GlobalFunctions.showInfoDialog(
                        context,
                        "Error.",
                        "El servicio ${serviceList[position].title} ya está en favoritos."
                    )
                } else {
                    if (FirebaseFunctions.addFavourite(fav) != null) {
                        GlobalFunctions.showInfoDialog(
                            context,
                            "¡Favorito añadido!",
                            "Se ha añadido el servicio ${serviceList[position].title} a favoritos."
                        )
                    } else {
                        GlobalFunctions.showInfoDialog(
                            context,
                            "Error.",
                            "Hubo un problema al añadir el servicio a favoritos."
                        )
                    }
                }
            }
            true
        }
        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, ServiceDetailActivity::class.java)
            intent.putExtra("serviceId", serviceList[position].serviceId)
            holder.itemView.context.startActivity(intent)
        }
    }
    override fun getItemCount() = serviceList.size
}