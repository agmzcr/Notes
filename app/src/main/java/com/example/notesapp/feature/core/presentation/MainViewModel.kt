package com.example.notesapp.feature.core.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.notesapp.feature.notes.domain.model.Note
import com.example.notesapp.feature.notes.domain.use_case.DeleteNoteUseCase
import com.example.notesapp.feature.notes.domain.use_case.GetAllNotesUseCase
import com.example.notesapp.feature.notes.domain.use_case.GetNoteByIdUseCase
import com.example.notesapp.feature.notes.domain.use_case.InsertNoteUseCase
import com.example.notesapp.feature.notes.domain.use_case.UpdateNoteUseCase
import com.example.notesapp.util.Response
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getAllNotesUseCase: GetAllNotesUseCase,
    private val insertNoteUseCase: InsertNoteUseCase,
    private val updateNoteUseCase: UpdateNoteUseCase,
    private val deleteNoteUseCase: DeleteNoteUseCase,
    private val getNoteByIdUseCase: GetNoteByIdUseCase,
) : ViewModel() {
    var note by mutableStateOf(
        Note(0, "", null)
    )

    private var deletedNote: Note? = null

    private var _response =
        MutableStateFlow<Response<List<Note>>>(Response.Loading)

    val response = _response.asStateFlow()

    init {
        getAllNotes()
    }

    private fun getAllNotes() = viewModelScope.launch {
        getAllNotesUseCase()
            .onStart {
                _response.value = Response.Loading
            }
            .catch {
                _response.value = Response.Error(it)
            }.collect {
                _response.value = Response.Success(it)
            }
    }

    fun insertNote(note: Note) {
        viewModelScope.launch {
            insertNoteUseCase(note = note)
        }
    }

    fun updateNote(note: Note) {
        viewModelScope.launch {
            updateNoteUseCase(note = note)
        }
    }

    fun deleteNote(note: Note) {
        viewModelScope.launch {
            deletedNote = note
            deleteNoteUseCase(note = note)
        }
    }

    fun undoDeleteNote() {
        viewModelScope.launch {
            deletedNote?.let { note ->
                insertNoteUseCase(note = note)
            }
        }
    }

    fun getNoteById(noteId: Int) {
        viewModelScope.launch {
            note = getNoteByIdUseCase(noteId = noteId)
        }
    }

    fun updateNoteTitle(newValue: String) {
        note = note.copy(
            title = newValue
        )
    }

    fun updateNoteDescription(newValue: String) {
        note = note.copy(
            description = newValue
        )
    }
}