package com.example.myapplication.AppInitializationAndNavigation

import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.AuthenticationAndIntroPage.LoginScreen
import com.example.myapplication.BusinessFunctionalities.BusinessSetupScreen
import com.example.myapplication.BusinessFunctionalities.EditBusinessProfileScreen
import com.example.myapplication.CreateAccountScreen
import com.example.myapplication.FirstScreen
import com.example.myapplication.RegisterChoiceScreen
import com.example.myapplication.SecondScreen
import com.example.myapplication.ui.business.*
import com.example.myapplication.ui.customer.*
import com.google.firebase.auth.FirebaseAuth

@Composable
fun SetupNavigation() {
    // Firebase authentication instance for handling user login and details
    val auth = FirebaseAuth.getInstance()

    // States to manage loading, error messages, and user details
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var userName by rememberSaveable { mutableStateOf("") }
    var userEmail by rememberSaveable { mutableStateOf("") }
    var eventName by rememberSaveable { mutableStateOf("") }
    var eventDescription by rememberSaveable { mutableStateOf("") }

    // Initialize FirebaseAuth and fetch current user details when the screen is first launched
    LaunchedEffect(Unit) {
        val user = auth.currentUser
        userName = user?.displayName ?: "Guest"
        userEmail = user?.email ?: "user@example.com"
    }

    // NavController is used for handling navigation between screens
    val navController = rememberNavController()

    // Setup navigation between different screens using NavHost
    NavHost(navController = navController, startDestination = "FirstScreen") {

        // First Screen: Initial screen that might show a welcome message or intro
        composable("FirstScreen") { FirstScreen(navController) }

        // Second Screen: Shows additional options or a step in the flow
        composable("SecondScreen") { SecondScreen(navController) }

        // RegisterChoiceScreen: The screen to choose between registering as a customer or business
        composable("RegisterChoiceScreen") { RegisterChoiceScreen(navController) }

        // Login Screen: For user authentication via email and password
        composable("LoginScreen") {
            LoginScreen(
                onLogin = { email, password ->
                    isLoading = true // Set loading state to true while attempting login
                    auth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener { task ->
                            isLoading = false // Reset loading state after login attempt
                            if (task.isSuccessful) {
                                // If login is successful, navigate to Customer Home screen
                                val user = auth.currentUser
                                userName = user?.displayName ?: "Guest"
                                userEmail = user?.email ?: "user@example.com"
                                navController.navigate("CustomerHomeScreen")
                            } else {
                                // If login fails, display error message
                                errorMessage = task.exception?.message
                            }
                        }
                },
                isLoading = isLoading,
                errorMessage = errorMessage,
                navController = navController
            )
        }

        // Create Account Screen: Registers a new user either as a customer or business
        composable("CreateAccountScreen/{userType}") { backStackEntry ->
            val userType = backStackEntry.arguments?.getString("userType") ?: "Customer"
            CreateAccountScreen(
                navController = navController,
                userType = userType,
                onAccountCreated = { name ->
                    // Set user details after account is created
                    userName = name
                    val user = auth.currentUser
                    userEmail = user?.email ?: "user@example.com"
                    // Navigate based on user type (either Customer or Business)
                    if (userType == "Customer") {
                        navController.navigate("CustomerHomeScreen")
                    } else {
                        navController.navigate("BusinessHomeScreen")
                    }
                }
            )
        }

        // Customer Home Screen: Main screen for the customer with options to plan events, view tasks, or edit profile
        composable("CustomerHomeScreen") {
            CustomerHomeScreen(
                navigateToEventPlanning = { navController.navigate("CustomerEventPlanning") },
                navigateToTasks = { navController.navigate("TasksScreen") },
                navigateToProfile = { navController.navigate("ProfileScreen") }
            )
        }

        // Customer Event Planning Screen: Where the customer can plan their event by entering event details
        composable("CustomerEventPlanning") {
            CustomerEventPlanningScreen(
                navController = navController,
                onSubmit = { name, description ->
                    eventName = name
                    eventDescription = description
                    navController.navigate("CustomerHomeScreen")
                }
            )
        }

        // Tasks Screen: Displays customer tasks or reminders
        composable("TasksScreen") {
            CustomerTasksScreen(navController = navController)
        }

        // Profile Screen: Displays and allows editing of customer profile details
        composable("ProfileScreen") {
            CustomerProfileScreen(navController = navController)
        }

        // Edit Profile Screen: For editing customer's profile details
        composable("EditProfileScreen") {
            EditProfileScreen(navController = navController)
        }

        // Business Home Screen: Main screen for the business with options for business-related functionality
        composable("BusinessHomeScreen") {
            BusinessHomeScreen(navController = navController)
        }

        // Business Profile Screen: Allows businesses to view and update their profile
        composable("BusinessProfileScreen") {
            BusinessProfileScreen(navController = navController)
        }

        // Business Request Screen: Displays incoming requests for businesses
        composable("BusinessRequestScreen") {
            BusinessRequestScreen(navController = navController)
        }

        // Edit Business Profile Screen: Allows businesses to edit their profile information
        composable("EditBusinessProfileScreen") {
            EditBusinessProfileScreen(navController)
        }

        // Business Setup Screen: Initial screen for setting up a new business profile
        composable("BusinessSetupScreen") {
            BusinessSetupScreen(navController)
        }
    }
}
