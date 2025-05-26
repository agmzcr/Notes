package com.example.notesapp.feature.notes.domain.use_case

import com.example.notesapp.feature.notes.domain.NoteRepository
import com.example.notesapp.feature.notes.domain.model.Note
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class UpdateNoteUseCaseTest {

    private lateinit var mockRepository: NoteRepository

    private lateinit var updateNoteUseCase: UpdateNoteUseCase

    @Before
    fun setUp() {
        mockRepository = mockk()

        updateNoteUseCase = UpdateNoteUseCase(mockRepository)
    }

    @Test
    fun `invoke should call updateNote on repository with the given note`() = runTest {
        val noteToUpdate = Note(
            id = 1,
            title = "Updated Test Note Title",
            description = "This is the updated content."
        )

        coEvery { mockRepository.updateNote(noteToUpdate) } just runs

        updateNoteUseCase(noteToUpdate)

        coVerify(exactly = 1) { mockRepository.updateNote(noteToUpdate) }
    }

    @Test
    fun `invoke should handle repository throwing an exception when updating`() = runTest {
        val noteToUpdate =
            Note(id = 2, title = "Note causing error on update", description = "Content")
        val expectedException = IllegalStateException("Database error on update")

        coEvery { mockRepository.updateNote(noteToUpdate) } throws expectedException

        var actualException: Exception? = null

        try {
            updateNoteUseCase(noteToUpdate)
        } catch (e: Exception) {
            actualException = e
        }

        assertEquals(expectedException, actualException)

        coVerify(exactly = 1) { mockRepository.updateNote(noteToUpdate) }
    }
}