package com.example.tradeit

import android.content.ContentValues.TAG
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.tradeit.databinding.ActivityRegisterUserBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.auth.userProfileChangeRequest


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
                registerUser(displayNameET.text.toString(), emailET.text.toString(), passwordET.text.toString())
            } else {
                Toast.makeText(
                    baseContext,
                    "Las contraseñas no coinciden.",
                    Toast.LENGTH_SHORT,
                ).show()
            }
        }
    }

    private fun registerUser(displayName: String, email: String, password: String) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    //registro exitoso
                    updateDisplayName(displayName)
                    Toast.makeText(
                        baseContext,
                        "¡Registro de usuario $displayName exitoso!",
                        Toast.LENGTH_SHORT,
                    ).show()
                } else {
                    //registro fallido
                    Toast.makeText(
                        baseContext,
                        "La autenticación ha fallado.",
                        Toast.LENGTH_SHORT,
                    ).show()
                }
            }
    }

    private fun updateDisplayName(name: String) {
        val user = Firebase.auth.currentUser

        val profileUpdates = userProfileChangeRequest {
            displayName = name
            //photoUri = Uri.parse("https://example.com/jane-q-user/profile.jpg")   //esto es para ponerle una foto de perfil
        }

        user!!.updateProfile(profileUpdates)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    //actualización exitosa
                    Log.d(TAG, "User profile updated.")
                }
            }
    }
}