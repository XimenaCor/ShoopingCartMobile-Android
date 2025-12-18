package com.example.shoppingcartmobile

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.shoppingcartmobile.adapters.ProductAdapter
import com.example.shoppingcartmobile.api.RetrofitClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var errorText: TextView
    private lateinit var adapter: ProductAdapter
    private lateinit var cartManager: CartManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize views
        recyclerView = findViewById(R.id.recyclerViewProducts)
        progressBar = findViewById(R.id.progressBar)
        errorText = findViewById(R.id.tvError)

        // Initialize CartManager
        cartManager = CartManager(this)

        // Setup RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Load products
        loadProducts()

        // Cart icon click
        findViewById<ImageView>(R.id.ivCartIcon).setOnClickListener {
            val intent = Intent(this, CartActivity::class.java)
            startActivity(intent)
        }

        // Maps icon click
        findViewById<ImageView>(R.id.ivMaps).setOnClickListener {
            val intent = Intent(this, MapsActivity::class.java)
            startActivity(intent)
        }

        // Admin icon click
        findViewById<ImageView>(R.id.ivAdmin).setOnClickListener {
            if (AdminLoginActivity.isAdminLoggedIn(this)) {
                // Already logged in, go to dashboard
                val intent = Intent(this, AdminDashboardActivity::class.java)
                startActivity(intent)
            } else {
                // Need to login
                val intent = Intent(this, AdminLoginActivity::class.java)
                startActivity(intent)
            }
        }
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
                                Toast.makeText(this@MainActivity, "Viewing: ${product.name}", Toast.LENGTH_SHORT).show()
                            },
                            onAddToCart = { product ->
                                cartManager.addToCart(product)
                                val count = cartManager.getItemCount()
                                Toast.makeText(this@MainActivity, "Added to cart! Total items: $count", Toast.LENGTH_SHORT).show()
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
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}