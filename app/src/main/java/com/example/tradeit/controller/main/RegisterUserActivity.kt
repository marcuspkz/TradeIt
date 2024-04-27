package com.example.tradeit.controller.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.tradeit.controller.statics.FirebaseFunctions
import com.example.tradeit.databinding.ActivityRegisterUserBinding
import com.google.firebase.auth.FirebaseAuth


class RegisterUserActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterUserBinding
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()
        val emailET = binding.emailET
        val displayNameET = binding.displayNameET
        val passwordET = binding.passwordET
        val password2ET = binding.password2ET
        val registerButton = binding.registerButton

        registerButton.setOnClickListener {
            if (passwordET.text.toString() == password2ET.text.toString()) {
                FirebaseFunctions.registerUser(displayNameET.text.toString(), emailET.text.toString(), passwordET.text.toString(), firebaseAuth, this)
            } else {
                Toast.makeText(
                    baseContext,
                    "Las contraseñas no coinciden.",
                    Toast.LENGTH_SHORT,
                ).show()
            }
        }
    }
}