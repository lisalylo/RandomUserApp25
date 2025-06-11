package com.example.randomuserapp25.arUI

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Color
import com.example.randomuserapp25.domain.User

/**
 * Overlay für User-Info, platziert an den Barcode-Ecken
 */
@Composable
fun UserInfoOverlay(
    user: User,
    corners: List<Offset>,
    onClick: () -> Unit
) {
    // Beispiel: nimm den ersten Eckpunkt für die Position
    val (x, y) = corners.firstOrNull()
        ?.let { IntOffset(it.x.toInt(), it.y.toInt()) }
        ?: IntOffset(0, 0)

    Card(
        modifier = Modifier
            .offset { IntOffset(x, y) }
            .clickable(onClick = onClick)
            .size(width = 150.dp, height = 80.dp)
            .background(Color.White)
            .padding(8.dp)
    ) {
        Column {
            Text(text = user.name)
            Text(text = user.phone)
        }
    }
}
