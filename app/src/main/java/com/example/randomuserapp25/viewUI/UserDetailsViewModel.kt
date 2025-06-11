package com.example.randomuserapp25.viewUI

import com.example.randomuserapp25.data.UserRepository
import com.example.randomuserapp25.domain.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.SharingStarted
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.SavedStateHandle
import javax.inject.Inject

@HiltViewModel
class UserDetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val repository: UserRepository
) : ViewModel() {
    private val userId: String = checkNotNull(savedStateHandle["userId"])

    val user: StateFlow<User?> = repository.getUsers()
        .map { list -> list.find { it.id == userId } }
        .stateIn(viewModelScope, SharingStarted.Lazily, null)
}
