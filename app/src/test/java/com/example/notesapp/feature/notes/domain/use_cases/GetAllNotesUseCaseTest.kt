package com.example.notesapp.feature.notes.domain.use_cases

import com.example.notesapp.feature.notes.domain.NoteRepository
import com.example.notesapp.feature.notes.domain.model.Note
import com.example.notesapp.feature.notes.domain.use_case.GetAllNotesUseCase
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class GetAllNotesUseCaseTest {

    private lateinit var mockRepository: NoteRepository

    private lateinit var getAllNotesUseCase: GetAllNotesUseCase

    @Before
    fun setUp() {
        mockRepository = mockk()

        getAllNotesUseCase = GetAllNotesUseCase(mockRepository)
    }

    @Test
    fun `invoke should return flow of notes from repository`() = runTest {
        val fakeNotes = listOf(
            Note(id = 1, title = "Note 1", description = "Content 1"),
            Note(id = 2, title = "Note 2", description = "Content 2")
        )

        every { mockRepository.getAllNotes() } returns flowOf(fakeNotes)

        val notesFlow = getAllNotesUseCase() // Llama al operador invoke()

        val resultNotes = notesFlow.first()

        assertEquals(fakeNotes, resultNotes)

        verify(exactly = 1) { mockRepository.getAllNotes() }
    }

    @Test
    fun `invoke should return empty flow if repository returns empty flow`() = runTest {
        // ARRANGE
        val emptyNotesList = emptyList<Note>()
        every { mockRepository.getAllNotes() } returns flowOf(emptyNotesList)

        // ACT
        val notesFlow = getAllNotesUseCase()
        val resultNotes = notesFlow.first()

        // ASSERT
        assertTrue(resultNotes.isEmpty())
        assertEquals(emptyNotesList, resultNotes)
        verify(exactly = 1) { mockRepository.getAllNotes() }
    }
}