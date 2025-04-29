package com.example.myapplication.ui.business

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.material3.Icon
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavHostController
import com.example.myapplication.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

@Composable
fun BusinessHomeScreen(navController: NavHostController) {
    var businessName by remember { mutableStateOf("") }
    var registrationNumber by remember { mutableStateOf("") }
    var isBusinessSetUp by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(true) }

    val currentUser = FirebaseAuth.getInstance().currentUser
    val userId = currentUser?.uid
    val databaseReference = FirebaseDatabase.getInstance().getReference("users/$userId")

    // Fetch business info from Firebase
    LaunchedEffect(userId) {
        if (userId != null) {
            databaseReference.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    businessName = snapshot.child("businessName").value as? String ?: "Business Name"
                    registrationNumber = snapshot.child("registrationNumber").value as? String ?: "Not Registered"
                    isBusinessSetUp = snapshot.child("isBusinessSetUp").value as? Boolean ?: false
                    isLoading = false
                }
                override fun onCancelled(error: DatabaseError) {
                    isLoading = false
                }
            })
        }
    }

    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = Color.White,
                contentColor = Color(0xFFFF725E)
            ) {
                NavigationBarItem(
                    selected = true,
                    onClick = { /* Stay on Home */ },
                    icon = {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_home),
                            contentDescription = "Home"
                        )
                    },
                    label = { Text(text = "Home") }
                )
                NavigationBarItem(
                    selected = false,
                    onClick = { navController.navigate("BusinessRequestScreen") },
                    icon = {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_tasks),
                            contentDescription = "Requests"
                        )
                    },
                    label = { Text(text = "Requests") }
                )
                NavigationBarItem(
                    selected = false,
                    onClick = { navController.navigate("BusinessProfileScreen") },
                    icon = {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_profile),
                            contentDescription = "Profile"
                        )
                    },
                    label = { Text(text = "Profile") }
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF8F8F8)) // Light gray background
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            // Business Info Section
            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
            } else {
                // Business Info Box with rounded corners and padding
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White, shape = RoundedCornerShape(12.dp))
                        .padding(16.dp)
                ) {
                    Column {
                        Text(
                            text = "Welcome to $businessName!",
                            style = MaterialTheme.typography.headlineMedium,
                            color = Color.Black
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Registration Number: $registrationNumber",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.Gray
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // "Set Up Your Business Now!" Button (Only if not set up)
            if (!isBusinessSetUp) {
                Button(
                    onClick = { navController.navigate("BusinessSetupScreen") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF725E)),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(text = "Set Up Your Business Now!", color = Color.White)
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // "Upcoming Events" Section
            Text(
                text = "Upcoming Events",
                style = MaterialTheme.typography.headlineMedium,
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(8.dp))

            // No Events Section
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.no_events_icon),
                    contentDescription = "No Events Icon",
                    modifier = Modifier
                        .size(60.dp)
                        .padding(end = 16.dp)
                )
                Column {
                    Text(
                        text = "No Events",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Black
                    )
                    Text(
                        text = "Your event calendar is blank.\nApprove your next request now!",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray,
                        textAlign = TextAlign.Start
                    )
                }
            }
        }
    }
}
