package com.example.myapplication.BusinessFunctionalities

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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditBusinessProfileScreen(navController: NavHostController) {
    // Declare mutable state for the form fields
    var businessName by remember { mutableStateOf("") }
    var businessType by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    // Firebase reference to the business data for the currently logged-in user
    val userId = FirebaseAuth.getInstance().currentUser?.uid
    val databaseReference = FirebaseDatabase.getInstance().getReference("business/$userId")

    // Fetch existing profile data when the screen is launched
    LaunchedEffect(userId) {
        userId?.let {
            databaseReference.get().addOnSuccessListener { snapshot ->
                // Safely retrieve existing profile data from Firebase
                businessName = snapshot.child("businessName").value as? String ?: ""
                businessType = snapshot.child("businessType").value as? String ?: ""
                email = snapshot.child("email").value as? String ?: ""
                location = snapshot.child("location").value as? String ?: ""
                phoneNumber = snapshot.child("contactNumber").value as? String ?: ""
            }.addOnFailureListener {
                // If data retrieval fails, display an error message
                errorMessage = "Failed to fetch profile data: ${it.message}"
            }
        }
    }

    // Function to save the updated profile data to Firebase
    fun saveProfile() {
        // Ensure all fields are filled before updating
        if (businessName.isNotBlank() && businessType.isNotBlank() && email.isNotBlank() && location.isNotBlank() && phoneNumber.isNotBlank()) {
            // Prepare the updated data as a map
            val updatedData = mapOf(
                "businessName" to businessName,
                "businessType" to businessType,
                "email" to email,
                "location" to location,
                "contactNumber" to phoneNumber
            )

            // Update the data in Firebase
            userId?.let {
                databaseReference.updateChildren(updatedData).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        // Navigate back to the previous screen on success
                        navController.popBackStack()
                    } else {
                        // Display an error message if the update fails
                        errorMessage = "Failed to update profile: ${task.exception?.message}"
                    }
                }
            }
        } else {
            // Show an error message if any field is blank
            errorMessage = "All fields are required"
        }
    }

    Scaffold(
        topBar = {
            // Top bar with a back button and a save button
            CenterAlignedTopAppBar(
                title = { Text("Edit Profile") },
                navigationIcon = {
                    // Back button to go back to the previous screen
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_back),
                            contentDescription = "Back"
                        )
                    }
                },
                actions = {
                    // Save button to save the updated profile
                    TextButton(onClick = { saveProfile() }) {
                        Text(text = "SAVE", color = Color(0xFF4CAF50)) // Save button in green
                    }
                }
            )
        }
    ) { paddingValues ->
        // Main content of the screen with a column layout
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF8F8F8)) // Light gray background
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            // Placeholder logo for the business profile
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .background(Color(0xFFFFEBE8), shape = CircleShape)
                    .align(Alignment.CenterHorizontally),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "OY", // Placeholder text for logo
                    color = Color(0xFFFF725E),
                    style = MaterialTheme.typography.headlineMedium
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Business Name field
            OutlinedTextField(
                value = businessName,
                onValueChange = { businessName = it },
                label = { Text("Business Name") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Business Type field
            OutlinedTextField(
                value = businessType,
                onValueChange = { businessType = it },
                label = { Text("Business Type") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Email field
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("E-mail") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Location field
            OutlinedTextField(
                value = location,
                onValueChange = { location = it },
                label = { Text("Location") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Phone Number field
            OutlinedTextField(
                value = phoneNumber,
                onValueChange = { phoneNumber = it },
                label = { Text("Phone Number") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Display error message if present
            errorMessage?.let {
                Text(
                    text = it,
                    color = Color.Red, // Red color for error messages
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        }
    }
}
