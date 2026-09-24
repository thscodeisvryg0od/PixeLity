package com.example.pixellauncher

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.pixellauncher.ui.home.HomeScreen
import com.example.pixellauncher.ui.theme.LauncherTheme
import dagger.hilt.android.AndroidEntryPoint

/**
 * Single-activity host for the entire launcher UI.
 * Declared as the HOME activity in AndroidManifest.xml.
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        setContent {
            LauncherTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    HomeScreen()
                }
            }
        }
    }

    /**
     * Prevent leaving the launcher when the user presses Back on the home screen.
     * (When the drawer is open, HomeScreen already consumes the back press.)
     */
    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        // Intentionally do nothing – stay on home
    }
}
