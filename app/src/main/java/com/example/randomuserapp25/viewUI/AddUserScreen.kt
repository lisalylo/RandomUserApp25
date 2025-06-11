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
import com.example.randomuserapp25.viewUI.AddUserViewModel
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
fun AddUserScreen(
    navController: NavHostController,
    viewModel: AddUserViewModel = hiltViewModel()
) {
    val name by viewModel.name.collectAsState()
    val birthDateIso by viewModel.birthDate.collectAsState()
    val phone by viewModel.phone.collectAsState()
    val photoUrl by viewModel.photoUrl.collectAsState()
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    val displayFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
    var dateText by remember(birthDateIso) {
        mutableStateOf(
            runCatching {
                Instant.parse(birthDateIso)
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate()
                    .format(displayFormatter)
            }.getOrDefault("")
        )
    }

    val calendar = Calendar.getInstance()
    val datePicker = DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            val localDate = LocalDate.of(year, month + 1, dayOfMonth)
            dateText = localDate.format(displayFormatter)
            val iso = localDate.atStartOfDay(ZoneOffset.UTC).toInstant().toString()
            viewModel.onBirthDateChange(iso)
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Add User") },
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
        },
        content = { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = name,
                    onValueChange = viewModel::onNameChange,
                    label = { Text("Name") },
                    modifier = Modifier.fillMaxWidth()
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { datePicker.show() }
                ) {
                    OutlinedTextField(
                        value = dateText,
                        onValueChange = {},
                        label = { Text("Birth Date") },
                        readOnly = true,
                        enabled = false,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                OutlinedTextField(
                    value = phone,
                    onValueChange = viewModel::onPhoneChange,
                    label = { Text("Phone") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = photoUrl,
                    onValueChange = viewModel::onPhotoUrlChange,
                    label = { Text("Photo URL") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    )
}