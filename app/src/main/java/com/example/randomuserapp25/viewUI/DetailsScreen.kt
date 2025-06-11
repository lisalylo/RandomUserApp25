package com.example.randomuserapp25.viewUI

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.randomuserapp25.viewUI.UserDetailsViewModel
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    userId: String,
    navController: NavHostController,
    viewModel: UserDetailsViewModel = hiltViewModel()
) {
    val userState by viewModel.user.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("User Details") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = {
                        // Hier springt es in den EditScreen mit dieser ID
                        navController.navigate("edit/$userId")
                    }) {
                        Icon(Icons.Default.Edit, contentDescription = "Edit User")
                    }
                }
            )
        }

    ) { padding ->
        userState?.let { user ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(start = 16.dp, top = 48.dp, end = 16.dp, bottom = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                AsyncImage(
                    model = user.photoUrl,
                    contentDescription = user.name,
                    modifier = Modifier
                        .size(200.dp)
                        .padding(8.dp)
                )
                Text(text = user.name, style = MaterialTheme.typography.headlineSmall)
                Spacer(modifier = Modifier.height(8.dp))
                FormattedBirthday(user.birthDate)
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = user.phone, style = MaterialTheme.typography.bodyLarge)
            }
        } ?: Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    }
}

/** Birthdate "normal" anzeigen - java time (impl)
 * 1. Parsen, 2. in localDate umwandeln, 3. formatieren
 */

@Composable
fun FormattedBirthday(birthDateIso: String) {
    val instant = try {
        Instant.parse(birthDateIso)
    } catch (_: Exception) {
        null
    }
    val localDate = instant
        ?.atZone(ZoneId.systemDefault())
        ?.toLocalDate()
    val formatted = localDate
        ?.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))
        ?: "–"
    Text(
        text = formatted,
        style = MaterialTheme.typography.bodyMedium
    )
}
