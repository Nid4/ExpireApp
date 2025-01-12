package com.kasjan.model

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Delete
import java.util.Date

@Dao
interface ProductDao {
    @Query("SELECT * FROM products WHERE expiryDate >= :startDate AND expiryDate < :endDate")
    fun getProductsByDateRange(startDate: Date, endDate: Date): List<ShopProduct>


    @Insert
    suspend fun insertProduct(product: ShopProduct)

    @Delete
    suspend fun deleteProduct(product: ShopProduct)
}
