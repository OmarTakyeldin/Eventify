package com.example.myapplication.BusinessFunctionalities

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.myapplication.R

@Composable
fun ProfileOptions(onClick: () -> Unit, text: String) {
    // Row to arrange the text and icon horizontally
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick) // Trigger the passed navigation action when clicked
            .background(Color.White) // Set background color to white
            .padding(horizontal = 16.dp, vertical = 12.dp), // Add padding inside the Row
        verticalAlignment = Alignment.CenterVertically // Align the text and icon vertically centered
    ) {
        // Text component to display the passed 'text' string
        Text(
            text = text, // Display the text passed as an argument
            style = MaterialTheme.typography.bodyMedium, // Use bodyMedium style for the text
            color = Color.Black, // Set text color to black
            modifier = Modifier.weight(1f)
        )

        // Icon component to display a right arrow icon
        Icon(
            painter = painterResource(id = R.drawable.ic_arrow_right),
            contentDescription = "Arrow Right", // Provide a description for accessibility
            tint = Color.Gray // Set icon color to gray
        )
    }
}
