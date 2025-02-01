package com.kasjan.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.kasjan.R
import com.kasjan.model.ShopProduct

class ProductsAdapter(
    private val products: MutableList<ShopProduct>,
    private val onProductDeleted: (ShopProduct) -> Unit
) : RecyclerView.Adapter<ProductsAdapter.ProductViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_product, parent, false)
        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = products[position] // Pobierz produkt z listy
        holder.bind(product)

        // Obsługa kliknięcia przycisku usunięcia
        holder.deleteButton.setOnClickListener {
            onProductDeleted(product) // Wywołaj callback usunięcia
            remove(product) // Usuń produkt za pomocą nowej funkcji `remove`
        }
    }

    override fun getItemCount(): Int = products.size

    // Funkcja do usuwania konkretnego produktu
    fun remove(deletedProduct: ShopProduct) {
        val position = products.indexOf(deletedProduct)
        if (position != -1) {
            products.removeAt(position) // Usuń produkt z listy
            notifyItemRemoved(position) // Powiadom adapter o usunięciu
        }
    }

    // Funkcja do aktualizacji całej listy produktów
    fun update(newProducts: List<ShopProduct>) {
        products.clear() // Wyczyść obecną listę
        products.addAll(newProducts) // Dodaj nowe elementy
        notifyDataSetChanged() // Powiadom adapter o pełnej zmianie
    }

    class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textViewProductName: TextView = itemView.findViewById(R.id.tv_product_name)
        private val textViewProductQuantity: TextView = itemView.findViewById(R.id.tv_quantity)
        private val textViewProductCategory: TextView = itemView.findViewById(R.id.tv_category)
        private val textViewProductEAN: TextView = itemView.findViewById(R.id.tv_ean)
        val deleteButton: ImageButton = itemView.findViewById(R.id.buttonDeleteProduct) // Przenieśliśmy tutaj

        fun bind(product: ShopProduct) {
            textViewProductName.text = "Name: ${product.name}"
            textViewProductQuantity.text = "Quantity: ${product.quantity}"
            textViewProductCategory.text = "Category: ${product.category}"
            textViewProductEAN.text = "EAN: ${product.ean}"

            // Tworzymy datę w formacie dd/MM/yyyy z pól day, month, year
            val formattedDate = "${product.day.toString().padStart(2, '0')}/" +
                    "${product.month.toString().padStart(2, '0')}/" +
                    "${product.year}"
        }
    }
}
