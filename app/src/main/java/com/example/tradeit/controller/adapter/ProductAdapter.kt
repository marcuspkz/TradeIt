package com.example.tradeit.controller.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.tradeit.R
import com.example.tradeit.model.Product

class ProductAdapter(private var productList: List<Product> = emptyList()) : RecyclerView.Adapter<ProductViewHolder>() {
    fun updateList(list: List<Product>) {
        productList = list
        notifyDataSetChanged()
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        return ProductViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_product, parent, false)
        )
    }
    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.bind(productList[position])
    }
    override fun getItemCount() = productList.size
}