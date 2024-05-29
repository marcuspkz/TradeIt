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


        val emailET = binding.emailET
        val displayNameET = binding.displayNameET
        val passwordET = binding.passwordET
        val password2ET = binding.password2ET
        val registerButton = binding.registerButton

        registerButton.setOnClickListener {
            val elementsList = mutableListOf(emailET, displayNameET, passwordET, password2ET)
            if (GlobalFunctions.allEditTextAreFilled(elementsList)) {
                if (passwordET.text.toString() == password2ET.text.toString()) {
                    FirebaseFunctions.registerUser(displayNameET.text.toString(), emailET.text.toString(), passwordET.text.toString(), this)
                } else {
                    GlobalFunctions.showInfoDialog(this, "Error", "Las contraseñas no coinciden.")
                }
            } else {
                GlobalFunctions.showInfoDialog(this, "Error", "Es necesario completar todos los campos.")
            }
        }
    }
}