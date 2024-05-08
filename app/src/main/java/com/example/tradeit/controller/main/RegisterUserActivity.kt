package com.example.tradeit.controller.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.tradeit.controller.statics.FirebaseFunctions
import com.example.tradeit.controller.statics.GlobalFunctions
import com.example.tradeit.databinding.ActivityRegisterUserBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage


class RegisterUserActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterUserBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firebaseStorage: FirebaseStorage

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterUserBinding.inflate(layoutInflater)
        setContentView(binding.root)
        firebaseAuth = FirebaseAuth.getInstance()
        firebaseStorage = FirebaseStorage.getInstance()

        val emailET = binding.emailET
        val displayNameET = binding.displayNameET
        val passwordET = binding.passwordET
        val password2ET = binding.password2ET
        val registerButton = binding.registerButton
        val progressBar = binding.progressBar

        registerButton.setOnClickListener {
            progressBar.visibility = View.VISIBLE
            val elementsList = mutableListOf(emailET, displayNameET, passwordET, password2ET)
            if (GlobalFunctions.allEditTextAreFilled(elementsList)) {
                if (passwordET.text.toString() == password2ET.text.toString()) {
                    FirebaseFunctions.registerUser(displayNameET.text.toString(), emailET.text.toString(), passwordET.text.toString(), firebaseAuth, firebaseStorage, this)
                    progressBar.visibility = View.GONE
                } else {
                    progressBar.visibility = View.GONE
                    GlobalFunctions.showInfoDialog(this, "Error", "Las contraseñas no coinciden.")
                }
            } else {
                progressBar.visibility = View.GONE
                GlobalFunctions.showInfoDialog(this, "Error", "Es necesario completar todos los campos.")
            }
        }
    }
}