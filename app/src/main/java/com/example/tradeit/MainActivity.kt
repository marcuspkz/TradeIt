package com.example.tradeit

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tradeit.adapter.ProductAdapter
import com.example.tradeit.databinding.ActivityMainBinding
import com.example.tradeit.model.Product
import com.google.firebase.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.text.Normalizer
import java.util.regex.Pattern

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
            loginUser(emailET.text.toString(), passwordET.text.toString())
        }

        registerButton.setOnClickListener {
            val intent = Intent(this, RegisterUserActivity::class.java)
            startActivity(intent)
        }
    }

    private fun loginUser(email: String, password: String) {
        val email = email
        val password = password

        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    //autenticación exitosa
                    val intent = Intent(this, StartActivity::class.java)
                    startActivity(intent)
                    finish() //cerrar esta actividad para que no se pueda volver atrás
                    var displayName = getDisplayName()
                    Toast.makeText(
                        this,
                        "¡Bienvenido, $displayName!",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    //autenticación fallida
                    Toast.makeText(
                        this,
                        "Credenciales incorrectas. Revisa los datos e inténtalo de nuevo.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

    //debug
    private fun getDisplayName(): String? {
        val user = Firebase.auth.currentUser
        var name: String?
        name = ""
        user?.let {
            for (profile in it.providerData) {
                name = profile.displayName
            }
        }

        return name
    }
}