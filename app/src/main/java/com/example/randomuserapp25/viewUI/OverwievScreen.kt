
/*package com.example.randomuserapp25.viewUI

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.randomuserapp25.data.SortField
import com.example.randomuserapp25.domain.User
import com.example.randomuserapp25.viewUI.UserListViewModel
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OverviewScreen(
    navController: NavHostController,
    viewModel: UserListViewModel = hiltViewModel()
) {
    val users by viewModel.users.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val sortField by viewModel.sortField.collectAsState()
    val ascending by viewModel.sortAscending.collectAsState()
    var showMenu by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Random Users") },
                actions = {
                    Text(
                        text = "Sort",
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(end = 4.dp)
                    )
                    IconButton(onClick = { showMenu = true }) {
                        Icon(Icons.Default.Search, contentDescription = "Sortieren")
                    }
                    DropdownMenu(
                        expanded = showMenu,
                        onDismissRequest = { showMenu = false }
                    ) {
                        DropdownMenuItem(text = { Text("Name") }, onClick = {
                            viewModel.setSortField(SortField.NAME)
                            showMenu = false
                        })
                        DropdownMenuItem(text = { Text("Geburtstag") }, onClick = {
                            viewModel.setSortField(SortField.BIRTHDAY)
                            showMenu = false
                        })
                        Divider()
                        DropdownMenuItem(text = { Text(if (ascending) "Aufsteigend" else "Absteigend") }, onClick = {
                            viewModel.toggleSortOrder()
                            showMenu = false
                        })
                    }
                    IconButton(onClick = { navController.navigate("add") }) {
                        Icon(Icons.Default.Add, contentDescription = "Add User")
                    }
                    IconButton(onClick = { viewModel.refreshUsers() }) {
                        Icon(Icons.Default.Refresh, contentDescription = "Refresh")
                    }
                }
            )
        },
        floatingActionButton = {}, // FAB replaced by topbar action
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else {
                LazyColumn {
                    items(users) { user ->
                        UserListItem(user) { navController.navigate("detail/${user.id}") }
                    }
                }
            }
        }
    }
}
@Composable
fun UserListItem(
    user: User,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = user.photoUrl,
                contentDescription = "Profilbild von ${user.name}",
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = user.name,
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}*/
package com.example.randomuserapp25.viewUI

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.randomuserapp25.data.SortField
import com.example.randomuserapp25.domain.User
import java.util.UUID
import androidx.compose.material.icons.filled.Info

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OverviewScreen(
    navController: NavHostController,
    viewModel: UserListViewModel = hiltViewModel()
) {
    val users by viewModel.users.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val sortField by viewModel.sortField.collectAsState()
    val ascending by viewModel.sortAscending.collectAsState()
    var showMenu by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Random Users") },
                actions = {
                    IconButton(onClick = { showMenu = true }) {
                        Icon(Icons.Default.Info, contentDescription = "Sortieren")
                    }
                    DropdownMenu(
                        expanded = showMenu,
                        onDismissRequest = { showMenu = false }
                    ) {
                        DropdownMenuItem(text = { Text("Name") }, onClick = {
                            viewModel.setSortField(SortField.NAME)
                            showMenu = false
                        })
                        DropdownMenuItem(text = { Text("Geburtstag") }, onClick = {
                            viewModel.setSortField(SortField.BIRTHDAY)
                            showMenu = false
                        })
                        Divider()
                        DropdownMenuItem(
                            text = { Text(if (ascending) "Aufsteigend" else "Absteigend") },
                            onClick = {
                                viewModel.toggleSortOrder()
                                showMenu = false
                            }
                        )
                    }

                    // → Hier das Add-Icon korrekt auf "edit/{newId}" zeigen:
                    IconButton(onClick = {
                        val newId = UUID.randomUUID().toString()
                        navController.navigate("edit/$newId")
                    }) {
                        Icon(Icons.Default.Add, contentDescription = "Add User")
                    }

                    IconButton(onClick = { viewModel.refreshUsers() }) {
                        Icon(Icons.Default.Refresh, contentDescription = "Refresh")
                    }
                    IconButton(onClick = {
                        navController.navigate("ar")
                    }) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "AR Camera"
                        )
                    }
                }
            )
        },
        floatingActionButton = {} // kein FAB mehr, alles in der TopBar
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else {
                LazyColumn {
                    items(users) { user ->
                        UserListItem(user) {
                            navController.navigate("detail/${user.id}")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun UserListItem(
    user: User,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = user.photoUrl,
                contentDescription = "Profilbild von ${user.name}",
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = user.name,
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}
