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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun AccountCreation(
    userType: String,  // The type of user (Customer or Business)
    onAccountCreated: (String) -> Unit,  // Callback to notify when the account is created
    navController: NavHostController  // Navigation controller to navigate between screens
) {
    // States to hold input values for the form fields
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var businessName by remember { mutableStateOf("") } // State for Business Name (for Business users)
    var registrationNumber by remember { mutableStateOf("") } // State for Registration Number (for Business users)
    var errorMessage by remember { mutableStateOf<String?>(null) }  // State for error messages

    // Firebase authentication instance to handle account creation
    val auth = FirebaseAuth.getInstance()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),  // Add padding around the form
        verticalArrangement = Arrangement.Center,  // Center the form vertically
        horizontalAlignment = Alignment.Start  // Align the form to the start (left side)
    ) {
        // Display header text based on the user type
        Text(
            text = if (userType == "Customer") "Create Customer Account" else "Create Business Account",
            style = MaterialTheme.typography.headlineMedium,
            color = Color.Black,
            modifier = Modifier.padding(bottom = 16.dp)  // Add some space below the header
        )

        // Input field for the user's name
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },  // Update the name value as user types
            label = { Text("Name") },  // Label for the input field
            modifier = Modifier.fillMaxWidth(),  // Make the input field full width
            colors = TextFieldDefaults.outlinedTextFieldColors(
                unfocusedBorderColor = Color(0xFFBDBDBD),  // Gray border when not focused
                focusedBorderColor = Color(0xFFFF725E)  // Red border when focused
            )
        )

        Spacer(modifier = Modifier.height(10.dp))  // Space between input fields

        // Input field for email
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },  // Update the email value as user types
            label = { Text("Email") },  // Label for the input field
            modifier = Modifier.fillMaxWidth(),  // Make the input field full width
            colors = TextFieldDefaults.outlinedTextFieldColors(
                unfocusedBorderColor = Color(0xFFBDBDBD),
                focusedBorderColor = Color(0xFFFF725E)
            )
        )

        Spacer(modifier = Modifier.height(10.dp))  // Space between input fields

        // Input field for password
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },  // Update the password value as user types
            label = { Text("Password") },  // Label for the input field
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation(),  // Mask the password input
            colors = TextFieldDefaults.outlinedTextFieldColors(
                unfocusedBorderColor = Color(0xFFBDBDBD),
                focusedBorderColor = Color(0xFFFF725E)
            )
        )

        // Only display these fields for Business account creation
        if (userType == "Business") {
            Spacer(modifier = Modifier.height(10.dp))

            // Input field for business name (only for Business users)
            OutlinedTextField(
                value = businessName,
                onValueChange = { businessName = it },  // Update the business name value as user types
                label = { Text("Business Name") },
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    unfocusedBorderColor = Color(0xFFBDBDBD),
                    focusedBorderColor = Color(0xFFFF725E)
                )
            )

            Spacer(modifier = Modifier.height(10.dp))  // Space between input fields

            // Input field for business registration number (only for Business users)
            OutlinedTextField(
                value = registrationNumber,
                onValueChange = { registrationNumber = it },  // Update the registration number value
                label = { Text("Registration Number") },
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    unfocusedBorderColor = Color(0xFFBDBDBD),
                    focusedBorderColor = Color(0xFFFF725E)
                )
            )
        }

        Spacer(modifier = Modifier.height(24.dp))  // Space before the button

        // Button to create account
        Button(
            onClick = {
                if (email.isNotBlank() && password.isNotBlank() && name.isNotBlank()) {
                    // Attempt to create a new user with email and password
                    auth.createUserWithEmailAndPassword(email.trim(), password.trim())
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                // Get the user ID after successful account creation
                                val user = FirebaseAuth.getInstance().currentUser
                                val userId = user?.uid

                                // Save user details to Firebase Database
                                if (userId != null) {
                                    val userData = if (userType == "Business") {
                                        mapOf(
                                            "name" to name,
                                            "email" to email,
                                            "userType" to userType,
                                            "businessName" to businessName,
                                            "registrationNumber" to registrationNumber
                                        )
                                    } else {
                                        mapOf(
                                            "name" to name,
                                            "email" to email,
                                            "userType" to userType
                                        )
                                    }

                                    val databaseReference =
                                        FirebaseDatabase.getInstance().getReference("users/$userId")
                                    databaseReference.updateChildren(userData)
                                }

                                // Notify the parent composable that the account is created
                                onAccountCreated(name)

                                // Navigate to the appropriate home screen based on user type
                                if (userType == "Customer") {
                                    navController.navigate("CustomerHomeScreen")
                                } else {
                                    navController.navigate("BusinessHomeScreen")
                                }
                            } else {
                                // If account creation failed, show the error message
                                errorMessage = task.exception?.message ?: "Unknown error occurred"
                            }
                        }
                } else {
                    // Show error message if required fields are empty
                    errorMessage = "All fields are required"
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),  // Full width and a fixed height for the button
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF725E)),
            shape = RoundedCornerShape(12.dp)  // Rounded corners for the button
        ) {
            Text(
                text = "Create Account",  // Button text
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White
            )
        }

        Spacer(modifier = Modifier.height(16.dp))  // Space between the button and error message

        // Display error message if there is any
        errorMessage?.let {
            Text(
                text = it,
                color = Color.Red,
                style = MaterialTheme.typography.bodySmall  // Small red text for error
            )
        }
    }
}
