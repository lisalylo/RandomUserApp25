package com.example.randomuserapp25.viewUI

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.randomuserapp25.data.UserRepository
import com.example.randomuserapp25.domain.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class AddUserViewModel @Inject constructor(
    private val repository: UserRepository
) : ViewModel() {
    private val _name = MutableStateFlow("")
    val name: StateFlow<String> = _name.asStateFlow()

    private val _birthDate = MutableStateFlow("")
    val birthDate: StateFlow<String> = _birthDate.asStateFlow()

    private val _phone = MutableStateFlow("")
    val phone: StateFlow<String> = _phone.asStateFlow()

    private val _photoUrl = MutableStateFlow("")
    val photoUrl: StateFlow<String> = _photoUrl.asStateFlow()

    fun onNameChange(value: String) { _name.value = value }
    fun onBirthDateChange(value: String) { _birthDate.value = value }
    fun onPhoneChange(value: String) { _phone.value = value }
    fun onPhotoUrlChange(value: String) { _photoUrl.value = value }

    suspend fun saveUser() {
        val user = User(
            id = UUID.randomUUID().toString(),
            name = name.value,
            photoUrl = photoUrl.value,
            birthDate = birthDate.value,
            phone = phone.value
        )
        repository.saveUser(user)
    }
}
