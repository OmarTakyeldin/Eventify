package com.example.myapplication.ui.business

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.myapplication.R
import com.example.myapplication.BusinessFunctionalities.ProfileOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

@Composable
fun BusinessProfileScreen(navController: NavHostController) {
    // State variables to hold business details like name and email
    var businessName by remember { mutableStateOf("Business Name") }
    var businessEmail by remember { mutableStateOf("example@email.com") }

    // Firebase user ID and reference to the business data in the database
    val userId = FirebaseAuth.getInstance().currentUser?.uid
    val databaseReference = FirebaseDatabase.getInstance().getReference("business/$userId")

    // Load business information from Firebase on the screen launch
    LaunchedEffect(userId) {
        userId?.let {
            // Fetch business data from the Firebase database
            databaseReference.get().addOnSuccessListener { snapshot ->
                // Update the UI state with data from Firebase or use default if not found
                businessName = snapshot.child("businessName").value as? String ?: "Business Name"
                businessEmail = snapshot.child("email").value as? String ?: "example@email.com"
            }
        }
    }

    // Scaffold that sets up the screen layout with a bottom navigation bar
    Scaffold(
        bottomBar = {
            // Bottom navigation bar with three items: Home, Requests, Profile
            NavigationBar(
                containerColor = Color.White,
                contentColor = Color(0xFFFF725E) // Pink color for content
            ) {
                // Home navigation item
                NavigationBarItem(
                    selected = false,
                    onClick = { navController.navigate("BusinessHomeScreen") },
                    icon = {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_home),
                            contentDescription = "Home"
                        )
                    },
                    label = { Text("Home") }
                )
                // Requests navigation item
                NavigationBarItem(
                    selected = false,
                    onClick = { navController.navigate("BusinessRequestScreen") },
                    icon = {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_tasks),
                            contentDescription = "Requests"
                        )
                    },
                    label = { Text("Requests") }
                )
                // Profile navigation item (currently selected)
                NavigationBarItem(
                    selected = true,
                    onClick = { /* Stay on Profile */ },
                    icon = {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_profile),
                            contentDescription = "Profile"
                        )
                    },
                    label = { Text("Profile") }
                )
            }
        }
    ) { paddingValues ->
        // Main content of the Business Profile screen
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF8F8F8)) // Light gray background
                .padding(paddingValues)
        ) {
            // Top section displaying business name, email, and logo
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(16.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ) {
                    // Logo placeholder (using text for now)
                    Box(
                        modifier = Modifier
                            .size(64.dp)
                            .background(Color(0xFFFFEBE8), shape = CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "LOGO",
                            color = Color(0xFFFF725E),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    // Business name and email display
                    Column {
                        Text(
                            text = businessName,
                            style = MaterialTheme.typography.headlineMedium,
                            color = Color.Black
                        )
                        Text(
                            text = businessEmail,
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.Gray
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Options section with profile-related options
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                // Edit Profile option
                ProfileOption("Edit Profile") {
                    navController.navigate("EditBusinessProfileScreen")
                }
                // Contact Us option (currently no action)
                ProfileOption("Contact Us") {
                    // Handle Contact Us action
                }
                // Sign Out option, handles logout and navigation to login screen
                ProfileOption("Sign Out") {
                    try {
                        // Perform Firebase logout
                        FirebaseAuth.getInstance().signOut()

                        // Navigate to LoginScreen and clear backstack
                        navController.navigate("LoginScreen") {
                            popUpTo(0) // Clear all previous screens from the backstack
                        }
                    } catch (e: Exception) {
                        // Log any error during sign out
                        e.printStackTrace()
                    }
                }
            }
        }
    }
}

@Composable
fun ProfileOption(text: String, onClick: () -> Unit) {
    ProfileOptions(onClick, text)
}
