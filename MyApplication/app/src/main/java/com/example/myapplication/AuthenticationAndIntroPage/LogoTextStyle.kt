package com.example.myapplication.AuthenticationAndIntroPage

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle

@Composable
fun LogoText() {
    Text(
        text = buildAnnotatedString {
            withStyle(style = SpanStyle(color = Color(0xFFFF5757))) { // Red color for "Event"
                append("Event")
            }
            withStyle(style = SpanStyle(color = Color(0xFF6B0000))) { // Dark brown for "ify"
                append("ify")
            }
        },
        style = MaterialTheme.typography.headlineMedium
    )
}