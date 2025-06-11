package com.example.randomuserapp25.viewUI

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.randomuserapp25.data.UserRepository
import com.example.randomuserapp25.domain.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class EditUserViewModel @Inject constructor(
    private val repository: UserRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val userId: String = checkNotNull(savedStateHandle["userId"])

    // Original User fetched from repository
    val originalUser: StateFlow<User?> = repository.getUsers()
        .map { list -> list.find { it.id == userId } }
        .stateIn(viewModelScope, SharingStarted.Lazily, null)

    // Editable fields
    private val _name = MutableStateFlow("")
    val name: StateFlow<String> = _name.asStateFlow()

    private val _birthDate = MutableStateFlow("")
    val birthDate: StateFlow<String> = _birthDate.asStateFlow()

    private val _phone = MutableStateFlow("")
    val phone: StateFlow<String> = _phone.asStateFlow()

    private val _photoUrl = MutableStateFlow("")
    val photoUrl: StateFlow<String> = _photoUrl.asStateFlow()

    init {
        // Initialize editable fields with original values
        viewModelScope.launch {
            originalUser.collect { user ->
                user?.let {
                    _name.value = it.name
                    _birthDate.value = it.birthDate
                    _phone.value = it.phone
                    _photoUrl.value = it.photoUrl
                }
            }
        }
    }

    fun onNameChange(value: String) { _name.value = value }
    fun onBirthDateChange(value: String) { _birthDate.value = value }
    fun onPhoneChange(value: String) { _phone.value = value }
    fun onPhotoUrlChange(value: String) { _photoUrl.value = value }

    fun saveUser() {
        viewModelScope.launch {
            val userToSave = User(
                id = userId,
                name = name.value,
                photoUrl = photoUrl.value,
                birthDate = birthDate.value,
                phone = phone.value
            )
            repository.saveUser(userToSave)
        }
    }
}
