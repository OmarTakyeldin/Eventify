package com.example.myapplication.ui.customer

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.myapplication.R
import com.example.myapplication.CustomerFunctionalities.CustomerProfileOptions
import com.example.myapplication.CustomerFunctionalities.EditProfileUtility
import com.google.firebase.auth.FirebaseAuth

@Composable
fun CustomerProfileScreen(
    navController: NavHostController
) {
    var userName by remember { mutableStateOf("Guest") }
    var userEmail by remember { mutableStateOf("") }
    val user = FirebaseAuth.getInstance().currentUser

    // Fetch user details
    LaunchedEffect(Unit) {
        user?.let {
            userEmail = it.email ?: "Unknown Email"
            userName = it.displayName ?: "Guest"
        }
    }

    Scaffold(
        containerColor = Color(0xFFF5F5F5),
        bottomBar = {
            NavigationBar(
                containerColor = Color.White,
                contentColor = Color(0xFFFF725E)
            ) {
                NavigationBarItem(
                    selected = false,
                    onClick = { navController.navigate("CustomerHomeScreen") },
                    icon = { Icon(painter = painterResource(id = R.drawable.ic_home), contentDescription = "Home") },
                    label = { Text(text = "Home") }
                )
                NavigationBarItem(
                    selected = false,
                    onClick = { navController.navigate("TasksScreen") },
                    icon = { Icon(painter = painterResource(id = R.drawable.ic_tasks), contentDescription = "Tasks") },
                    label = { Text(text = "Tasks") }
                )
                NavigationBarItem(
                    selected = false,
                    onClick = { navController.navigate("CustomerEventPlanning") },
                    icon = { Icon(painter = painterResource(id = R.drawable.ic_create_event), contentDescription = "Create Event") },
                    label = { Text(text = "Create Event") }
                )
                NavigationBarItem(
                    selected = true,
                    onClick = { /* Already on Profile */ },
                    icon = { Icon(painter = painterResource(id = R.drawable.ic_profile), contentDescription = "Profile") },
                    label = { Text(text = "Profile") }
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
        ) {
            // User Information Section
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 24.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .background(Color(0xFFFFDAD6), shape = RoundedCornerShape(32.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = userName.take(2).uppercase(),
                        style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                        color = Color(0xFFFF725E)
                    )
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(
                        text = userName,
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                    )
                    Text(
                        text = userEmail,
                        style = MaterialTheme.typography.bodyMedium.copy(color = Color.Gray)
                    )
                }
            }
            Divider(color = Color(0xFFE0E0E0), thickness = 1.dp)

            // Profile Options
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.padding(vertical = 16.dp)
            ) {
                ProfileOption("Edit Profile") {
                    navController.navigate("EditProfileScreen") // Navigate to Edit Profile Screen
                }
                ProfileOption("Contact Us") {
                    navController.navigate("ContactUsScreen") // Navigate to Contact Us Screen
                }
                ProfileOption("Sign Out") {
                    FirebaseAuth.getInstance().signOut()
                    navController.navigate("LoginScreen") {
                        popUpTo(0) // Clear backstack
                    }
                }
            }
        }
    }
}


@Composable
fun ProfileOption(optionText: String, onClick: () -> Unit) {
    CustomerProfileOptions(onClick, optionText)
}

@Composable
fun EditProfileScreen(
    navController: NavHostController
) {
    EditProfileUtility(navController)
}




