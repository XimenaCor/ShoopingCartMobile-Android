package com.example.shoppingcartmobile

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.shoppingcartmobile.api.RetrofitClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProductFormActivity : AppCompatActivity() {

    private lateinit var etName: EditText
    private lateinit var etDescription: EditText
    private lateinit var etPrice: EditText
    private lateinit var etQuantity: EditText
    private lateinit var btnSave: Button
    private lateinit var tvTitle: TextView

    private var productId: Long? = null // null for new, value for edit

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_form)

        // Initialize views
        etName = findViewById(R.id.etName)
        etDescription = findViewById(R.id.etDescription)
        etPrice = findViewById(R.id.etPrice)
        etQuantity = findViewById(R.id.etQuantity)
        btnSave = findViewById(R.id.btnSave)
        tvTitle = findViewById(R.id.tvTitle)

        // Check if editing existing product
        productId = intent.getLongExtra("PRODUCT_ID", -1L).takeIf { it != -1L }

        if (productId != null) {
            tvTitle.text = "Edit Product"
            loadProductDetails(productId!!)
        } else {
            tvTitle.text = "Add Product"
        }

        // Save button click
        btnSave.setOnClickListener {
            saveProduct()
        }

        // Back button in toolbar
        findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar).setOnClickListener {
            finish()
        }
    }

    private fun loadProductDetails(id: Long) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = RetrofitClient.apiService.getProduct(id)
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        val product = response.body()
                        product?.let {
                            etName.setText(it.name)
                            etDescription.setText(it.description ?: "")
                            etPrice.setText(it.price.toString())
                            etQuantity.setText(it.quantity.toString())
                        }
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@ProductFormActivity, "Error loading product", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun saveProduct() {
        val name = etName.text.toString().trim()
        val description = etDescription.text.toString().trim()
        val priceText = etPrice.text.toString().trim()
        val quantityText = etQuantity.text.toString().trim()

        // Validation
        if (name.isEmpty()) {
            etName.error = "Product name is required"
            return
        }
        if (priceText.isEmpty()) {
            etPrice.error = "Price is required"
            return
        }

        val price = priceText.toDoubleOrNull()
        if (price == null || price <= 0) {
            etPrice.error = "Enter a valid price"
            return
        }

        val quantity = quantityText.toIntOrNull() ?: 1

        val product = Product(
            id = productId,
            name = name,
            description = if (description.isEmpty()) null else description,
            price = price,
            quantity = quantity
        )

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = if (productId == null) {
                    // Create new product
                    RetrofitClient.apiService.createProduct(product)
                } else {
                    // Update existing product
                    RetrofitClient.apiService.updateProduct(productId!!, product)
                }

                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        val action = if (productId == null) "created" else "updated"
                        Toast.makeText(this@ProductFormActivity, "Product $action successfully!", Toast.LENGTH_LONG).show()
                        finish()
                    } else {
                        Toast.makeText(this@ProductFormActivity, "Failed to save product", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@ProductFormActivity, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}