package com.example.tradeit.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.tradeit.controller.FirebaseFunctions
import com.example.tradeit.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()
        val emailET = binding.emailET
        val passwordET = binding.passwordET
        val loginButton = binding.loginButton
        var registerButton = binding.registerButton

        loginButton.setOnClickListener {
            FirebaseFunctions.loginUser(emailET.text.toString(), passwordET.text.toString(), firebaseAuth, this)
        }

        registerButton.setOnClickListener {
            val intent = Intent(this, RegisterUserActivity::class.java)
            startActivity(intent)
        }
    }
}