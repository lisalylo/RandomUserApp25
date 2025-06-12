package com.example.randomuserapp25.viewUI

import android.graphics.Bitmap
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.randomuserapp25.arData.QrCodeGenerator
import com.example.randomuserapp25.data.UserRepository
import com.example.randomuserapp25.domain.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserDetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val repository: UserRepository,
    private val qrGenerator: QrCodeGenerator
) : ViewModel() {

    private val userId: String = checkNotNull(savedStateHandle["userId"])

    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> = _user.asStateFlow()

    private val _qrBitmap = MutableStateFlow<Bitmap?>(null)
    val qrBitmap: StateFlow<Bitmap?> = _qrBitmap.asStateFlow()

    /**
     * 1. liste aller user holen
     * 2. gesuchten user finden
     * 3. werte setzen
     */
    init {
        viewModelScope.launch {
            val list: List<User> = repository.getUsers().first()
            val found: User? = list.firstOrNull { it.id == userId }
            found?.let { u ->
                _user.value = u
                _qrBitmap.value = qrGenerator.generate(u.id, size = 256)
            }
        }
    }
}
