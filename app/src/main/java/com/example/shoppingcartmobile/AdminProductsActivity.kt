package com.example.shoppingcartmobile

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.shoppingcartmobile.adapters.AdminProductAdapter
import com.example.shoppingcartmobile.api.RetrofitClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AdminProductsActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var errorText: TextView
    private lateinit var btnAddProduct: Button
    private lateinit var adapter: AdminProductAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_products)

        // Initialize views
        recyclerView = findViewById(R.id.recyclerViewAdminProducts)
        progressBar = findViewById(R.id.progressBar)
        errorText = findViewById(R.id.tvError)
        btnAddProduct = findViewById(R.id.btnAddProduct)

        // Setup RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Load products
        loadProducts()

        // Add Product button
        btnAddProduct.setOnClickListener {
            val intent = Intent(this, ProductFormActivity::class.java)
            startActivity(intent)
        }

        // Back button in toolbar
        findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar).setOnClickListener {
            finish()
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
                        adapter = AdminProductAdapter(products,
                            onEditClick = { product ->
                                // Open edit form
                                val intent = Intent(this@AdminProductsActivity, ProductFormActivity::class.java)
                                intent.putExtra("PRODUCT_ID", product.id ?: 0)
                                startActivity(intent)
                            },
                            onDeleteClick = { product ->
                                // Delete product
                                deleteProduct(product.id ?: 0)
                            }
                        )
                        recyclerView.adapter = adapter

                        if (products.isEmpty()) {
                            errorText.text = "No products found"
                            errorText.visibility = View.VISIBLE
                        }
                    } else {
                        errorText.text = "Failed to load products"
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

    private fun deleteProduct(productId: Long) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = RetrofitClient.apiService.deleteProduct(productId)

                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        Toast.makeText(this@AdminProductsActivity, "Product deleted", Toast.LENGTH_SHORT).show()
                        loadProducts() // Refresh list
                    } else {
                        Toast.makeText(this@AdminProductsActivity, "Failed to delete product", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@AdminProductsActivity, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        loadProducts()
    }
}