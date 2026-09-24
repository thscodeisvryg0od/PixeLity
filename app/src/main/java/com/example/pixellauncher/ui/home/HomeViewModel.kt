package com.example.pixellauncher.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pixellauncher.data.model.AppInfo
import com.example.pixellauncher.data.preferences.LauncherPreferences
import com.example.pixellauncher.data.repository.AppRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

data class HomeUiState(
    val allApps: List<AppInfo> = emptyList(),
    val dockApps: List<AppInfo> = emptyList(),
    val filteredApps: List<AppInfo> = emptyList(),
    val searchQuery: String = "",
    val isDrawerOpen: Boolean = false,
    val isLoading: Boolean = true
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val appRepository: AppRepository,
    private val preferences: LauncherPreferences
) : ViewModel() {

    private val _allApps = MutableStateFlow<List<AppInfo>>(emptyList())
    private val _searchQuery = MutableStateFlow("")
    private val _isDrawerOpen = MutableStateFlow(false)
    private val _isLoading = MutableStateFlow(true)

    val uiState: StateFlow<HomeUiState> = combine(
        _allApps,
        preferences.dockApps,
        preferences.hiddenApps,
        _searchQuery,
        _isDrawerOpen,
        _isLoading
    ) { apps, dockKeys, hiddenKeys, query, drawerOpen, loading ->
        val visible = apps.filter { it.key !in hiddenKeys }
        val dock = dockKeys.mapNotNull { key -> visible.find { it.key == key } }
        val filtered = if (query.isBlank()) {
            visible
        } else {
            visible.filter { it.label.contains(query, ignoreCase = true) }
        }

        HomeUiState(
            allApps = visible,
            dockApps = dock,
            filteredApps = filtered,
            searchQuery = query,
            isDrawerOpen = drawerOpen,
            isLoading = loading
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = HomeUiState()
    )

    init {
        refreshApps()
    }

    fun refreshApps() {
        viewModelScope.launch {
            _isLoading.value = true
            val apps = appRepository.loadLaunchableApps()
            _allApps.value = apps

            // Seed a sensible default dock only once (when empty)
            val currentDock = preferences.dockApps.first()
            if (currentDock.isEmpty() && apps.isNotEmpty()) {
                val defaults = apps
                    .filter { it.packageName in defaultDockPackages }
                    .take(5)
                    .map { it.key }
                    .toSet()
                if (defaults.isNotEmpty()) {
                    preferences.setDockApps(defaults)
                }
            }
            _isLoading.value = false
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

    fun launchApp(app: AppInfo) {
        appRepository.launchApp(app)
        closeDrawer()
    }

    fun addToDock(app: AppInfo) {
        viewModelScope.launch { preferences.addToDock(app.key) }
    }

    fun removeFromDock(app: AppInfo) {
        viewModelScope.launch { preferences.removeFromDock(app.key) }
    }

    fun hideApp(app: AppInfo) {
        viewModelScope.launch { preferences.hideApp(app.key) }
    }

    fun openAppInfo(app: AppInfo) {
        appRepository.openAppInfo(app.packageName)
    }

    fun uninstallApp(app: AppInfo) {
        appRepository.uninstallApp(app.packageName)
    }

    companion object {
        private val defaultDockPackages = setOf(
            "com.android.chrome",
            "com.google.android.gm",
            "com.google.android.apps.maps",
            "com.google.android.youtube",
            "com.android.vending",
            "com.whatsapp",
            "com.android.settings",
            "com.google.android.dialer",
            "com.google.android.apps.messaging",
            "com.android.camera2",
            "com.google.android.GoogleCamera"
        )
    }
}
