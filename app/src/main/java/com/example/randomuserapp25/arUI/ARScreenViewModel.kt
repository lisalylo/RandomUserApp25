package com.example.randomuserapp25.arUI

import androidx.compose.ui.geometry.Offset
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.randomuserapp25.data.UserRepository
import com.example.randomuserapp25.domain.User
import com.google.mlkit.vision.barcode.common.Barcode
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel für den AR-Screen.
 * - Baut die ImageDatabase auf
 * - Erkennt getrackte Images und mapped sie auf User
 */
@HiltViewModel
class ArScreenViewModel @Inject constructor(
    private val repository: UserRepository
) : ViewModel() {
    private val _detected = MutableStateFlow<DetectedUser?>(null)
    val detected: StateFlow<DetectedUser?> = _detected.asStateFlow()

    private val disappearanceDelay = 1000L
    private var lastSeenTimestamp = 0L

    fun onBarcodes(barcodes: List<Barcode>) {
        val now = System.currentTimeMillis()
        val firstQr = barcodes.firstOrNull { it.rawValue != null && it.cornerPoints != null }
        if (firstQr != null) {
            lastSeenTimestamp = now
            val id = firstQr.rawValue!!
            viewModelScope.launch {
                val users = repository.getUsers().first()
                users.find { it.id == id }?.let { user ->
                    val corners = firstQr.cornerPoints!!
                        .map { Offset(it.x.toFloat(), it.y.toFloat()) }
                    _detected.value = DetectedUser(user, corners)
                }
            }
        } else {
            if (now - lastSeenTimestamp > disappearanceDelay) {
                _detected.value = null
            }
        }
    }
}


data class DetectedUser(val user: User, val corners: List<Offset>?)
