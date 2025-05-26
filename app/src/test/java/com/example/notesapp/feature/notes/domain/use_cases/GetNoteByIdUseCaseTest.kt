package com.example.notesapp.feature.notes.domain.use_case

import com.example.notesapp.feature.notes.domain.model.Note
import com.example.notesapp.feature.notes.domain.NoteRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class GetNoteByIdUseCaseTest {

    private lateinit var mockRepository: NoteRepository

    private lateinit var getNoteByIdUseCase: GetNoteByIdUseCase

    @Before
    fun setUp() {
        mockRepository = mockk()

        getNoteByIdUseCase = GetNoteByIdUseCase(mockRepository)
    }

    @Test
    fun `invoke should return note from repository when note exists`() = runTest {
        val testNoteId = 1
        val expectedNote = Note(
            id = testNoteId,
            title = "Fetched Note",
            description = "This is the content of the fetched note."
        )

        coEvery { mockRepository.getNoteById(testNoteId) } returns expectedNote

        val actualNote = getNoteByIdUseCase(testNoteId)

        assertEquals(expectedNote, actualNote)

        coVerify(exactly = 1) { mockRepository.getNoteById(testNoteId) }
    }

    @Test
    fun `invoke should throw exception from repository if note retrieval fails`() = runTest {
        val testNoteId = 99
        val expectedException = RuntimeException("Error fetching note with ID $testNoteId from database")

        coEvery { mockRepository.getNoteById(testNoteId) } throws expectedException

        var actualException: Exception? = null

        try {
            getNoteByIdUseCase(testNoteId)
        } catch (e: Exception) {
            actualException = e
        }

        assertEquals(expectedException, actualException)

        coVerify(exactly = 1) { mockRepository.getNoteById(testNoteId) }
    }
}