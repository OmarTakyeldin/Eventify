package com.example.myapplication.BusinessFunctionalities

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.myapplication.R
import com.example.myapplication.data.BusinessRepository

@OptIn(ExperimentalMaterial3Api::class) // Opt-in for experimental features in Material3
@Composable
fun BusinessSetupScreen(navController: NavHostController) {
    // State variables for the form fields and error message
    var businessName by remember { mutableStateOf("") }
    var businessType by remember { mutableStateOf("Food Vendors") }
    var location by remember { mutableStateOf("") }
    var contactNumber by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var expanded by remember { mutableStateOf(false) } // Dropdown menu expanded state
    val businessTypes = listOf("Food Vendors", "Singers", "Florists") // Available business types

    // Repository instance to handle data submission
    val businessRepository = BusinessRepository()

    // Scaffold to hold the UI layout with a top bar
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Set Up Your Business") }, // Title for the top bar
                navigationIcon = {
                    // Back button in the top bar to pop the navigation stack
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(painter = painterResource(id = R.drawable.ic_back), contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        // Main content of the screen inside a Column
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF8F8F8)) // Light gray background color
                .padding(paddingValues)
                .padding(16.dp) // Add internal padding
        ) {
            // Business Name input field
            OutlinedTextField(
                value = businessName,
                onValueChange = { businessName = it }, // Update business name on input change
                label = { Text("Business Name") }, // Label for the input field
                modifier = Modifier.fillMaxWidth(), // Take up full width
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    unfocusedBorderColor = Color(0xFFBDBDBD), // Gray border when unfocused
                    focusedBorderColor = Color(0xFFFF725E) // Red border when focused
                )
            )

            Spacer(modifier = Modifier.height(16.dp)) // Spacer between inputs

            // Business Type Dropdown menu
            Box {
                OutlinedTextField(
                    value = businessType,
                    onValueChange = { businessType = it }, // Update business type on input change
                    label = { Text("What Type of Business?") }, // Label for the dropdown
                    readOnly = true, // Make it read-only so the dropdown menu opens when clicked
                    trailingIcon = {
                        // Icon to indicate the dropdown menu
                        Icon(
                            imageVector = Icons.Default.ArrowDropDown,
                            contentDescription = "Dropdown",
                            modifier = Modifier.clickable { expanded = !expanded } // Toggle dropdown visibility
                        )
                    },
                    modifier = Modifier.fillMaxWidth(), // Take up full width
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        unfocusedBorderColor = Color(0xFFBDBDBD), // Gray border when unfocused
                        focusedBorderColor = Color(0xFFFF725E) // Red border when focused
                    )
                )

                // Dropdown menu with business type options
                DropdownMenu(
                    expanded = expanded, // Controlled by the 'expanded' state
                    onDismissRequest = { expanded = false }, // Dismiss the menu when clicking outside
                    modifier = Modifier.fillMaxWidth().align(Alignment.TopStart) // Full width dropdown
                ) {
                    businessTypes.forEach { type -> // Iterate through business types
                        DropdownMenuItem(
                            text = { Text(type) }, // Display business type name
                            onClick = {
                                businessType = type // Set selected business type
                                expanded = false // Close the dropdown menu
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp)) // Spacer between inputs

            // Location input field
            OutlinedTextField(
                value = location,
                onValueChange = { location = it }, // Update location on input change
                label = { Text("Exact Location") }, // Label for the location input
                modifier = Modifier.fillMaxWidth(), // Take up full width
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    unfocusedBorderColor = Color(0xFFBDBDBD), // Gray border when unfocused
                    focusedBorderColor = Color(0xFFFF725E) // Red border when focused
                )
            )

            Spacer(modifier = Modifier.height(16.dp)) // Spacer between inputs

            // Contact Number input field
            OutlinedTextField(
                value = contactNumber,
                onValueChange = { contactNumber = it }, // Update contact number on input change
                label = { Text("Contact Number") }, // Label for the contact number input
                modifier = Modifier.fillMaxWidth(), // Take up full width
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    unfocusedBorderColor = Color(0xFFBDBDBD), // Gray border when unfocused
                    focusedBorderColor = Color(0xFFFF725E) // Red border when focused
                )
            )

            Spacer(modifier = Modifier.height(16.dp)) // Spacer between inputs

            // Email input field
            OutlinedTextField(
                value = email,
                onValueChange = { email = it }, // Update email on input change
                label = { Text("Email") }, // Label for the email input
                modifier = Modifier.fillMaxWidth(), // Take up full width
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    unfocusedBorderColor = Color(0xFFBDBDBD), // Gray border when unfocused
                    focusedBorderColor = Color(0xFFFF725E) // Red border when focused
                )
            )

            // Display error message if present
            errorMessage?.let {
                Text(
                    text = it, // Show the error message
                    color = Color.Red, // Error message in red color
                    style = MaterialTheme.typography.bodySmall, // Small text style
                    modifier = Modifier.padding(top = 8.dp) // Padding above the error message
                )
            }

            Spacer(modifier = Modifier.height(32.dp)) // Spacer before submit button

            // Submit Button
            Button(
                onClick = {
                    // Submit the form data to the repository
                    businessRepository.submitSetup(
                        businessName = businessName,
                        businessType = businessType,
                        location = location,
                        contactNumber = contactNumber,
                        email = email,
                        onSuccess = { navController.popBackStack() }, // On success, go back to previous screen
                        onError = { errorMessage = it } // On error, show the error message
                    )
                },
                modifier = Modifier.fillMaxWidth(), // Take up full width
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF725E)), // Button color
                shape = RoundedCornerShape(12.dp) // Rounded corners for the button
            ) {
                Text(text = "Complete Setup", color = Color.White) // Button text
            }
        }
    }
}
