package com.example.shoppingcartmobile.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.shoppingcartmobile.R
import com.example.shoppingcartmobile.CartItem

class CartAdapter(
    private val cartItems: List<CartItem>,
    private val onRemoveClick: (CartItem) -> Unit
) : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    class CartViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.tvProductName)
        val price: TextView = itemView.findViewById(R.id.tvProductPrice)
        val quantity: TextView = itemView.findViewById(R.id.tvQuantity)
        val removeButton: Button = itemView.findViewById(R.id.btnRemove)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_cart, parent, false)
        return CartViewHolder(view)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val item = cartItems[position]

        holder.name.text = item.productName
        holder.price.text = "$${item.price}"
        holder.quantity.text = "Qty: ${item.quantity}"

        // Calculate item total
        val itemTotal = item.price * item.quantity
        holder.price.text = "$${itemTotal} ($${item.price} each)"

        // Remove button click
        holder.removeButton.setOnClickListener {
            onRemoveClick(item)
        }
    }

    override fun getItemCount(): Int = cartItems.size
}