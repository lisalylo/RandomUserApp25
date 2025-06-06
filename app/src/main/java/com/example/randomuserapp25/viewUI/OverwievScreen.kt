package com.example.randomuserapp25.viewUI

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun OverviewScreen(viewModel: UserViewModel = hiltViewModel()) {
    val users by viewModel.users.observeAsState(emptyList())

    LazyColumn {
        items(users) { user ->
            Text(user.name)
        }
    }
}