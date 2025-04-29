package com.example.myapplication.CustomerFunctionalities

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun EditProfileUtility(navController: NavHostController) {
    // Initialize Firebase Auth instance and get the current user
    val auth = FirebaseAuth.getInstance()
    val user = auth.currentUser

    // State variables to hold the user's profile information
    var firstName by remember { mutableStateOf(user?.displayName?.split(" ")?.getOrNull(0) ?: "") }
    var lastName by remember { mutableStateOf(user?.displayName?.split(" ")?.getOrNull(1) ?: "") }
    var email by remember { mutableStateOf(user?.email ?: "") }
    var phoneNumber by remember { mutableStateOf("") }

    // Scaffold provides the basic layout structure with top bar
    Scaffold(
        topBar = {
            // TopAppBar with back navigation and save action
            TopAppBar(
                title = { Text("Edit Profile", style = MaterialTheme.typography.titleLarge) },
                navigationIcon = {
                    // Back button to pop the screen from the navigation stack
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    // Save button that updates user profile in Firebase
                    TextButton(onClick = {
                        // Build the profile update request with new first and last name
                        val profileUpdates = UserProfileChangeRequest.Builder()
                            .setDisplayName("$firstName $lastName")
                            .build()

                        // Update the profile in Firebase Auth and handle completion
                        user?.updateProfile(profileUpdates)?.addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                // Navigate back to Profile screen if update is successful
                                navController.popBackStack()
                            } else {
                                // Handle the error if needed (e.g., show a message to the user)
                            }
                        }
                    }) {
                        // Text for the save button with green color
                        Text("SAVE", color = Color(0xFF4CAF50)) // Green save button
                    }
                },
                colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = Color.White) // Set top bar color
            )
        }
    ) { innerPadding ->
        // Column layout for form fields, centered horizontally
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding) // Apply inner padding from Scaffold
                .padding(horizontal = 16.dp), // Apply extra horizontal padding
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp)) // Spacer for vertical spacing

            // Avatar (initials of the user) with a circular background
            Box(
                modifier = Modifier
                    .size(100.dp) // Avatar size
                    .background(Color(0xFFFFDAD6), shape = RoundedCornerShape(50)), // Circular background
                contentAlignment = Alignment.Center
            ) {
                // Display the user's initials in the center of the avatar
                Text(
                    text = firstName.take(1).uppercase() + lastName.take(1).uppercase(), // Initials
                    style = MaterialTheme.typography.headlineLarge.copy(fontSize = 40.sp), // Style for the initials
                    color = Color(0xFFFF725E) // Color of the initials
                )
            }

            Spacer(modifier = Modifier.height(16.dp)) // Spacer for vertical spacing

            // Form Field for First Name
            OutlinedTextField(
                value = firstName, // Bind the text field value to the state variable
                onValueChange = { firstName = it }, // Update state on text change
                label = { Text("First Name") }, // Label for the field
                modifier = Modifier.fillMaxWidth(), // Fill the width of the parent container
                singleLine = true // Ensure single-line input
            )
            Spacer(modifier = Modifier.height(16.dp)) // Spacer for vertical spacing

            // Form Field for Last Name
            OutlinedTextField(
                value = lastName, // Bind the text field value to the state variable
                onValueChange = { lastName = it }, // Update state on text change
                label = { Text("Last Name") }, // Label for the field
                modifier = Modifier.fillMaxWidth(), // Fill the width of the parent container
                singleLine = true // Ensure single-line input
            )
            Spacer(modifier = Modifier.height(16.dp)) // Spacer for vertical spacing

            // Form Field for Email (non-editable)
            OutlinedTextField(
                value = email, // Display the user's email
                onValueChange = { /* Email editing disabled */ }, // Prevent any changes to the email field
                label = { Text("E-mail") }, // Label for the field
                modifier = Modifier.fillMaxWidth(), // Fill the width of the parent container
                singleLine = true, // Ensure single-line input
                enabled = false // Disable editing of the email field
            )
            Spacer(modifier = Modifier.height(16.dp)) // Spacer for vertical spacing

            // Form Field for Phone Number
            OutlinedTextField(
                value = phoneNumber, // Bind the text field value to the state variable
                onValueChange = { phoneNumber = it }, // Update state on text change
                label = { Text("Phone Number") }, // Label for the field
                modifier = Modifier.fillMaxWidth(), // Fill the width of the parent container
                singleLine = true // Ensure single-line input
            )
        }
    }
}
