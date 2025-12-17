package com.example.shoppingcartmobile.api

import com.example.shoppingcartmobile.CartResponse
import com.example.shoppingcartmobile.Product
import retrofit2.Response
import retrofit2.http.*

interface ApiService {
    // Products
    @GET("/api/products")
    suspend fun getProducts(): Response<List<Product>>

    @GET("/api/products/{id}")
    suspend fun getProduct(@Path("id") id: Long): Response<Product>

    // Cart operations
    @GET("/cart")
    suspend fun getCart(): Response<CartResponse>

    @POST("/cart/add/{productId}")
    suspend fun addToCart(@Path("productId") productId: Long): Response<CartResponse>

    @POST("/cart/remove/{productId}")
    suspend fun removeFromCart(@Path("productId") productId: Long): Response<CartResponse>

    @POST("/cart/clear")
    suspend fun clearCart(): Response<Void>
}