package com.kasjan.model

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductDao {

    @Query("SELECT * FROM products WHERE day = :day AND month = :month AND year = :year AND userId = :userId")
    fun getProductsByDate(day: Int, month: Int, year: Int, userId: String): List<ShopProduct>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProduct(product: ShopProduct): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(products: List<ShopProduct>)

    @Delete
    suspend fun deleteProduct(product: ShopProduct)

    @Query("DELETE FROM products WHERE userId = :userId")
    suspend fun clearAllProducts(userId: String)

    @Query("SELECT * FROM products WHERE userId = :userId")
    fun getAllProductsFlow(userId: String): Flow<List<ShopProduct>>

}
