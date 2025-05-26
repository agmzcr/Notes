package com.example.notesapp.feature.notes.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.notesapp.feature.core.presentation.MainViewModel
import com.example.notesapp.feature.core.presentation.toastMessage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditNoteScreen(
    noteId: Int,
    navController: NavController,
    viewModel: MainViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val focusRequest = FocusRequester()
    val textStyle = TextStyle(
        fontSize = 18.sp
    )

    LaunchedEffect(true) {
        if (noteId > 0) {
            viewModel.getNoteById(noteId)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = null
                        )
                    }
                },
                title = {
                    Text(text = if (noteId > 0) "Edit Note" else "Add Note")
                },
                actions = {
                    IconButton(onClick = {
                        if (viewModel.note.title.isNotBlank()) {
                            viewModel.insertNote(viewModel.note)
                            navController.popBackStack()
                        } else {
                            toastMessage(context, "Title cant be empty")
                        }
                    }) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = null
                        )
                    }
                })
        }
    ) { contentPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding)
        ) {
            LaunchedEffect(key1 = true) {
                if (noteId == -1) {
                    focusRequest.requestFocus()
                }
            }

            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp)
                    .focusRequester(focusRequest),
                shape = RectangleShape,
                textStyle = textStyle,
                value = viewModel.note.title,
                onValueChange = {
                    viewModel.updateNoteTitle(it)
                },
                placeholder = {
                    Text(
                        text = "Add title...",
                        style = textStyle
                    )
                })
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(4.dp),
                shape = RectangleShape,
                textStyle = textStyle,
                value = viewModel.note.description ?: "",
                onValueChange = {
                    viewModel.updateNoteDescription(it)
                },
                placeholder = {
                    Text(
                        text = "Add description...",
                        style = textStyle
                    )
                }
            )
        }
    }
}