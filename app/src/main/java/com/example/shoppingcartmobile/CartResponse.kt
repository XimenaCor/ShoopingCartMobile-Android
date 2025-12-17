package com.example.shoppingcartmobile

data class CartResponse(
    val items: List<CartItem>,
    val total: Double
)