package com.kasjan.model

import java.sql.Date

data class Product(
    val name: String,
    val category: String,
    val quantity: Int,
    val expiryDate: String
)
