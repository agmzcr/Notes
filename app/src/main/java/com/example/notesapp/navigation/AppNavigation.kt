package com.example.notesapp.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.example.notesapp.navigation.nav_graph.notes

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    Scaffold { contentPadding ->
        NavHost(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding),
            navController = navController,
            startDestination = "home_screen"
        ) {
            notes(navController = navController)
        }
    }
}