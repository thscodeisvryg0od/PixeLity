package com.example.pixellauncher

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * Application entry point.
 *
 * Annotated with @HiltAndroidApp so Hilt can generate the base application
 * component and inject dependencies across the app (ViewModels, repositories, etc.).
 */
@HiltAndroidApp
class LauncherApp : Application()
