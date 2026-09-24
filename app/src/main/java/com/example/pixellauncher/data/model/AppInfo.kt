package com.example.pixellauncher.data.model

import android.graphics.drawable.Drawable

/**
 * Lightweight representation of an installed launchable app.
 */
data class AppInfo(
    val packageName: String,
    val activityName: String,
    val label: String,
    val icon: Drawable?
) {
    val key: String get() = "$packageName/$activityName"
}
