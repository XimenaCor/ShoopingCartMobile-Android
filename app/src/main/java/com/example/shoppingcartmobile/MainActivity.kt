package com.example.shoppingcartmobile

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.shoppingcartmobile.adapters.ProductAdapter
import com.example.shoppingcartmobile.api.RetrofitClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

import android.widget.ImageView
import android.content.Intent

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var errorText: TextView
    private lateinit var adapter: ProductAdapter
    private lateinit var cartManager: CartManager  // NEW

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize views
        recyclerView = findViewById(R.id.recyclerViewProducts)
        progressBar = findViewById(R.id.progressBar)
        errorText = findViewById(R.id.tvError)

        // Make cart icon clickable
        findViewById<ImageView>(R.id.ivCartIcon).setOnClickListener {
            val intent = Intent(this, CartActivity::class.java)
            startActivity(intent)
        }

        // Initialize CartManager
        cartManager = CartManager(this)  // NEW

        // Setup RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = ProductAdapter(
            products = emptyList(),
            onItemClick = { product ->
                showToast("Viewing: ${product.name}")
                // We'll implement product details later
            },
            onAddToCart = { product ->  // NEW: Handle add to cart
                cartManager.addToCart(product)
                val count = cartManager.getItemCount()
                showToast("Added to cart! Total items: $count")
            }
        )
        recyclerView.adapter = adapter

        // Load products
        loadProducts()
    }

    private fun loadProducts() {
        progressBar.visibility = View.VISIBLE
        errorText.visibility = View.GONE

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = RetrofitClient.apiService.getProducts()

                withContext(Dispatchers.Main) {
                    progressBar.visibility = View.GONE

                    if (response.isSuccessful) {
                        val products = response.body() ?: emptyList()
                        adapter = ProductAdapter(
                            products = products,
                            onItemClick = { product ->
                                showToast("Viewing: ${product.name}")
                            },
                            onAddToCart = { product ->
                                cartManager.addToCart(product)
                                val count = cartManager.getItemCount()
                                showToast("Added to cart! Total items: $count")
                            }
                        )
                        recyclerView.adapter = adapter

                        if (products.isEmpty()) {
                            errorText.text = "No products available"
                            errorText.visibility = View.VISIBLE
                        }
                    } else {
                        errorText.text = "Failed to load products: ${response.message()}"
                        errorText.visibility = View.VISIBLE
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    progressBar.visibility = View.GONE
                    errorText.text = "Error: ${e.message}"
                    errorText.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun showToast(message: String) {
        android.widget.Toast.makeText(this, message, android.widget.Toast.LENGTH_SHORT).show()
    }
}