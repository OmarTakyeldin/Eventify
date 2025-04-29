package com.example.myapplication.data

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

// Data class to represent a Business object with default values for each field
data class Business(
    val businessName: String = "",
    val businessType: String = "",
    val location: String = "",
    val contactNumber: String = "",
    val email: String = ""
)

class BusinessRepository {

    // Function to submit the business setup details to Firebase
    fun submitSetup(
        businessName: String,
        businessType: String,
        location: String,
        contactNumber: String,
        email: String,
        onSuccess: () -> Unit, // Callback to invoke when the operation is successful
        onError: (String) -> Unit // Callback to invoke if an error occurs
    ) {
        // Check if all fields are filled
        if (businessName.isNotBlank() && businessType.isNotBlank() && location.isNotBlank() &&
            contactNumber.isNotBlank() && email.isNotBlank()) {

            // Get the current authenticated user ID from FirebaseAuth
            val userId = FirebaseAuth.getInstance().currentUser?.uid

            if (userId != null) {
                // Create a new Business object with the provided details
                val business = Business(
                    businessName = businessName,
                    businessType = businessType,
                    location = location,
                    contactNumber = contactNumber,
                    email = email
                )

                // Reference to Firebase database location for the user's business
                val databaseRef = FirebaseDatabase.getInstance().getReference("business/$userId")

                // Save the business data to Firebase under the user's unique ID
                databaseRef.setValue(business).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        // Once the business data is saved, update the user's profile to mark the business setup as complete
                        FirebaseDatabase.getInstance().getReference("users/$userId")
                            .child("isBusinessSetUp").setValue(true)
                        onSuccess() // Call the onSuccess callback
                    } else {
                        // In case of an error, invoke the onError callback with the error message
                        onError("Failed to save data: ${task.exception?.message}")
                    }
                }
            } else {
                // If user is not authenticated, return an error
                onError("User not authenticated")
            }
        } else {
            // If any fields are missing, return an error indicating that all fields are required
            onError("All fields are required")
        }
    }
}
