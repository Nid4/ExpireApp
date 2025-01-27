package com.kasjan.utils

import android.util.Log
import com.google.firebase.database.FirebaseDatabase
import com.kasjan.model.ShopProduct

class FirebaseHelper {
    private val databaseReference = FirebaseDatabase.getInstance().getReference("products")
    // Dodaj produkt do Firebase
    fun addProductToFirebase(product: ShopProduct) {
        val productId = product.id.toString()
        databaseReference.child(productId).setValue(product)
    }

//     fun deleteProductFromFirebase(product: ShopProduct) {
//         val productId = product.id.toString()
//         Log.d("FirebaseHelper", "Attempting to delete product with ID: $productId")
//        databaseReference.child(productId).removeValue()
//            .addOnSuccessListener {
//                Log.d("DayFragment", "Product deleted from Firebase: $productId")
//            }
//            .addOnFailureListener { exception ->
//                Log.e("DayFragment", "Failed to delete product from Firebase", exception)
//            }
//    }
fun deleteProductFromFirebase(product: ShopProduct) {
    val productKey = product.ean.toString() // Zmień z id na ean
    Log.d("FirebaseHelper", "Attempting to delete product with key: $productKey")
    databaseReference.child(productKey).removeValue()
        .addOnSuccessListener {
            Log.d("DayFragment", "Product deleted from Firebase: $productKey")
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
        TODO("Not yet implemented")
    }
}
