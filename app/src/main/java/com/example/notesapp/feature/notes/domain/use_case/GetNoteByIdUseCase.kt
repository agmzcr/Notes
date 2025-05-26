package com.example.notesapp.feature.notes.domain.use_case

import com.example.notesapp.feature.notes.domain.NoteRepository
import com.example.notesapp.feature.notes.domain.model.Note
import javax.inject.Inject

class GetNoteByIdUseCase @Inject constructor(
    private val repository: NoteRepository
) {
    suspend operator fun invoke(noteId: Int): Note {
        return repository.getNoteById(noteId)
    }
}