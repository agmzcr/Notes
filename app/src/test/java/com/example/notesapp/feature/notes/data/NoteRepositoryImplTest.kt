package com.example.notesapp.feature.notes.data

import com.example.notesapp.feature.notes.domain.model.Note
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class NoteRepositoryImplTest {

    private lateinit var dao: NoteDao
    private lateinit var repository: NoteRepositoryImpl

    @Before
    fun setUp() {
        dao = mockk()
        repository = NoteRepositoryImpl(dao)
    }

    @Test
    fun `insertNote calls dao insertNote`() = runTest {
        val note = Note(1, "Test", "Desc")
        coEvery { dao.insertNote(note) } just Runs

        repository.insertNote(note)

        coVerify { dao.insertNote(note) }
    }

    @Test
    fun `updateNote calls dao updateNote`() = runTest {
        val note = Note(1, "Updated", "Updated desc")
        coEvery { dao.updateNote(note) } just Runs

        repository.updateNote(note)

        coVerify { dao.updateNote(note) }
    }

    @Test
    fun `deleteNote calls dao deleteNote`() = runTest {
        val note = Note(1, "To Delete", "Desc")
        coEvery { dao.deleteNote(note) } just Runs

        repository.deleteNote(note)

        coVerify { dao.deleteNote(note) }
    }

    @Test
    fun `getNoteById returns expected note`() = runTest {
        val note = Note(1, "Title", "Desc")
        coEvery { dao.getNoteById(1) } returns note

        val result = repository.getNoteById(1)

        assertEquals(note, result)
        coVerify { dao.getNoteById(1) }
    }

    @Test
    fun `getAllNotes returns flow of notes`() = runTest {
        val notes = listOf(Note(1, "Title", "Desc"))
        coEvery { dao.getAllNotes() } returns flowOf(notes)

        val result = repository.getAllNotes().first()

        assertEquals(notes, result)
        coVerify { dao.getAllNotes() }
    }
}