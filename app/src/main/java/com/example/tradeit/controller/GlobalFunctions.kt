package com.example.tradeit.controller

import android.app.AlertDialog
import android.content.Context
import android.widget.EditText

object GlobalFunctions {
    fun removeAccents(text: String): String {
        return text
            .replace("á", "a")
            .replace("é", "e")
            .replace("í", "i")
            .replace("ó", "o")
            .replace("ú", "u")
    }

    fun allEditTextAreFilled(editTextList: MutableList<EditText>): Boolean {
        for (element in editTextList) {
            if (element.text.toString().isEmpty()) {
                return false
            }
        }

        return true
    }

    fun showInfoDialog(context: Context, title: String, message: String) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle(title)
        builder.setMessage(message)
        builder.setPositiveButton("Aceptar") { dialog, _ ->
            dialog.dismiss()
        }
        val dialog = builder.create()
        dialog.show()
    }
}