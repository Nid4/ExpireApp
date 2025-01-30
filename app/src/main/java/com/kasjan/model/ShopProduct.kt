package com.kasjan.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "products")
data class ShopProduct(
    @PrimaryKey(autoGenerate = true) var id: Int = 0,
    val ean: Int = 0,
    var name: String = "",
    var category: String? = null,
    var quantity: Int? = 0,
    var day: Int = 1,
    var month: Int = 1,
    var year: Int = 1970,
    val userId: String = ""
) {
    // Empty constructor needed for Firebase
    constructor() : this(0, 0, "", null, 0, 1,1,1970, "")
}
