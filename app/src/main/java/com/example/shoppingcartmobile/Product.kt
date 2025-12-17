package com.example.shoppingcartmobile  // No changes needed - this is already correct!

data class Product(
    val id: Long? = null,
    val name: String,
    val description: String? = null,
    val price: Double,
    val quantity: Int = 0
)