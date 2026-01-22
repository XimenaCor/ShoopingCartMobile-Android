package com.example.shoppingcartmobile

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class AdminDashboardActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_dashboard)

        // Logout button
        findViewById<Button>(R.id.btnLogout).setOnClickListener {
            AdminLoginActivity.logout(this)
            finish() // Go back to login
            Toast.makeText(this, "Logged out", Toast.LENGTH_SHORT).show()
        }

        // Manage Products button
        findViewById<Button>(R.id.btnManageProducts).setOnClickListener {
            val intent = Intent(this, AdminProductsActivity::class.java)
            startActivity(intent)
        }

        // Back button in toolbar
        findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar).setOnClickListener {
            finish()
        }
    }
}