package com.example.tradeit.controller.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.tradeit.controller.statics.FirebaseFunctions
import com.example.tradeit.controller.statics.GlobalFunctions
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
        val progressBar = binding.progressBar

        loginButton.setOnClickListener {
            progressBar.visibility = View.VISIBLE
            if (emailET.text.toString() != "" && passwordET.text.toString() != "") {
                FirebaseFunctions.loginUser(emailET.text.toString(), passwordET.text.toString(), firebaseAuth, this)
                progressBar.visibility = View.GONE
            } else {
                progressBar.visibility = View.GONE
                GlobalFunctions.showInfoDialog(this, "Error", "Es necesario introducir correo electrónico y contraseña.")
            }
        }

        registerButton.setOnClickListener {
            val intent = Intent(this, RegisterUserActivity::class.java)
            startActivity(intent)
        }
    }
}