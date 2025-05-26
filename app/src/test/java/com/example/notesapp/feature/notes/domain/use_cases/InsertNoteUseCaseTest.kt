package com.example.notesapp.feature.notes.domain.use_cases

import com.example.notesapp.feature.notes.domain.NoteRepository
import com.example.notesapp.feature.notes.domain.model.Note
import com.example.notesapp.feature.notes.domain.use_case.InsertNoteUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class InsertNoteUseCaseTest {

    private lateinit var mockRepository: NoteRepository

    private lateinit var insertNoteUseCase: InsertNoteUseCase

    @Before
    fun setUp() {
        mockRepository = mockk()

        insertNoteUseCase = InsertNoteUseCase(mockRepository)
    }

    @Test
    fun `invoke should call insertNote on repository with the given note`() = runTest {

        val noteToInsert = Note(
            id = 1,
            title = "Test Note to Insert",
            description = "This is the content of the test note.",
        )

        coEvery { mockRepository.insertNote(noteToInsert) } just runs // o `just Runs`

        insertNoteUseCase(noteToInsert)

        coVerify(exactly = 1) { mockRepository.insertNote(noteToInsert) }
    }

    @Test
    fun `invoke should handle repository throwing an exception`() = runTest {

        val noteToInsert = Note(id = 2, title = "Note causing error", description = "Content")
        val expectedException = Exception("Database error on insert")

        coEvery { mockRepository.insertNote(noteToInsert) } throws expectedException

        var actualException: Exception? = null

        try {
            insertNoteUseCase(noteToInsert)
        } catch (e: Exception) {
            actualException = e
        }

        org.junit.Assert.assertEquals(expectedException.message, actualException?.message)

        coVerify(exactly = 1) { mockRepository.insertNote(noteToInsert) }
    }
}