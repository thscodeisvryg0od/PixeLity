package com.example.pixellauncher.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pixellauncher.data.model.AppInfo
import com.example.pixellauncher.data.repository.AppRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

data class HomeUiState(
    val apps: List<AppInfo> = emptyList(),
    val favoriteApps: List<AppInfo> = emptyList(),
    val searchQuery: String = "",
    val searchResults: List<AppInfo> = emptyList(),
    val isLoading: Boolean = true
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: AppRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    private val _searchQuery = MutableStateFlow("")

    init {
        loadApps()
    }

    fun loadApps() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            combine(
                repository.getInstalledApps(),
                repository.getFavoritePackages(),
                _searchQuery
            ) { apps: List<AppInfo>, favorites: Set<String>, query: String ->
                val favoriteAppsList = apps.filter { app: AppInfo -> app.packageName in favorites }
                val searchList = if (query.isBlank()) {
                    emptyList()
                } else {
                    apps.filter { app: AppInfo ->
                        app.label.contains(query, ignoreCase = true) ||
                                app.packageName.contains(query, ignoreCase = true)
                    }
                }
                HomeUiState(
                    apps = apps,
                    favoriteApps = favoriteAppsList,
                    searchQuery = query,
                    searchResults = searchList,
                    isLoading = false
                )
            }.collect { state: HomeUiState ->
                _uiState.value = state
            }
        }
    }

    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
    }

    fun toggleFavorite(packageName: String) {
        viewModelScope.launch {
            repository.toggleFavorite(packageName)
        }
    }
}
