package com.example.myapplication.data

import android.annotation.SuppressLint
import android.widget.CalendarView
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import java.time.LocalDate

@SuppressLint("NewApi")
@Composable
fun Calendar(onDateSelected: (LocalDate) -> Unit) {
    // Get the current context
    val context = LocalContext.current

    // Create and manage a CalendarView using AndroidView
    AndroidView(
        factory = { CalendarView(context) }, // Create a CalendarView using the context
        modifier = Modifier
            .fillMaxWidth() // Make the calendar view take up the full width of its parent
            .padding(vertical = 16.dp), // Add vertical padding around the calendar
        update = { calendarView ->
            // Set the current date on the calendar view
            calendarView.date = java.util.Calendar.getInstance().timeInMillis

            calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
                // Convert the selected date to LocalDate and pass it to the onDateSelected callback
                onDateSelected(LocalDate.of(year, month + 1, dayOfMonth))
            }
        }
    )
}

