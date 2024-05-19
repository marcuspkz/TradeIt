package com.example.tradeit.controller.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.tradeit.R
import com.example.tradeit.controller.statics.FirebaseFunctions
import com.example.tradeit.controller.statics.GlobalFunctions
import com.example.tradeit.databinding.ActivityAccountInfoBinding
import com.example.tradeit.databinding.ActivityProductDetailBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth

class AccountInfoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAccountInfoBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAccountInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.updateEmailButton.setOnClickListener {
            if (binding.updateEmail.text.isNotEmpty()) {
                FirebaseFunctions.sendEmailVerification(binding.updateEmail.text.toString(), this) { success ->
                    if (success) {
                        GlobalFunctions.showInfoDialog(this, "Correo enviado.", "Correo electrónico enviado a tu nueva dirección, revisa tu bandeja. Será necesario verificar la cuenta para poder realizar el cambio.")
                    } else {
                        GlobalFunctions.showInfoDialog(this, "Error.", "Error al enviar el formulario de cambio de correo.")
                    }
                }
            } else {
                GlobalFunctions.showInfoDialog(this, "Error.", "El correo electrónico no puede estar vacío.")
            }
        }

        binding.updateNameButton.setOnClickListener {
            val newDisplayName = binding.displayName.text.toString()

            if (newDisplayName.isNotEmpty()) {
                FirebaseFunctions.updateDisplayName(newDisplayName) { success ->
                    if (success) {
                        GlobalFunctions.showInfoDialog(this, "Éxito.", "Nombre actualizado con éxito.")
                    } else {
                        GlobalFunctions.showInfoDialog(this, "Error.", "Error al actualizar el nombre.")
                    }
                }
            } else {
                GlobalFunctions.showInfoDialog(this, "Error.", "El nombre no puede estar vacío.")
            }
        }

        binding.updatePasswordButton.setOnClickListener {
            val email = FirebaseAuth.getInstance().currentUser?.email
            if (!email.isNullOrEmpty()) {
                FirebaseFunctions.sendPasswordResetEmail(email) { success ->
                    if (success) {
                        GlobalFunctions.showInfoDialog(this, "Correo enviado.", "Revisa la bandeja de entrada de tu correo electrónico para restablecer la contraseña.")
                    } else {
                        GlobalFunctions.showInfoDialog(this, "Error.", "Error al enviar el correo de restablecimiento de contraseña.")
                    }
                }
            } else {
                GlobalFunctions.showInfoDialog(this, "Error.", "No se pudo obtener el correo electrónico del usuario.")
            }
        }
    }
}