package com.example.myapplication.ui.customer

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.myapplication.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomerTasksScreen(navController: NavHostController) {
    val auth = FirebaseAuth.getInstance()
    val userId = auth.currentUser?.uid
    val databaseReference = FirebaseDatabase.getInstance().getReference("tasks/$userId")

    var tasks by remember { mutableStateOf(emptyMap<String, Pair<String, Boolean>>()) }
    var newTask by remember { mutableStateOf("") }
    var showAddTaskDialog by remember { mutableStateOf(false) }

    // Load tasks from Firebase
    LaunchedEffect(userId) {
        userId?.let {
            databaseReference.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val taskMap = mutableMapOf<String, Pair<String, Boolean>>()
                    snapshot.children.forEach { taskSnapshot ->
                        val taskKey = taskSnapshot.key
                        val taskDescription = taskSnapshot.child("description").getValue(String::class.java)
                        val isChecked = taskSnapshot.child("completed").getValue(Boolean::class.java) ?: false
                        if (taskKey != null && taskDescription != null) {
                            taskMap[taskKey] = taskDescription to isChecked
                        }
                    }
                    tasks = taskMap
                }

                override fun onCancelled(error: DatabaseError) {
                    // Handle the error if needed
                }
            })
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Task List",
                        style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                        color = Color.Black
                    )
                },
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = Color.White,
                    titleContentColor = Color.Black
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddTaskDialog = true },
                containerColor = Color(0xFFFF725E)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Task", tint = Color.White)
            }
        },
        bottomBar = {
            NavigationBar(containerColor = Color.White, contentColor = Color(0xFFFF725E)) {
                NavigationBarItem(
                    selected = false,
                    onClick = {
                        navController.navigate("CustomerHomeScreen") {
                            popUpTo("CustomerHomeScreen") { inclusive = true }
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
                    selected = true,
                    onClick = { /* Already on Tasks Screen */ },
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
                    onClick = {
                        navController.navigate("CustomerEventPlanning") {
                            popUpTo("CustomerEventPlanning") { inclusive = true }
                        }
                    },
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
                            popUpTo("ProfileScreen") { inclusive = true }
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
        },
        content = { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .background(Color(0xFFF5F5F5))
                    .padding(horizontal = 16.dp)
            ) {
                if (tasks.isEmpty()) {
                    // Empty State
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(top = 64.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_empty_task),
                            contentDescription = "No Tasks",
                            modifier = Modifier.size(128.dp)
                        )
                        Text(
                            text = "No Tasks found",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontSize = 16.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color.Gray
                            ),
                            modifier = Modifier.padding(top = 8.dp)
                        )
                        Text(
                            text = "Please add your tasks",
                            style = MaterialTheme.typography.bodySmall.copy(color = Color.Gray),
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                } else {
                    // Task List
                    Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                        tasks.forEach { (taskKey, taskPair) ->
                            val (task, isChecked) = taskPair
                            Card(
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp),
                                colors = CardDefaults.cardColors(containerColor = Color.White)
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Checkbox(
                                        checked = isChecked,
                                        onCheckedChange = { checked ->
                                            databaseReference.child(taskKey).child("completed").setValue(checked)
                                        }
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = task,
                                        style = MaterialTheme.typography.bodyMedium.copy(
                                            fontSize = 16.sp,
                                            color = if (isChecked) Color.Gray else Color.Black
                                        ),
                                        modifier = Modifier.weight(1f)
                                    )
                                    IconButton(onClick = {
                                        databaseReference.child(taskKey).removeValue()
                                    }) {
                                        Icon(
                                            painter = painterResource(id = R.drawable.ic_delete),
                                            contentDescription = "Delete Task",
                                            tint = Color.Red
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Add Task Dialog
            if (showAddTaskDialog) {
                AlertDialog(
                    onDismissRequest = { showAddTaskDialog = false },
                    title = { Text(text = "Add New Task") },
                    text = {
                        OutlinedTextField(
                            value = newTask,
                            onValueChange = { newTask = it },
                            label = { Text("Task Description") },
                            modifier = Modifier.fillMaxWidth()
                        )
                    },
                    confirmButton = {
                        Button(
                            onClick = {
                                if (newTask.isNotEmpty()) {
                                    databaseReference.push().setValue(
                                        mapOf(
                                            "description" to newTask,
                                            "completed" to false
                                        )
                                    )
                                    newTask = ""
                                    showAddTaskDialog = false
                                }
                            }
                        ) {
                            Text("Add")
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { showAddTaskDialog = false }) {
                            Text("Cancel")
                        }
                    }
                )
            }
        }
    )
}


