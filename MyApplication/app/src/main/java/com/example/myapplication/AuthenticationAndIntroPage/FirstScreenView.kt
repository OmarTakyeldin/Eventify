package com.example.myapplication.AuthenticationAndIntroPage

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.myapplication.EventifyStyledText
import com.example.myapplication.R
import kotlinx.coroutines.delay

@Composable
fun FirstScreenComponents(navController: NavHostController) {
    // LaunchedEffect is used to delay navigation to the next screen by 2 seconds
    LaunchedEffect(Unit) {
        delay(2000) // Delay for 2 seconds
        navController.navigate("SecondScreen") // Navigate to the second screen
    }

    // Column layout to arrange elements vertically
    Column(
        modifier = Modifier
            .fillMaxSize() // Fill the entire screen size
            .background(MaterialTheme.colorScheme.background), // Set background color from the MaterialTheme
        verticalArrangement = Arrangement.Center, // Center content vertically
        horizontalAlignment = Alignment.CenterHorizontally // Center content horizontally
    ) {
        // Display the logo image (replace with your own logo resource)
        Image(
            painter = painterResource(id = R.drawable.eventify_logo), // Reference to the logo resource
            contentDescription = "Eventify Logo", // Descriptive text for accessibility
            modifier = Modifier.size(150.dp) // Set the size of the logo (150 dp)
        )

        Spacer(modifier = Modifier.height(16.dp)) // Add vertical space between the logo and text

        // Display the styled text (likely a slogan or tagline for the app)
        EventifyStyledText() // Custom composable to display styled text
    }
}
