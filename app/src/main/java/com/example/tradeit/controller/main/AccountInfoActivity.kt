package com.example.tradeit.controller.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.tradeit.R
import com.example.tradeit.controller.main.start.StartActivity
import com.example.tradeit.controller.statics.FirebaseFunctions
import com.example.tradeit.controller.statics.GlobalFunctions
import com.example.tradeit.databinding.ActivityAccountInfoBinding
import com.example.tradeit.databinding.ActivityProductDetailBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth

class AccountInfoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAccountInfoBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAccountInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.infoDescription.text = "Nombre: ${FirebaseAuth.getInstance().currentUser?.displayName}\n" +
                "Correo electrónico: ${FirebaseAuth.getInstance().currentUser?.email}"

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

        binding.deleteAccountButton.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Confirmación de contraseña")
            builder.setMessage("Introduce tu contraseña para confirmar la eliminación de la cuenta")

            val input = EditText(this)
            input.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            val layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            layoutParams.setMargins(48, 0, 48, 0)
            input.layoutParams = layoutParams
            builder.setView(input)

            builder.setPositiveButton("Confirmar") { dialog, _ ->
                val password = input.text.toString()
                val user = FirebaseAuth.getInstance().currentUser

                if (user != null && password.isNotEmpty()) {
                    val credential = EmailAuthProvider.getCredential(user.email!!, password)
                    user.reauthenticate(credential).addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            AlertDialog.Builder(this).apply {
                                setTitle("¿Seguro que quieres borrar la cuenta?")
                                setPositiveButton("Sí") { _, _ ->
                                    FirebaseFunctions.deleteAccount { success ->
                                        if (success) {
                                            FirebaseAuth.getInstance().signOut()
                                            val intent = Intent(this@AccountInfoActivity, MainActivity::class.java)
                                            startActivity(intent)
                                            Toast.makeText(this@AccountInfoActivity, "Cuenta eliminada correctamente.", Toast.LENGTH_SHORT).show()
                                            finish()
                                        } else {
                                            GlobalFunctions.showInfoDialog(this@AccountInfoActivity, "Error", "Error al intentar eliminar la cuenta.")
                                        }
                                    }
                                }
                                setNegativeButton("Cancelar") { dialog, _ -> dialog.cancel() }
                                create().show()
                            }
                        } else {
                            GlobalFunctions.showInfoDialog(this, "Error",  "Contraseña incorrecta. Inténtalo de nuevo.")
                        }
                    }
                } else {
                    GlobalFunctions.showInfoDialog(this, "Error",  "La contraseña no puede estar vacía.")
                }
            }

            builder.setNegativeButton("Cancelar") { dialog, _ -> dialog.cancel() }
            val dialog = builder.create()
            dialog.show()

            val messageView = dialog.findViewById<TextView>(android.R.id.message)
            messageView?.setPadding(48, 0, 48, 0)
        }
    }
}