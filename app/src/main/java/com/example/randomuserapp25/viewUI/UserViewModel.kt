package com.example.randomuserapp25.viewUI

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.randomuserapp25.depInj.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.launch

class UserViewModel {
    @HiltViewModel
    class UserViewModel @Inject constructor(
        private val repository: UserRepository
    ) : ViewModel() {

        val users = repository.getLocalUsers().asLiveData()

        fun fetchAndStoreUsers() {
            viewModelScope.launch {
                val users = repository.fetchRemoteUsers()
                users.forEach { repository.saveUser(it) }
            }
        }
    }
}