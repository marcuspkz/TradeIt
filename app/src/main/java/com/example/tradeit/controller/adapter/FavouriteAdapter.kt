package com.example.tradeit.controller.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.example.tradeit.R
import com.example.tradeit.controller.main.detail.ProductDetailActivity
import com.example.tradeit.controller.main.detail.ServiceDetailActivity
import com.example.tradeit.controller.main.fragments.FavouritesFragment
import com.example.tradeit.controller.statics.FirebaseFunctions
import com.example.tradeit.controller.statics.GlobalFunctions
import com.example.tradeit.model.Favourite
import java.util.Locale

class FavouriteAdapter(private var favouriteList: MutableList<Favourite>) : RecyclerView.Adapter<FavouriteViewHolder>() {
    fun updateList(list: MutableList<Favourite>) {
        favouriteList = list
        notifyDataSetChanged()
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavouriteViewHolder {
        return FavouriteViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_favourite, parent, false)
        )
    }
    override fun onBindViewHolder(holder: FavouriteViewHolder, position: Int) {
        holder.bind(favouriteList[position])
        if (favouriteList[position].isProduct) {
            holder.itemView.setOnClickListener {
                val intent = Intent(holder.itemView.context, ProductDetailActivity::class.java)
                intent.putExtra("productId", favouriteList[position].itemId)
                holder.itemView.context.startActivity(intent)
            }
        } else {
            holder.itemView.setOnClickListener {
                val intent = Intent(holder.itemView.context, ServiceDetailActivity::class.java)
                intent.putExtra("serviceId", favouriteList[position].itemId)
                holder.itemView.context.startActivity(intent)
            }
        }

        holder.itemView.setOnLongClickListener {
            val context = it.context
            AlertDialog.Builder(context)
                .setTitle("Confirmación")
                .setMessage("¿Seguro que deseas eliminar este favorito?")
                .setPositiveButton("Sí") { dialog, _ ->
                    holder.itemView.isEnabled = false
                    FirebaseFunctions.deleteFavourite(favouriteList[position].favId) { success ->
                        if (success) {
                            GlobalFunctions.showInfoDialog(
                                context,
                                "¡Favorito eliminado!",
                                "Se ha eliminado el elemento de favoritos."
                            )
                            favouriteList.removeAt(position)
                            notifyDataSetChanged()
                        } else {
                            GlobalFunctions.showInfoDialog(
                                context,
                                "Error.",
                                "No se ha podido eliminar el elemento de favoritos."
                            )
                        }
                        holder.itemView.isEnabled = true
                    }
                    dialog.dismiss()
                }
                .setNegativeButton("No") { dialog, _ ->
                    dialog.dismiss()
                }
                .create()
                .show()
            true
        }
    }
    override fun getItemCount() = favouriteList.size
}