package com.example.cipher.ui.screens.auth_screen.utils

import android.content.Context
import com.example.cipher.R
import com.example.cipher.ui.theme.CipherTheme
import java.util.Calendar
import android.app.DatePickerDialog

class DatePickerUtil {

    companion object {
        fun showDatePickerDialog(context: Context, onDateSelected: (String) -> Unit) {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val themeId = if (CipherTheme.darkTheme) R.style.DarkDatePickerTheme else R.style.LightDatePickerTheme

            DatePickerDialog(context, themeId, { _, selectedYear, selectedMonth, selectedDay ->
                val formattedDate = "$selectedDay.${selectedMonth + 1}.$selectedYear"
                onDateSelected(formattedDate)
            }, year, month, day).show()
        }
    }
}
