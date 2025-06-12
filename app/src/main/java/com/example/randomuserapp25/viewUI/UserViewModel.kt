package com.example.randomuserapp25.viewUI

import android.util.Log
import com.example.randomuserapp25.data.SortField
import com.example.randomuserapp25.data.UserRepository
import com.example.randomuserapp25.domain.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

/**
 * 1. Verwaltung Sortieroptionen
 * 2. Bereitstellung Liste von usern als Flow für ui
 * 3. Laden/Aktualisieren User data von Web-API (repo)
 * 4. Funktionen für Settings-Optionen (Leeren, Füllen DB)
 */
@HiltViewModel
class UserListViewModel @Inject constructor(
    private val repository: UserRepository
) : ViewModel() {
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _sortField = MutableStateFlow(SortField.NAME)

    private val _sortAscending = MutableStateFlow(true)
    val sortAscending: StateFlow<Boolean> = _sortAscending.asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    val users: StateFlow<List<User>> = combine(
        _sortField,
        _sortAscending
    ) { field, asc ->
        field to asc
    }
        .flatMapLatest { (field, asc) ->
            repository.getUsersSortedBy(field, ascending = asc)
        }
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    init {
        refreshUsers()
    }

    fun setSortField(field: SortField) {
        _sortField.value = field
    }

    fun toggleSortOrder() {
        _sortAscending.value = !_sortAscending.value
    }

    fun refreshUsers(count: Int = 10) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                repository.refreshUsersFromRemote(count)
            } catch (t: Throwable) {
                Log.e("UserListVm", "refresh failed", t)
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun clearDatabase() {
        viewModelScope.launch {
            repository.deleteAllUsers()
        }
    }

    fun fillDatabase(count: Int = 10) {
        viewModelScope.launch {
            val fetched = repository.fetchRemoteUsers(count)
            repository.saveUsers(fetched)
        }
    }
}
