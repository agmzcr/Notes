package com.example.notesapp.feature.notes.presentation

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.StickyNote2
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.notesapp.feature.core.presentation.EmptyListScreen
import com.example.notesapp.feature.core.presentation.MainViewModel
import com.example.notesapp.feature.core.presentation.undoDeleteNote
import com.example.notesapp.feature.notes.presentation.components.LoadingAndErrorScreen
import com.example.notesapp.feature.notes.presentation.components.NoteList
import com.example.notesapp.util.Response

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteScreen(
    onAddNoteClick: (Int) -> Unit,
    onEditNoteClick: (Int) -> Unit,
    viewModel: MainViewModel = hiltViewModel()
) {
    val scope = rememberCoroutineScope()

    val snackbarHostState = remember { SnackbarHostState() }

    val response by viewModel.response.collectAsStateWithLifecycle()

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = {}) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.StickyNote2,
                            contentDescription = null
                        )
                    }
                },
                title = {
                    Text(text = "Notes")
                })
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { onAddNoteClick(-1) }) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null
                )
            }
        }
    ) { contentPadding ->
        AnimatedContent(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding),
            targetState = response,
            label = "animated content",
            transitionSpec = {
                fadeIn(
                    animationSpec = tween(
                        300,
                        easing = LinearEasing
                    )
                ) togetherWith fadeOut(
                    animationSpec = tween(
                        300,
                        easing = LinearEasing
                    )
                )
            }
        ) { result ->
            when (result) {
                is Response.Loading -> {
                    LoadingAndErrorScreen(label = "Loading...")
                }

                is Response.Success -> {
                    val notes = result.data
                    if (notes.isEmpty()) {
                        EmptyListScreen()
                    } else {
                        NoteList(
                            notes = notes,
                            onEditNoteClick = onEditNoteClick,
                            onUndoDeleteClick = {
                                undoDeleteNote(
                                    scope = scope,
                                    snackbarHostState = snackbarHostState,
                                    viewModel = viewModel
                                )
                            }
                        )
                    }
                }

                is Response.Error -> {
                    val message = result.error.message ?: "Something went wrong!"

                    LoadingAndErrorScreen(label = message)
                }

                else -> Unit
            }
        }
    }
}