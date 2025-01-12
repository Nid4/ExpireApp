package com.kasjan.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "products")
data class ShopProduct(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val ean: Int,
    val name: String,
    val category: String? = null,
    val quantity: Int,
    val expiryDate: Date
)
