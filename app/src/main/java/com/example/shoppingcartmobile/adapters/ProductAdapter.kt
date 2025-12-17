package com.example.shoppingcartmobile.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.shoppingcartmobile.R
import com.example.shoppingcartmobile.Product

class ProductAdapter(
    private val products: List<Product>,
    private val onItemClick: (Product) -> Unit,
    private val onAddToCart: (Product) -> Unit  // NEW: Add to cart callback
) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.tvProductName)
        val price: TextView = itemView.findViewById(R.id.tvProductPrice)
        val description: TextView = itemView.findViewById(R.id.tvProductDescription)
        val addButton: Button = itemView.findViewById(R.id.btnAddToCart)  // NEW
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_product, parent, false)
        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = products[position]

        holder.name.text = product.name
        holder.price.text = "$${product.price}"
        holder.description.text = product.description ?: "No description"

        // Item click
        holder.itemView.setOnClickListener {
            onItemClick(product)
        }

        // Add to cart button click
        holder.addButton.setOnClickListener {
            onAddToCart(product)
        }
    }

    override fun getItemCount(): Int = products.size
}