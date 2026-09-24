package com.example.pixellauncher.ui.home

import android.content.Context
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
    val filteredApps: List<AppInfo> = emptyList(),
    val dockApps: List<AppInfo> = emptyList(),
    val searchQuery: String = "",
    val isDrawerOpen: Boolean = false,
    val isLoading: Boolean = true
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: AppRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    private val _isDrawerOpen = MutableStateFlow(false)

    init {
        loadApps()
    }

    fun loadApps() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            combine(
                repository.loadLaunchableApps(),
                _searchQuery,
                _isDrawerOpen
            ) { apps, query, isDrawerOpen ->
                val filtered = if (query.isBlank()) {
                    apps
                } else {
                    apps.filter {
                        it.label.contains(query, ignoreCase = true) ||
                                it.packageName.contains(query, ignoreCase = true)
                    }
                }
                val dock = apps.take(5) // Varsayılan dock uygulamaları
                HomeUiState(
                    apps = apps,
                    filteredApps = filtered,
                    dockApps = dock,
                    searchQuery = query,
                    isDrawerOpen = isDrawerOpen,
                    isLoading = false
                )
            }.collect { state ->
                _uiState.value = state
            }
        }
    }

    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
    }

    fun openDrawer() {
        _isDrawerOpen.value = true
    }

    fun closeDrawer() {
        _isDrawerOpen.value = false
        _searchQuery.value = ""
    }

    fun launchApp(context: Context, packageName: String) {
        repository.launchApp(context, packageName)
    }

    fun addToDock(app: AppInfo) {
        // Dock yönetimi ekleme mantığı
    }

    fun hideApp(packageName: String) {
        // Uygulama gizleme mantığı
    }

    fun openAppInfo(context: Context, packageName: String) {
        repository.openAppInfo(context, packageName)
    }

    fun uninstallApp(context: Context, packageName: String) {
        repository.uninstallApp(context, packageName)
    }
}
