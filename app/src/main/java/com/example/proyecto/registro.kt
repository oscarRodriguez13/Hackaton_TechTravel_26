package com.example.proyecto

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

import com.example.proyecto.databinding.ActivityRegistroBinding

class registro : AppCompatActivity() {
    private lateinit var binding: ActivityRegistroBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistroBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set up click listener for register button
        binding.registerButton.setOnClickListener {
            val username = binding.username.text.toString().trim()
            val email = binding.email.text.toString().trim()
            val password = binding.password.text.toString().trim()

            // TODO: Add your own logic for registering the user



            // After registering the user, go back to Main activity
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        // Set up click listener for back button
        binding.backButton.setOnClickListener {
            // Simply finish the activity to go back to login activity
            val intent = Intent(this, Login::class.java)
            startActivity(intent)

            finish()
        }
    }
}