package com.kasjan.utils

import android.util.Log
import com.google.firebase.database.FirebaseDatabase
import com.kasjan.model.ShopProduct

class FirebaseHelper {
    private val databaseReference = FirebaseDatabase.getInstance().getReference("products")
//    // Dodaj produkt do Firebase
//    fun addProductToFirebase(product: ShopProduct) {
//        val productId = product.id.toString()
//        databaseReference.child(productId).setValue(product)
//    }
    fun addProductToFirebase(product: ShopProduct) {
        val database = FirebaseDatabase.getInstance()
        val productsRef = database.getReference("products")

        // Użyj pola 'id' jako klucza głównego w Firebase
        val productKey = product.id.toString()
        productsRef.child(productKey).setValue(product)
            .addOnSuccessListener {
                Log.d("FirebaseHelper", "Product added successfully with id: $productKey")
            }
            .addOnFailureListener { e ->
                Log.e("FirebaseHelper", "Failed to add product to Firebase", e)
            }
    }

     fun deleteProductFromFirebase(product: ShopProduct) {
         val productId = product.id.toString()
         Log.d("FirebaseHelper", "Attempting to delete product with ID: $productId")
        databaseReference.child(productId).removeValue()
            .addOnSuccessListener {
                Log.d("DayFragment", "Product deleted from Firebase: $productId")
            }
            .addOnFailureListener { exception ->
                Log.e("DayFragment", "Failed to delete product from Firebase", exception)
            }
    }

    // Pobierz produkty z Firebase
    fun getProductsFromFirebase(onSuccess: (List<ShopProduct>) -> Unit, onFailure: (Exception) -> Unit) {
        databaseReference.get().addOnSuccessListener { dataSnapshot ->
            val products = mutableListOf<ShopProduct>()
            for (snapshot in dataSnapshot.children) {
                val product = snapshot.getValue(ShopProduct::class.java)
                product?.let { products.add(it) }
            }
            onSuccess(products)
        }.addOnFailureListener {
            onFailure(it)
        }
    }
    fun deleteAllProductsFromFirebase() {
        val database = FirebaseDatabase.getInstance()
        val productsRef = database.getReference("products") // Zmień "products" na właściwą ścieżkę w Firebase
        productsRef.removeValue()
            .addOnSuccessListener {
                Log.d("FirebaseHelper", "All products deleted from Firebase successfully")
            }
            .addOnFailureListener { e ->
                Log.e("FirebaseHelper", "Failed to delete all products from Firebase", e)
            }
    }
}
