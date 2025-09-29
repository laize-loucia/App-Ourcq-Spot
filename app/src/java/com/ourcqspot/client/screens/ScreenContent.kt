package com.ourcqspot.client.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration

@Composable
fun ScreenContent(name: String,
                  useHypertextStyle: Boolean = false,
                  onClick: () -> Unit) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        // Flemme de le faire bien sans répétition de code,
        // il est juste 1h du matin et c'est vraiment seulement un prototype
        // et il faudra tout changer ensuite (contenu + révision conteneurs)
        if (useHypertextStyle) {
            Text(
                modifier = Modifier.clickable { onClick() },
                text = name,
                fontSize = MaterialTheme.typography.headlineMedium.fontSize,
                fontWeight = FontWeight.Bold,
                color = Color.Blue,
                textDecoration = TextDecoration.Underline
            )
        } else {
            Text(
                modifier = Modifier.clickable { onClick() },
                text = name,
                fontSize = MaterialTheme.typography.headlineMedium.fontSize,
                fontWeight = FontWeight.Bold
            )
        }
    }
}