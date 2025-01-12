package com.kasjan.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.kasjan.R
import com.kasjan.model.ShopProduct
import java.util.*

class ProductsAdapter(
    private val products: List<ShopProduct>
) : RecyclerView.Adapter<ProductsAdapter.ProductViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_product, parent, false)
        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.bind(products[position])
    }

    override fun getItemCount(): Int = products.size

    class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textViewProductName: TextView = itemView.findViewById(R.id.tv_product_name)

        fun bind(product: ShopProduct) {
            textViewProductName.text = product.name // Assuming your Product class has a 'name' property
        }
    }
}