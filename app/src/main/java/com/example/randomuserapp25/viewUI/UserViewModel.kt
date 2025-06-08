package com.example.randomuserapp25.viewUI

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.randomuserapp25.data.UserRepository
import com.example.randomuserapp25.domain.User
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
class UserViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    val users: StateFlow<List<User>> = userRepository.getUsers()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    fun addUser(user: User) = viewModelScope.launch {
        userRepository.saveUser(user)
    }

    //sortieren, aktualisieren, löschen etc. hier machen
}