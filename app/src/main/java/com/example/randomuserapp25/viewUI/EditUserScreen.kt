package com.example.randomuserapp25.viewUI

import android.app.DatePickerDialog
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.randomuserapp25.viewUI.EditUserViewModel
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditUserScreen(
    navController: NavHostController,
    userId: String,
    viewModel: EditUserViewModel = hiltViewModel()
) {
    val original by viewModel.originalUser.collectAsState()
    val name by viewModel.name.collectAsState()
    val birthDateIso by viewModel.birthDate.collectAsState()
    val phone by viewModel.phone.collectAsState()
    val photoUrl by viewModel.photoUrl.collectAsState()
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    val fmt = DateTimeFormatter.ofPattern("dd.MM.yyyy")
    var dateText by remember(birthDateIso) {
        mutableStateOf(
            runCatching {
                Instant.parse(birthDateIso)
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate()
                    .format(fmt)
            }.getOrDefault("")
        )
    }

    val cal = Calendar.getInstance()
    val dpd = DatePickerDialog(
        context,
        { _, y, m, d ->
            val date = LocalDate.of(y, m + 1, d)
            dateText = date.format(fmt)
            val iso = date.atStartOfDay(ZoneOffset.UTC).toInstant().toString()
            viewModel.onBirthDateChange(iso)
        },
        cal.get(Calendar.YEAR),
        cal.get(Calendar.MONTH),
        cal.get(Calendar.DAY_OF_MONTH)
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (original == null) "Add User" else "Edit User") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.Close, contentDescription = "Cancel")
                    }
                },
                actions = {
                    IconButton(onClick = {
                        scope.launch {
                            viewModel.saveUser()
                            navController.popBackStack()
                        }
                    }) {
                        Icon(Icons.Default.Check, contentDescription = "Save")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedTextField(
                value = name,
                onValueChange = { viewModel.onNameChange(it) },
                label = { Text("Name") },
                modifier = Modifier.fillMaxWidth()
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { dpd.show() }
            ) {
                OutlinedTextField(
                    value = dateText,
                    onValueChange = {},                     // bleibt leer
                    label = { Text("Birth Date") },
                    readOnly = true,
                    enabled = false,                        // hier deaktivieren!
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { dpd.show() }           // und hier drauf reagieren
                )
            }
            OutlinedTextField(
                value = phone,
                onValueChange = { viewModel.onPhoneChange(it) },
                label = { Text("Phone") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = photoUrl,
                onValueChange = { viewModel.onPhotoUrlChange(it) },
                label = { Text("Photo URL") },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}