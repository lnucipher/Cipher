package com.example.cipher.ui.common.utils

import android.annotation.SuppressLint
import android.content.Context
import com.example.cipher.R
import com.example.cipher.ui.common.theme.CipherTheme
import java.util.Calendar
import android.app.DatePickerDialog

class DatePickerUtil {

    companion object {
        @SuppressLint("DefaultLocale")
        fun showDatePickerDialog(context: Context, onDateSelected: (String) -> Unit) {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val themeId = if (CipherTheme.darkTheme) R.style.DarkDatePickerTheme else R.style.LightDatePickerTheme

            DatePickerDialog(context, themeId, { _, selectedYear, selectedMonth, selectedDay ->
                val formattedDate = String.format("%02d-%02d-%d", selectedMonth + 1, selectedDay, selectedYear)
                onDateSelected(formattedDate)
            }, year, month, day).show()
        }
    }
}
