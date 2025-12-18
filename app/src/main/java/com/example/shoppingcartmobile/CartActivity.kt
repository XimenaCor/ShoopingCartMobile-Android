package com.example.shoppingcartmobile

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.shoppingcartmobile.adapters.CartAdapter

class CartActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var emptyText: TextView
    private lateinit var subtotalText: TextView
    private lateinit var totalText: TextView
    private lateinit var checkoutButton: Button
    private lateinit var cartManager: CartManager
    private lateinit var adapter: CartAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)

        // Initialize views
        recyclerView = findViewById(R.id.recyclerViewCart)
        emptyText = findViewById(R.id.tvEmptyCart)
        subtotalText = findViewById(R.id.tvSubtotal)
        totalText = findViewById(R.id.tvTotal)
        checkoutButton = findViewById(R.id.btnCheckout)

        // Initialize CartManager
        cartManager = CartManager(this)

        // Setup RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Load cart items
        loadCartItems()

        // Checkout button click
        checkoutButton.setOnClickListener {
            checkout()
        }

        // Back button in toolbar
        findViewById<View>(R.id.toolbar).setOnClickListener {
            finish() // Go back to MainActivity
        }
    }

    private fun loadCartItems() {
        val cartItems = cartManager.getCartItems()
        val subtotal = cartManager.getCartTotal()

        // Update UI based on cart content
        if (cartItems.isEmpty()) {
            emptyText.visibility = View.VISIBLE
            recyclerView.visibility = View.GONE
            subtotalText.text = "$0.00"
            totalText.text = "$0.00"
            checkoutButton.isEnabled = false
        } else {
            emptyText.visibility = View.GONE
            recyclerView.visibility = View.VISIBLE

            // Update totals
            subtotalText.text = "$${String.format("%.2f", subtotal)}"
            totalText.text = "$${String.format("%.2f", subtotal)}" // No tax/shipping yet

            // Setup adapter
            adapter = CartAdapter(cartItems) { item ->
                // Remove item from cart
                cartManager.removeFromCart(item.productId)
                loadCartItems() // Refresh
                Toast.makeText(this, "Removed ${item.productName}", Toast.LENGTH_SHORT).show()
            }
            recyclerView.adapter = adapter

            checkoutButton.isEnabled = true
        }
    }

    private fun checkout() {
        val cartItems = cartManager.getCartItems()
        if (cartItems.isEmpty()) {
            Toast.makeText(this, "Cart is empty!", Toast.LENGTH_SHORT).show()
            return
        }

        // Show checkout confirmation
        val total = cartManager.getCartTotal()
        Toast.makeText(this,
            "Checkout complete! Total: $${String.format("%.2f", total)}",
            Toast.LENGTH_LONG).show()

        // Clear cart after checkout
        cartManager.clearCart()

        // SIMPLIFIED: Just go back to previous activity
        finish()
    }

    override fun onResume() {
        super.onResume()
        // Refresh cart when returning to this activity
        loadCartItems()
    }
}