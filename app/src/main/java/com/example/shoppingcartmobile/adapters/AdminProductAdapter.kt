package com.example.shoppingcartmobile.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.shoppingcartmobile.R
import com.example.shoppingcartmobile.Product

class AdminProductAdapter(
    private val products: List<Product>,
    private val onEditClick: (Product) -> Unit,
    private val onDeleteClick: (Product) -> Unit
) : RecyclerView.Adapter<AdminProductAdapter.AdminProductViewHolder>() {

    class AdminProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.tvProductName)
        val price: TextView = itemView.findViewById(R.id.tvProductPrice)
        val quantity: TextView = itemView.findViewById(R.id.tvProductQuantity)
        val btnEdit: Button = itemView.findViewById(R.id.btnEdit)
        val btnDelete: Button = itemView.findViewById(R.id.btnDelete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdminProductViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_admin_product, parent, false)
        return AdminProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: AdminProductViewHolder, position: Int) {
        val product = products[position]

        holder.name.text = product.name
        holder.price.text = "$${product.price}"
        holder.quantity.text = "Qty: ${product.quantity}"

        holder.btnEdit.setOnClickListener {
            onEditClick(product)
        }

        holder.btnDelete.setOnClickListener {
            onDeleteClick(product)
        }
    }

    override fun getItemCount(): Int = products.size
}