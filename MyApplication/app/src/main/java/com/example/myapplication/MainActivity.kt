package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.navigation.NavHostController
import com.example.myapplication.ui.theme.MyApplicationTheme
import com.google.firebase.auth.FirebaseAuth
import com.example.myapplication.AuthenticationAndIntroPage.FirstScreenComponents
import com.example.myapplication.AuthenticationAndIntroPage.LogoText
import com.example.myapplication.AuthenticationAndIntroPage.SecondScreenComponents
import com.example.myapplication.AuthenticationAndIntroPage.RegistrationChoice
import com.example.myapplication.AuthenticationAndIntroPage.AccountCreation
import com.example.myapplication.AppInitializationAndNavigation.SetupNavigation


// MainActivity class for the Android app, which initializes FirebaseAuth and sets up the content view
class MainActivity : ComponentActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize FirebaseAuth instance
        auth = FirebaseAuth.getInstance()

        // Set the content of the activity using Jetpack Compose
        setContent {
            MyApplicationTheme {
                // Call the function to set up navigation for the app
                SetupNavigation()
            }
        }
    }
}

// First screen of the app
@Composable
fun FirstScreen(navController: NavHostController) {
    // Display the components for the first screen
    FirstScreenComponents(navController)
}

// Displays styled text or a logo, specific to the app
@Composable
fun EventifyStyledText() {
    // Call the composable to display logo text
    LogoText()
}

// Second screen of the app
@Composable
fun SecondScreen(navController: NavHostController) {
    // Call the composable function to display the components for the second screen
    SecondScreenComponents(navController)
}

// Register Choice screen, where the user can choose between different registration options
@Composable
fun RegisterChoiceScreen(navController: NavHostController) {
    // Call the composable function to display registration choice components
    RegistrationChoice(navController)
}

// Create Account screen, where the user can create an account based on their selected user type
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateAccountScreen(
    navController: NavHostController,
    userType: String,
    onAccountCreated: (String) -> Unit // Callback to handle account creation success
) {
    // Call the composable to handle account creation with the given user type and callback
    AccountCreation(userType, onAccountCreated, navController)
}


