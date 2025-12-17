package com.example.shoppingcartmobile

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class CartManager(private val context: Context) {

    companion object {
        private const val PREFS_NAME = "cart_prefs"
        private const val CART_ITEMS_KEY = "cart_items"
    }

    private val sharedPref: SharedPreferences =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    private val gson = Gson()

    // Add product to cart
    fun addToCart(product: Product) {
        val currentCart = getCartItems().toMutableList()

        // Check if product already in cart
        val existingItem = currentCart.find { it.productId == product.id }
        if (existingItem != null) {
            // Increase quantity
            existingItem.quantity += 1
        } else {
            // Add new item
            currentCart.add(
                CartItem(
                    productId = product.id ?: 0,
                    productName = product.name,
                    price = product.price,
                    quantity = 1
                )
            )
        }

        saveCartItems(currentCart)
    }

    // Get all cart items
    fun getCartItems(): List<CartItem> {
        val json = sharedPref.getString(CART_ITEMS_KEY, "[]")
        val type = object : TypeToken<List<CartItem>>() {}.type
        return gson.fromJson(json, type) ?: emptyList()
    }

    // Remove item from cart
    fun removeFromCart(productId: Long) {
        val currentCart = getCartItems().toMutableList()
        currentCart.removeAll { it.productId == productId }
        saveCartItems(currentCart)
    }

    // Clear entire cart
    fun clearCart() {
        sharedPref.edit().remove(CART_ITEMS_KEY).apply()
    }

    // Get cart total
    fun getCartTotal(): Double {
        return getCartItems().sumOf { it.price * it.quantity }
    }

    // Get item count
    fun getItemCount(): Int {
        return getCartItems().sumOf { it.quantity }
    }

    // Save cart items to SharedPreferences
    private fun saveCartItems(items: List<CartItem>) {
        val json = gson.toJson(items)
        sharedPref.edit().putString(CART_ITEMS_KEY, json).apply()
    }
}