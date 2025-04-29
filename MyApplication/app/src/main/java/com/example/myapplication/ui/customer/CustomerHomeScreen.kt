package com.example.myapplication.ui.customer

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.myapplication.R
import com.example.myapplication.data.Event
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

@Composable
fun CustomerHomeScreen(
    navigateToEventPlanning: () -> Unit,
    navigateToTasks: () -> Unit,
    navigateToProfile: () -> Unit
) {
    var events by remember { mutableStateOf<List<Pair<String, Event>>>(emptyList()) } // Pair of event ID and Event
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val currentUser = FirebaseAuth.getInstance().currentUser
    val userId = currentUser?.uid
    val databaseReference = FirebaseDatabase.getInstance().getReference("events/$userId")

    // Fetch events from Firebase
    LaunchedEffect(userId) {
        if (userId != null) {
            databaseReference.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val eventList = snapshot.children.mapNotNull { child ->
                        val event = child.getValue(Event::class.java)
                        val eventId = child.key
                        if (event != null && eventId != null) eventId to event else null
                    }
                    events = eventList
                    isLoading = false
                }

                override fun onCancelled(error: DatabaseError) {
                    errorMessage = error.message
                    isLoading = false
                }
            })
        } else {
            errorMessage = "User not authenticated"
            isLoading = false
        }
    }

    // Function to delete event by ID
    fun deleteEvent(eventId: String) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        if (userId != null) {
            val eventReference = FirebaseDatabase.getInstance().getReference("events/$userId/$eventId")
            eventReference.removeValue().addOnCompleteListener { task ->
            }
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
                    onClick = { /* Already on Home */ },
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
                    onClick = { navigateToTasks() },
                    icon = {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_tasks),
                            contentDescription = "Tasks"
                        )
                    },
                    label = { Text(text = "Tasks") }
                )
                NavigationBarItem(
                    selected = false,
                    onClick = { navigateToEventPlanning() },
                    icon = {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_create_event),
                            contentDescription = "Create Event"
                        )
                    },
                    label = { Text(text = "Create Event") }
                )
                NavigationBarItem(
                    selected = false,
                    onClick = { navigateToProfile() },
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
                .background(Color(0xFFFFF5F5)) // Light pink background
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            // Welcome Section
            Text(
                text = "Hello, welcome!",
                style = MaterialTheme.typography.headlineMedium,
                color = Color.Black,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            // Plan an Event Section
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp)
                    .background(Color.White, shape = RoundedCornerShape(12.dp))
                    .padding(16.dp)
            ) {
                Column {
                    Text(
                        text = "Now that you are all set.\nLet’s make your events extraordinary,\nstarting right here!",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Black
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = navigateToEventPlanning,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF725E)),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = "Plan an Event",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.White
                        )
                    }
                }
            }

            // Upcoming Events Section
            Text(
                text = "Upcoming Events",
                style = MaterialTheme.typography.headlineSmall,
                color = Color.Black,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            if (isLoading) {
                // Loading Indicator
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Color(0xFFFF725E))
                }
            } else if (errorMessage != null) {
                // Error Message
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = errorMessage ?: "An unknown error occurred",
                        color = Color.Red,
                        textAlign = TextAlign.Center
                    )
                }
            } else if (events.isEmpty()) {
                // No Events Message
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
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
                            text = "Your event calendar is blank.\nUse Eventify to plan your next event now!",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.Gray,
                            textAlign = TextAlign.Start
                        )
                    }
                }
            } else {
                // Display Events
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(events) { (eventId, event) ->
                        Card(
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween // Align text and icon with space between
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.no_events_icon),
                                    contentDescription = "Event Icon",
                                    tint = Color(0xFFFF725E),
                                    modifier = Modifier.size(48.dp)
                                )
                                Spacer(modifier = Modifier.width(16.dp))
                                Column(modifier = Modifier.weight(1f)) { // Allow text to take remaining space
                                    Text(
                                        text = event.name,
                                        style = MaterialTheme.typography.bodyLarge,
                                        color = Color.Black
                                    )
                                    Text(
                                        text = event.description,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = Color.Gray
                                    )
                                }
                                // Delete button
                                IconButton(
                                    onClick = {
                                        eventId?.let { deleteEvent(it) }
                                    }
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Delete,
                                        contentDescription = "Delete Event",
                                        tint = Color.Red
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
