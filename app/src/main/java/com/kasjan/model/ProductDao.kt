package com.kasjan.model

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Delete
import androidx.room.OnConflictStrategy
import kotlinx.coroutines.flow.Flow
import java.util.Date

@Dao
interface ProductDao {
    @Query("SELECT * FROM products WHERE day = :day AND month = :month AND year = :year")
    fun getProductsByDate(day: Int, month: Int, year: Int): List<ShopProduct>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProduct(product: ShopProduct)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(products: List<ShopProduct>)

    @Delete
    suspend fun deleteProduct(product: ShopProduct)

    @Delete
    suspend fun deleteAll(products: List<ShopProduct>)

    @Query("DELETE FROM products")
    suspend fun clearAllProducts()

    @Query("SELECT * FROM products")
    fun getAllProductsFlow(): Flow<List<ShopProduct>>

}
