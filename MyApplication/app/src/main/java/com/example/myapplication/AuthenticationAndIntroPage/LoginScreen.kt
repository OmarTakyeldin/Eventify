package com.example.myapplication.AuthenticationAndIntroPage

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    onLogin: (String, String) -> Unit,
    isLoading: Boolean,
    errorMessage: String?,
    navController: NavHostController

) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val auth = FirebaseAuth.getInstance()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.Start
    ) {
        // Header Text
        Text(
            text = "Login",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Email Field
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("E-mail") },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                unfocusedBorderColor = Color(0xFFBDBDBD),
                focusedBorderColor = Color(0xFFFF725E)
            )
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Password Field
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation(),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                unfocusedBorderColor = Color(0xFFBDBDBD),
                focusedBorderColor = Color(0xFFFF725E)
            )
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Error Message
        if (errorMessage != null) {
            Text(
                text = errorMessage,
                color = Color.Red,
                style = MaterialTheme.typography.bodySmall
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        var errorMessage by remember { mutableStateOf<String?>(null) }

        // Login Button
        Button(
            onClick = {
                // Sign in the user
                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            // Get the current user and their userType
                            val userId = auth.currentUser?.uid
                            val userRef = FirebaseDatabase.getInstance().getReference("users/$userId")
                            userRef.child("userType").get().addOnCompleteListener { typeTask ->
                                if (typeTask.isSuccessful) {
                                    val userType = typeTask.result?.value as? String
                                    if (userType == "Business") {
                                        navController.navigate("BusinessHomeScreen")
                                    } else {
                                        navController.navigate("CustomerHomeScreen")
                                    }
                                } else {
                                    // Handle error if fetching userType fails
                                    errorMessage = "Failed to fetch user type"
                                }
                            }
                        } else {
                            errorMessage = task.exception?.message ?: "Unknown error occurred"
                        }
                    }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF725E)),
            shape = RoundedCornerShape(12.dp)
        ) {
            if (isLoading) {
                CircularProgressIndicator(color = Color.White, strokeWidth = 2.dp)
            } else {
                Text(
                    text = "Login",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White
                )
            }
        }


        Spacer(modifier = Modifier.height(16.dp))

        // Don't Have an Account? Sign Up Text
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Don't have an account? ",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                text = "Sign Up",
                style = MaterialTheme.typography.bodySmall,
                color = Color(0xFFFF725E),
                modifier = Modifier.clickable {
                    navController.navigate("RegisterChoiceScreen")
                }
            )
        }
    }
}
