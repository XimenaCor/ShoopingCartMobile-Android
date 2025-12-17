package com.example.shoppingcartmobile

data class CartItem(
    val productId: Long,
    val productName: String,
    val price: Double,
    var quantity: Int
)