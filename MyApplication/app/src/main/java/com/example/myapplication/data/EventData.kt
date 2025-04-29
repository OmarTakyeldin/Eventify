package com.example.myapplication.data

// Data class to represent an Event
data class Event(
    val name: String = "",
    val description: String = "",
    val guests: List<String> = emptyList()
)
