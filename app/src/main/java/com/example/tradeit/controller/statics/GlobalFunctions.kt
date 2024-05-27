package com.example.tradeit.controller.statics

import android.app.AlertDialog
import android.content.Context
import android.widget.EditText
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

object GlobalFunctions {
    val cities = arrayOf(
        "Álava", "Albacete", "Alicante", "Almería", "Asturias", "Ávila", "Badajoz", "Baleares",
        "Barcelona", "Burgos", "Cáceres", "Cádiz", "Cantabria", "Castellón", "Ciudad Real",
        "Córdoba", "Cuenca", "Gerona", "Granada", "Guadalajara", "Guipúzcoa", "Huelva",
        "Huesca", "Jaén", "La Coruña", "La Rioja", "Las Palmas", "León", "Lérida", "Lugo",
        "Madrid", "Málaga", "Murcia", "Navarra", "Orense", "Palencia", "Pontevedra", "Salamanca",
        "Santa Cruz de Tenerife", "Segovia", "Sevilla", "Soria", "Tarragona", "Teruel",
        "Toledo", "Valencia", "Valladolid", "Vizcaya", "Zamora", "Zaragoza"
    )

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

    fun getCurrentDate(): String {
        val currentDate = Calendar.getInstance().time
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        return dateFormat.format(currentDate)
    }

    fun formatDate(milliseconds: Long): String {
        val date = Date(milliseconds)
        val formatter = SimpleDateFormat("dd/MM/yy - HH:mm")
        return formatter.format(date)
    }
}