package com.example.myapplication.ui.customer

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.myapplication.R
import com.example.myapplication.data.Event
import com.google.firebase.auth.FirebaseAuth
import java.time.LocalDate
import com.google.firebase.database.FirebaseDatabase
import com.example.myapplication.data.Calendar


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomerEventPlanningScreen(
    navController: NavHostController,
    onSubmit: (String, String) -> Unit
) {
    var currentStep by remember { mutableStateOf(1) } // Track the current step
    var selectedEvent by remember { mutableStateOf("") } // Store the selected event
    var eventName by remember { mutableStateOf("") } // Store the custom event name
    var selectedDate by remember { mutableStateOf<LocalDate?>(null) } // Store the selected date
    var selectedVendor by remember { mutableStateOf("") } // Store the selected food vendor
    var selectedSinger by remember {mutableStateOf("")} // Store the selected singer
    var selectedFlorist by remember {mutableStateOf("")} // Store the selected Florist
    var guestList by remember { mutableStateOf(mutableListOf<Pair<String, Boolean>>()) } // List to hold guest names and checkbox states
    var newGuest by remember { mutableStateOf("") } // Temporary input for a new guest
    var customEvent by remember {mutableStateOf("")} // Store the selected Florist
    val context = LocalContext.current // Properly initialize the context

    Scaffold(
        containerColor = Color(0xFFFF9A9A), // Set the consistent background color for the whole screen
        bottomBar = {
            NavigationBar(
                containerColor = Color.White,
                contentColor = Color(0xFFFF725E)
            ) {
                NavigationBarItem(
                    selected = false,
                    onClick = {
                        navController.navigate("CustomerHomeScreen") {
                            popUpTo("CustomerHomeScreen") { inclusive = true } // Prevents stacking
                        }
                    },
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
                    onClick = {
                        navController.navigate("TasksScreen") {
                            popUpTo("TasksScreen") { inclusive = true } // Prevents stacking
                        }
                    },
                    icon = {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_tasks),
                            contentDescription = "Tasks"
                        )
                    },
                    label = { Text(text = "Tasks") }
                )
                NavigationBarItem(
                    selected = true,
                    onClick = { /* Already on Create Event */ },
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
                    onClick = {
                        navController.navigate("ProfileScreen") {
                            popUpTo("ProfileScreen") { inclusive = true } // Prevents stacking
                        }
                    },
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

    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Color(0xFFF5F5F5)) // Consistent background color for the Column
                .padding(horizontal = 16.dp)
        ) {


            //Title section
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 24.dp, bottom = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (currentStep > 1) { // Show Back Button only if not on the first step
                        IconButton(onClick = { currentStep -= 1 }) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_back),
                                contentDescription = "Back",
                                tint = Color.Black
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(8.dp)) // Space between back button and title
                    Text(
                        text = when (currentStep) {
                            1 -> "1 of 7: Pick an event!"
                            2 -> "2 of 7: Pick an event name!"
                            3 -> "3 of 7: Pick a date!"
                            4 -> "4 of 7: Food vendors"
                            5 -> "5 of 7: Singers"
                            6 -> "6 of 7: Florists"
                            7 -> "7 of 7: Guest list"
                            8 -> "Review and Submit!"
                            else -> ""
                        },
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        ),
                        color = Color.Black
                    )
                }
                if (currentStep in 4..7) { // Show Skip button only on steps 4-7
                    TextButton(onClick = {
                        when (currentStep) {
                            4 -> {
                                selectedVendor = "" // Discard selected vendor
                                currentStep = 5
                            }
                            5 -> {
                                selectedSinger = "" // Discard selected singer
                                currentStep = 6
                            }
                            6 -> {
                                selectedFlorist = "" // Discard selected florist
                                currentStep = 7
                            }
                            7 -> {
                                guestList.clear() // Clear guest list
                                currentStep = 8
                            }
                        }
                    }) {
                        Text(
                            text = "Skip",
                            color = Color(0xFFFF725E),
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }


            when (currentStep) {
                1 -> {
                    // Step 1: Pick an Event
                    val eventOptions = listOf(
                        "Wedding",
                        "Birthday",
                        "Baby shower",
                        "Prom",
                        "Graduation",
                        "House party",
                        "Other"
                    )

                    Column(
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                    ) {
                        eventOptions.forEach { event ->
                            Button(
                                onClick = {
                                    selectedEvent = event
                                    if (event != "Other") customEvent = "" // Clear custom event if not "Other"
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(56.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF5757)),
                                shape = RoundedCornerShape(16.dp)
                            ) {
                                Text(
                                    text = event,
                                    style = MaterialTheme.typography.bodyMedium.copy(fontSize = 16.sp),
                                    color = Color.White
                                )
                            }
                        }

                        // Custom event input field
                        if (selectedEvent == "Other") {
                            Spacer(modifier = Modifier.height(16.dp))
                            OutlinedTextField(
                                value = customEvent,
                                onValueChange = { customEvent = it },
                                placeholder = { Text(text = "Please specify your event") },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(16.dp),
                                colors = TextFieldDefaults.outlinedTextFieldColors(
                                    focusedBorderColor = Color(0xFFFF5757),
                                    unfocusedBorderColor = Color(0xFFCCCCCC)
                                )
                            )
                        }
                    }

                    // Confirm Button for Step 1
                    Button(
                        onClick = {
                            if (selectedEvent.isNotEmpty() && (selectedEvent != "Other" || customEvent.isNotEmpty())) {
                                currentStep = 2 // Move to Step 2
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                            .padding(bottom = 16.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF725E)),
                        shape = RoundedCornerShape(16.dp),
                        enabled = selectedEvent.isNotEmpty() && (selectedEvent != "Other" || customEvent.isNotEmpty()) // Disable if no input for "Other"
                    ) {
                        Text(
                            text = "Confirm",
                            style = MaterialTheme.typography.bodyMedium.copy(fontSize = 16.sp),
                            color = Color.White
                        )
                    }
                }


                2 -> {
                    // Step 2: Pick an Event Name
                    OutlinedTextField(
                        value = eventName,
                        onValueChange = { eventName = it },
                        placeholder = { Text(text = "Enter event name") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 16.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            focusedBorderColor = Color(0xFFFF5757),
                            unfocusedBorderColor = Color(0xFFCCCCCC)
                        )
                    )

                    // Confirm Button for Step 2
                    Button(
                        onClick = {
                            if (eventName.isNotEmpty()) {
                                currentStep = 3 // Move to Step 3
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF725E)),
                        shape = RoundedCornerShape(16.dp),
                        enabled = eventName.isNotEmpty() // Disable if no event name is entered
                    ) {
                        Text(
                            text = "Confirm",
                            style = MaterialTheme.typography.bodyMedium.copy(fontSize = 16.sp),
                            color = Color.White
                        )
                    }
                }

                3 -> {
                    // Step 3: Pick a Date
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 16.dp)
                    ) {
                        // Calendar Component
                        AndroidCalendar(
                            onDateSelected = { selectedDate = it },
                        )

                        // Confirm Button at the bottom
                        Button(
                            onClick = {
                                currentStep = 4 // Move to Step 4
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp)
                                .align(Alignment.BottomCenter) // Position the button at the bottom of the screen
                                .padding(bottom = 16.dp), // Add spacing above the navigation bar
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF725E)),
                            shape = RoundedCornerShape(16.dp),
                            enabled = selectedDate != null // Disable if no date is selected
                        ) {
                            Text(
                                text = "Confirm",
                                style = MaterialTheme.typography.bodyMedium.copy(fontSize = 16.sp),
                                color = Color.White
                            )
                        }
                    }
                }

                4 -> {
                    // Step 4: Food Vendors
                    val vendors = listOf(
                        "TLM Food Expo",
                        "Genting Sustainbiz",
                        "Burger King"
                    )

                    Column(
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(vertical = 16.dp)
                    ) {
                        vendors.forEach { vendor ->
                            Card(
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 8.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = Color(0xFFFFF5F5)
                                )
                            ) {
                                Column(
                                    modifier = Modifier.padding(16.dp)
                                ) {
                                    Text(text = "Email:\nNumber:", color = Color.Gray)
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Button(
                                        onClick = { selectedVendor = vendor }, // Update selectedVendor
                                        modifier = Modifier.fillMaxWidth(),
                                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF725E)),
                                        shape = RoundedCornerShape(12.dp)
                                    ) {
                                        Text(text = vendor, color = Color.White)
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.weight(1f)) // Push the Confirm button to the bottom

                        // Confirm Button for Step 4
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 16.dp) // Add spacing above the navigation bar
                        ) {
                            Button(
                                onClick = {
                                    if (selectedVendor.isNotEmpty()) {
                                        currentStep = 5 // Move to Step 5
                                    }
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(56.dp)
                                    .align(Alignment.Center), // Ensure the button is centered horizontally
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF725E)),
                                shape = RoundedCornerShape(16.dp),
                                enabled = selectedVendor.isNotEmpty() // Enable only if a vendor is selected
                            ) {
                                Text(
                                    text = "Confirm",
                                    style = MaterialTheme.typography.bodyMedium.copy(fontSize = 16.sp),
                                    color = Color.White
                                )
                            }
                        }
                    }
                }

                5 -> {
                    // Step 5: Singers
                    val singers = listOf(
                        "Tamer Hosny",
                        "Amr Diab",
                        "Hamada Hilal",
                        "Mohamed Hamaki",
                        "Cairokee"
                    )

                    // Make the content scrollable
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(vertical = 16.dp)
                            .verticalScroll(rememberScrollState()) // Add vertical scrolling
                    ) {
                        singers.forEach { singer ->
                            Card(
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 8.dp, vertical = 8.dp), // Adjust vertical padding for better spacing
                                colors = CardDefaults.cardColors(
                                    containerColor = Color(0xFFFFF5F5) // Matches Step 4 background color
                                )
                            ) {
                                Column(
                                    modifier = Modifier.padding(16.dp)
                                ) {
                                    Text(text = "Email:\nNumber:", color = Color.Gray)
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Button(
                                        onClick = { selectedSinger = singer }, // Updates selected singer
                                        modifier = Modifier.fillMaxWidth(),
                                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF725E)),
                                        shape = RoundedCornerShape(12.dp)
                                    ) {
                                        Text(text = singer, color = Color.White)
                                    }
                                }
                            }
                        }

                        // Add spacing at the bottom to ensure the Confirm button is not covered
                        Spacer(modifier = Modifier.height(16.dp))

                        // Confirm Button for Step 5
                        Button(
                            onClick = {
                                if (selectedSinger.isNotEmpty()) {
                                    currentStep = 6 // Move to Step 6
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp)
                                .padding(horizontal = 8.dp), // Add horizontal padding for better alignment
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF725E)),
                            shape = RoundedCornerShape(16.dp),
                            enabled = selectedSinger.isNotEmpty() // Enable only if a singer is selected
                        ) {
                            Text(
                                text = "Confirm",
                                style = MaterialTheme.typography.bodyMedium.copy(fontSize = 16.sp),
                                color = Color.White
                            )
                        }
                    }
                }
                6 -> {
                    // Step 6: Florists
                    val florists = listOf(
                        "BloomThis",
                        "Flower Chimp",
                        "Summer Pots Florist",
                        "Petal Passion",
                        "Elegant Blooms"
                    )

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(vertical = 16.dp)
                            .verticalScroll(rememberScrollState()) // Enable scrolling
                    ) {
                        florists.forEach { florist ->
                            Card(
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 8.dp, vertical = 8.dp), // Adjust padding
                                colors = CardDefaults.cardColors(
                                    containerColor = Color(0xFFFFF5F5) // Matches styling of Step 4 and 5
                                )
                            ) {
                                Column(
                                    modifier = Modifier.padding(16.dp)
                                ) {
                                    Text(text = "Email:\nNumber:", color = Color.Gray)
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Button(
                                        onClick = { selectedFlorist = florist }, // Track selected florist
                                        modifier = Modifier.fillMaxWidth(),
                                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF725E)),
                                        shape = RoundedCornerShape(12.dp)
                                    ) {
                                        Text(text = florist, color = Color.White)
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.weight(1f)) // Push Confirm button to the bottom

                        // Confirm Button for Step 6
                        Button(
                            onClick = {
                                if (selectedFlorist.isNotEmpty()) {
                                    currentStep = 7 // Move to Step 7
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp)
                                .padding(horizontal = 8.dp), // Add horizontal padding for alignment
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF725E)),
                            shape = RoundedCornerShape(16.dp),
                            enabled = selectedFlorist.isNotEmpty() // Enable only if a florist is selected
                        ) {
                            Text(
                                text = "Confirm",
                                style = MaterialTheme.typography.bodyMedium.copy(fontSize = 16.sp),
                                color = Color.White
                            )
                        }
                    }
                }


                7 -> {
                    // Step 7: Editable Guest List with Functional Checkboxes

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp)
                            .verticalScroll(rememberScrollState()) // Enable scrolling
                    ) {
                        // Guest List Display
                        guestList.forEachIndexed { index, guest ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Checkbox(
                                    checked = guest.second, // Bind to the checkbox state
                                    onCheckedChange = { isChecked ->
                                        val updatedList = guestList.toMutableList()
                                        updatedList[index] = guest.copy(second = isChecked) // Update the state in the list
                                        guestList = updatedList // Trigger recomposition
                                    },
                                    colors = CheckboxDefaults.colors(checkedColor = Color(0xFFFF725E))
                                )
                                Text(
                                    text = guest.first,
                                    style = MaterialTheme.typography.bodyMedium.copy(fontSize = 16.sp),
                                    modifier = Modifier.padding(start = 8.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Add New Guest Input
                        OutlinedTextField(
                            value = newGuest,
                            onValueChange = { newGuest = it },
                            placeholder = { Text(text = "Add a guest") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 8.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = TextFieldDefaults.outlinedTextFieldColors(
                                focusedBorderColor = Color(0xFFFF725E),
                                unfocusedBorderColor = Color(0xFFCCCCCC)
                            ),
                            textStyle = MaterialTheme.typography.bodyMedium.copy(fontSize = 16.sp)
                        )

                        // Add Button
                        Button(
                            onClick = {
                                if (newGuest.isNotBlank()) {
                                    guestList =
                                        (guestList + Pair(newGuest.trim(), false)).toMutableList() // Add guest with unchecked state
                                    newGuest = "" // Clear input field
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF725E)),
                            shape = RoundedCornerShape(12.dp),
                            enabled = newGuest.isNotBlank() // Disable if input is empty
                        ) {
                            Text(
                                text = "Add",
                                style = MaterialTheme.typography.bodyMedium.copy(color = Color.White, fontSize = 16.sp)
                            )
                        }

                        Spacer(modifier = Modifier.weight(1f)) // Push the Save button to the bottom

                        // Save Button
                        Button(
                            onClick = {
                                currentStep = 8
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp)
                                .padding(top = 16.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50)), // Green for save
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Text(
                                text = "SAVE",
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontSize = 16.sp,
                                    color = Color.White
                                )
                            )
                        }
                    }
                }
                8 -> {
                    // Final Page: Review & Submit
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp)
                            .verticalScroll(rememberScrollState()) // Enable scrolling for large content
                    ) {
                        // Create the event description dynamically
                        val eventDescription = buildString {
                            append("Event: ${if (selectedEvent == "Other") customEvent else selectedEvent}\n")
                            append("Event Name: $eventName\n")
                            selectedDate?.let { append("Date: $it\n") }
                            if (selectedVendor.isNotEmpty()) append("Food Vendor: $selectedVendor\n")
                            if (selectedFlorist.isNotEmpty()) append("Florist: $selectedFlorist\n")
                            if (selectedSinger.isNotEmpty()) append("Singer: $selectedSinger\n")
                            if (guestList.isNotEmpty()) {
                                append("Guests:\n")
                                guestList.forEach { guest ->
                                    append(" - ${guest.first}\n")
                                }
                            }
                        }

                        // Section Header
                        Text(
                            text = "Review Your Event Details",
                            style = MaterialTheme.typography.headlineSmall,
                            color = Color.Black,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )

                        // Displaying review details with styling
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color.White, shape = RoundedCornerShape(12.dp))
                                .padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            // Displaying event type
                            Text(
                                text = "Event: ${if (selectedEvent == "Other") customEvent else selectedEvent}",
                                style = MaterialTheme.typography.bodyMedium.copy(fontSize = 16.sp),
                                color = Color(0xFF555555)
                            )

                            // Displaying event name
                            Text(
                                text = "Event Name: $eventName",
                                style = MaterialTheme.typography.bodyMedium.copy(fontSize = 16.sp),
                                color = Color(0xFF555555)
                            )

                            // Displaying event date
                            selectedDate?.let {
                                Text(
                                    text = "Date: $it",
                                    style = MaterialTheme.typography.bodyMedium.copy(fontSize = 16.sp),
                                    color = Color(0xFF555555)
                                )
                            }

                            // Displaying food vendor
                            if (selectedVendor.isNotEmpty()) {
                                Text(
                                    text = "Food Vendor: $selectedVendor",
                                    style = MaterialTheme.typography.bodyMedium.copy(fontSize = 16.sp),
                                    color = Color(0xFF555555)
                                )
                            }

                            // Displaying florist
                            if (selectedFlorist.isNotEmpty()) {
                                Text(
                                    text = "Florist: $selectedFlorist",
                                    style = MaterialTheme.typography.bodyMedium.copy(fontSize = 16.sp),
                                    color = Color(0xFF555555)
                                )
                            }

                            // Displaying singer
                            if (selectedSinger.isNotEmpty()) {
                                Text(
                                    text = "Singer: $selectedSinger",
                                    style = MaterialTheme.typography.bodyMedium.copy(fontSize = 16.sp),
                                    color = Color(0xFF555555)
                                )
                            }

                            // Displaying guest list
                            if (guestList.isNotEmpty()) {
                                Text(
                                    text = "Guest List:",
                                    style = MaterialTheme.typography.bodyMedium.copy(fontSize = 16.sp),
                                    color = Color(0xFF555555)
                                )
                                guestList.forEach { guest ->
                                    Text(
                                        text = " - ${guest.first}",
                                        style = MaterialTheme.typography.bodySmall.copy(fontSize = 14.sp),
                                        color = Color(0xFF777777)
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.weight(1f)) // Push the button to the bottom

                        // Submit Button
                        Button(
                            onClick = {
                                val userId = FirebaseAuth.getInstance().currentUser?.uid
                                if (userId != null) {
                                    val databaseReference = FirebaseDatabase.getInstance().getReference("events/$userId")

                                    val event = Event(
                                        name = eventName,
                                        description = eventDescription,
                                        guests = guestList.map { it.first }
                                    )

                                    databaseReference.push().setValue(event).addOnCompleteListener { task ->
                                        if (task.isSuccessful) {
                                            Toast.makeText(context, "Event added successfully", Toast.LENGTH_SHORT).show()
                                            navController.navigate("CustomerHomeScreen")
                                        } else {
                                            Toast.makeText(context, "Failed to add event", Toast.LENGTH_SHORT).show()
                                        }
                                    }
                                } else {
                                    Toast.makeText(context, "User not authenticated", Toast.LENGTH_SHORT).show()
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50)), // Green for submit
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Text(
                                text = "SUBMIT",
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontSize = 16.sp,
                                    color = Color.White
                                )
                            )
                        }
                    }
                }

            }
        }
    }
}

@SuppressLint("NewApi")
@Composable
fun AndroidCalendar(
    onDateSelected: (LocalDate) -> Unit
) {
    Calendar(onDateSelected)
}


