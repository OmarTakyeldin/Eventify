package com.example.myapplication.AuthenticationAndIntroPage

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

@Composable
fun RegistrationChoice(navController: NavHostController) {
    // Column to arrange all UI elements vertically and center them
    Column(
        modifier = Modifier
            .fillMaxSize() // Occupy full screen space
            .padding(16.dp), // Apply padding around the content
        verticalArrangement = Arrangement.Center, // Vertically center the content
        horizontalAlignment = Alignment.CenterHorizontally // Horizontally center the content
    ) {
        // Header text that informs users to choose between customer or business registration
        Text(
            text = "Register as a customer or a business.",
            style = MaterialTheme.typography.bodyMedium, // Use body text style
            textAlign = TextAlign.Center, // Center-align the text
            modifier = Modifier.padding(bottom = 32.dp) // Add padding below the header text
        )

        // Customer registration button
        Button(
            onClick = { navController.navigate("CreateAccountScreen/Customer") }, // Navigate to customer account creation screen
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF725E)), // Set button color
            modifier = Modifier
                .fillMaxWidth(0.8f) // Make button width 80% of the screen width
                .height(50.dp), // Set button height
            shape = RoundedCornerShape(12.dp) // Apply rounded corners
        ) {
            Text(
                text = "Customer", // Display text on button
                style = MaterialTheme.typography.bodyMedium, // Use body text style
                color = Color.White // Set text color to white
            )
        }

        Spacer(modifier = Modifier.height(16.dp)) // Add vertical space between the buttons

        // Business registration button
        Button(
            onClick = { navController.navigate("CreateAccountScreen/Business") }, // Navigate to business account creation screen
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF725E)), // Set button color
            modifier = Modifier
                .fillMaxWidth(0.8f) // Make button width 80% of the screen width
                .height(50.dp), // Set button height
            shape = RoundedCornerShape(12.dp) // Apply rounded corners
        ) {
            Text(
                text = "Business", // Display text on button
                style = MaterialTheme.typography.bodyMedium, // Use body text style
                color = Color.White // Set text color to white
            )
        }
    }
}
