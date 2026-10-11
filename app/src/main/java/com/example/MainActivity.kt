package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.screens.MainDashboard
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.theme.VirtuLabTheme
import com.example.viewmodel.SettingsViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val settingsViewModel: SettingsViewModel = viewModel()
            val userSettings by settingsViewModel.settings.collectAsState()

            MyApplicationTheme(themeMode = userSettings.themeMode) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = VirtuLabTheme.colors.background
                ) {
                    MainDashboard(settingsViewModel = settingsViewModel)
                }
            }
        }
    }
}
