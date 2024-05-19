package com.example.tradeit.controller.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
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

        binding.recoverPassword.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Recuperar contraseña")

            val input = EditText(this)
            input.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
            builder.setView(input)

            builder.setPositiveButton("Enviar") { dialog, which ->
                val email = input.text.toString().trim()
                if (email.isNotEmpty()) {
                    FirebaseFunctions.sendPasswordResetEmail(email) { success ->
                        if (success) {
                            GlobalFunctions.showInfoDialog(
                                this,
                                "Correo enviado.",
                                "Revisa la bandeja de entrada de tu correo electrónico para restablecer la contraseña."
                            )
                        } else {
                            GlobalFunctions.showInfoDialog(
                                this,
                                "Error.",
                                "Error al enviar el correo de restablecimiento de contraseña."
                            )
                        }
                    }
                } else {
                    GlobalFunctions.showInfoDialog(
                        this,
                        "Error.",
                        "El correo electrónico no puede estar vacío."
                    )
                }
            }

            builder.setNegativeButton("Cancelar") { dialog, _ -> dialog.cancel() }
            builder.show()
        }
    }
}