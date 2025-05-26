package com.example.notesapp.navigation.nav_graph

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.navArgument
import com.example.notesapp.feature.notes.presentation.AddEditNoteScreen
import com.example.notesapp.feature.notes.presentation.NoteScreen
import com.example.notesapp.navigation.Screen

fun NavGraphBuilder.notes(navController: NavController) {
    navigation(
        startDestination = Screen.NoteScreen.route,
        route = "home_screen"
    ) {
        composable(
            route = Screen.NoteScreen.route
        ) {
            NoteScreen(
                onAddNoteClick = { noteId ->
                    navController.navigate(
                        route = "${Screen.AddEditNoteScreen.route}/$noteId"
                    )
                },
                onEditNoteClick = { noteId ->
                    navController.navigate(
                        route = "${Screen.AddEditNoteScreen.route}/$noteId"
                    )
                }
            )
        }
        composable(
            route = "${Screen.AddEditNoteScreen.route}/{noteId}",
            arguments = listOf(navArgument("noteId") { type = NavType.IntType }),
            enterTransition = {
                slideInHorizontally(
                    initialOffsetX = { -it },
                    animationSpec = tween(
                        300,
                        easing = FastOutSlowInEasing
                    )
                )
            },
            exitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { -it },
                    animationSpec = tween(
                        300,
                        easing = FastOutSlowInEasing
                    )
                )
            }
        ) { entry ->
            entry.arguments?.getInt("noteId").let { noteId ->
                AddEditNoteScreen(
                    noteId = noteId!!,
                    navController = navController
                )
            }

        }
    }
}