package com.kasjan.model

import android.content.Context
import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProductRepository(private val context: Context) {

    fun syncFirebaseToRoom() {
        val databaseReference = FirebaseDatabase.getInstance().getReference("products")
        databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val productList = mutableListOf<ShopProduct>()
                for (productSnapshot in snapshot.children) {
                    val product = productSnapshot.getValue(ShopProduct::class.java)
                    if (product != null) {
                        productList.add(product)
                    }
                }
                CoroutineScope(Dispatchers.IO).launch {
                    val dao = AppDatabase.getDatabase(context).productDao()
                    dao.clearAllProducts() // Czyści tabelę
                    dao.insertAll(productList) // Wstaw nowe dane
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("FirebaseSync", "Failed to fetch data from Firebase: ${error.message}")
            }
        })

    }

    fun syncRoomToFirebase() {
        val dao = AppDatabase.getDatabase(context).productDao()
        CoroutineScope(Dispatchers.IO).launch {
            dao.getAllProductsFlow().collect { productList ->
                val databaseReference = FirebaseDatabase.getInstance().getReference("products")
                databaseReference.get().addOnSuccessListener { snapshot ->
                    val firebaseProducts = snapshot.children.mapNotNull { it.getValue(ShopProduct::class.java) }
                    val firebaseProductMap = firebaseProducts.associateBy { it.id }
                    for (product in productList) {
                        val firebaseProduct = firebaseProductMap[product.id]
                        if (firebaseProduct == null || firebaseProduct != product) {
                            // Zapisz tylko, jeśli produkt się zmienił
                            databaseReference.child(product.id.toString()).setValue(product)
                        }
                    }
                }
            }
        }
    }

}
