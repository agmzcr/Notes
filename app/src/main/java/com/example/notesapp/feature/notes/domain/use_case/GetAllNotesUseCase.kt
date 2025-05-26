package com.example.notesapp.feature.notes.domain.use_case

import com.example.notesapp.feature.notes.domain.NoteRepository
import com.example.notesapp.feature.notes.domain.model.Note
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllNotesUseCase @Inject constructor(
    private val repository: NoteRepository
) {
    operator fun invoke(): Flow<List<Note>> {
        return repository.getAllNotes()
    }
}