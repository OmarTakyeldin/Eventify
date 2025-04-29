package com.example.myapplication.CustomerFunctionalities

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
fun CustomerProfileOptions(onClick: () -> Unit, optionText: String) {
    // Row layout for displaying the profile option
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable { onClick() }, // Make the row clickable, triggering the provided onClick action
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // Text component for displaying the option's label
        Text(
            text = optionText,
            style = MaterialTheme.typography.bodyMedium.copy(color = Color.Black) // Apply black color for text
        )
        // Icon that acts as an indicator for navigation
        Icon(
            painter = painterResource(id = R.drawable.ic_arrow_right), // The arrow icon for navigation
            contentDescription = "Navigate",
            tint = Color.Gray
        )
    }
}
