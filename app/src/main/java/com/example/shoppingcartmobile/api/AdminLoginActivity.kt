package com.example.shoppingcartmobile

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class AdminLoginActivity : AppCompatActivity() {

    private lateinit var etUsername: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnLogin: Button
    private lateinit var tvError: TextView
    private lateinit var sharedPref: SharedPreferences

    companion object {
        private const val PREFS_NAME = "admin_prefs"
        private const val KEY_IS_LOGGED_IN = "is_logged_in"
        private const val ADMIN_USERNAME = "admin"
        private const val ADMIN_PASSWORD = "admin123"

        fun isAdminLoggedIn(context: android.content.Context): Boolean {
            val prefs = context.getSharedPreferences(PREFS_NAME, MODE_PRIVATE)
            return prefs.getBoolean(KEY_IS_LOGGED_IN, false)
        }

        fun logout(context: android.content.Context) {
            val prefs = context.getSharedPreferences(PREFS_NAME, MODE_PRIVATE)
            prefs.edit().putBoolean(KEY_IS_LOGGED_IN, false).apply()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_login)

        etUsername = findViewById(R.id.etUsername)
        etPassword = findViewById(R.id.etPassword)
        btnLogin = findViewById(R.id.btnLogin)
        tvError = findViewById(R.id.tvError)

        // Initialize SharedPreferences
        sharedPref = getSharedPreferences(PREFS_NAME, MODE_PRIVATE)

        // Check if already logged in
        if (isLoggedIn()) {
            goToAdminDashboard()
            return
        }

        // Login button click
        btnLogin.setOnClickListener {
            val username = etUsername.text.toString().trim()
            val password = etPassword.text.toString().trim()

            if (validateCredentials(username, password)) {
                saveLoginState(true)
                goToAdminDashboard()
            } else {
                tvError.visibility = TextView.VISIBLE
                Toast.makeText(this, "Invalid credentials", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun validateCredentials(username: String, password: String): Boolean {
        return username == ADMIN_USERNAME && password == ADMIN_PASSWORD
    }

    private fun isLoggedIn(): Boolean {
        return sharedPref.getBoolean(KEY_IS_LOGGED_IN, false)
    }

    private fun saveLoginState(isLoggedIn: Boolean) {
        sharedPref.edit().putBoolean(KEY_IS_LOGGED_IN, isLoggedIn).apply()
    }

    private fun goToAdminDashboard() {
        val intent = Intent(this@AdminLoginActivity, AdminDashboardActivity::class.java)
        startActivity(intent)
        finish()
    }
}