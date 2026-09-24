package com.example.pixellauncher.ui.home

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Apps
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.pixellauncher.R
import com.example.pixellauncher.ui.components.AppIcon
import com.example.pixellauncher.ui.components.ClockWidget
import com.example.pixellauncher.ui.drawer.AppDrawer

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    // Close drawer on back press
    BackHandler(enabled = state.isDrawerOpen) {
        viewModel.closeDrawer()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Main home content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
                .pointerInput(Unit) {
                    detectVerticalDragGestures { _, dragAmount ->
                        // Swipe up to open drawer
                        if (dragAmount < -40 && !state.isDrawerOpen) {
                            viewModel.openDrawer()
                        }
                    }
                },
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(48.dp))

            // Clock & date – Pixel-like top area
            ClockWidget()

            Spacer(modifier = Modifier.weight(1f))

            // Dock
            if (state.dockApps.isNotEmpty()) {
                Surface(
                    modifier = Modifier
                        .padding(horizontal = 20.dp, vertical = 12.dp)
                        .clip(RoundedCornerShape(28.dp)),
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.55f),
                    tonalElevation = 1.dp
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp, vertical = 14.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        state.dockApps.forEach { app ->
                            AppIcon(
                                app = app,
                                onClick = { viewModel.launchApp(app) },
                                iconSize = 52.dp,
                                showLabel = false,
                                onRemoveFromDock = { viewModel.removeFromDock(app) },
                                onAppInfo = { viewModel.openAppInfo(app) },
                                onUninstall = { viewModel.uninstallApp(app) }
                            )
                        }
                    }
                }
            }

            // Subtle hint when dock is empty
            if (state.dockApps.isEmpty() && !state.isLoading) {
                Text(
                    text = "Long-press apps in the drawer to pin them here",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                    modifier = Modifier.padding(bottom = 24.dp)
                )
            }

            // Open drawer FAB
            FloatingActionButton(
                onClick = { viewModel.openDrawer() },
                modifier = Modifier.padding(bottom = 20.dp),
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer
            ) {
                Icon(
                    imageVector = Icons.Default.Apps,
                    contentDescription = stringResource(R.string.drawer_search_hint)
                )
            }
        }

        // App drawer overlay
        AppDrawer(
            visible = state.isDrawerOpen,
            apps = state.filteredApps,
            searchQuery = state.searchQuery,
            onSearchQueryChange = viewModel::onSearchQueryChange,
            onAppClick = viewModel::launchApp,
            onAddToDock = viewModel::addToDock,
            onHide = viewModel::hideApp,
            onAppInfo = viewModel::openAppInfo,
            onUninstall = viewModel::uninstallApp,
            onDismiss = viewModel::closeDrawer
        )
    }
}
