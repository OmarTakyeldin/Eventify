package com.example.myapplication.AuthenticationAndIntroPage

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

@Composable
fun SecondScreenComponents(navController: NavHostController) {
    // Column to arrange all UI elements vertically and center them
    Column(
        modifier = Modifier
            .fillMaxSize() // Occupy full screen space
            .padding(16.dp), // Apply padding around the content
        verticalArrangement = Arrangement.Center, // Vertically center the content
        horizontalAlignment = Alignment.CenterHorizontally // Horizontally center the content
    ) {
        // Header text that greets users
        Text(
            text = "Welcome to Eventify", // Title text
            style = MaterialTheme.typography.headlineMedium, // Use headline text style
            color = MaterialTheme.colorScheme.onBackground, // Set text color to theme's onBackground color
            modifier = Modifier.padding(bottom = 8.dp) // Space between header and description
        )

        // Description text explaining the app's purpose
        Text(
            text = "Create an account with us and\nexperience seamless event planning.", // Descriptive text
            style = MaterialTheme.typography.bodyMedium, // Use body text style
            color = MaterialTheme.colorScheme.onBackground, // Set text color to theme's onBackground color
            textAlign = TextAlign.Center // Center-align the text
        )

        Spacer(modifier = Modifier.height(50.dp)) // Space between text and buttons

        // Create Account button that navigates to registration screen
        Button(
            onClick = { navController.navigate("RegisterChoiceScreen") }, // Navigate to the registration choice screen
            modifier = Modifier
                .fillMaxWidth(0.8f) // Make button width 80% of the screen width
                .height(50.dp), // Set button height
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF725E)), // Set button color
        ) {
            Text(
                text = "Create Account", // Button text
                color = Color.White, // Set text color to white
                style = MaterialTheme.typography.bodyMedium // Use body text style
            )
        }

        Spacer(modifier = Modifier.height(16.dp)) // Space between buttons

        // Log In button that navigates to login screen
        OutlinedButton(
            onClick = { navController.navigate("LoginScreen") }, // Navigate to the login screen
            modifier = Modifier
                .fillMaxWidth(0.8f) // Make button width 80% of the screen width
                .height(50.dp), // Set button height
            colors = ButtonDefaults.outlinedButtonColors(containerColor = Color.Transparent), // Make the button background transparent
            border = BorderStroke(1.dp, Color.Red) // Set red border around the button
        ) {
            Text(
                text = "Login", // Button text
                color = Color.Red, // Set text color to red
                style = MaterialTheme.typography.bodyMedium // Use body text style
            )
        }
    }
}
