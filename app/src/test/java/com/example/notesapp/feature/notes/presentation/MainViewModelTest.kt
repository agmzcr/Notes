package com.example.notesapp.feature.notes.presentation

import com.example.notesapp.feature.core.presentation.MainViewModel
import com.example.notesapp.feature.notes.domain.model.Note
import com.example.notesapp.feature.notes.domain.use_case.DeleteNoteUseCase
import com.example.notesapp.feature.notes.domain.use_case.GetAllNotesUseCase
import com.example.notesapp.feature.notes.domain.use_case.GetNoteByIdUseCase
import com.example.notesapp.feature.notes.domain.use_case.InsertNoteUseCase
import com.example.notesapp.feature.notes.domain.use_case.UpdateNoteUseCase
import com.example.notesapp.util.Response
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.unmockkAll
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class MainViewModelTest {

    private lateinit var getAllNotesUseCase: GetAllNotesUseCase
    private lateinit var insertNoteUseCase: InsertNoteUseCase
    private lateinit var updateNoteUseCase: UpdateNoteUseCase
    private lateinit var deleteNoteUseCase: DeleteNoteUseCase
    private lateinit var getNoteByIdUseCase: GetNoteByIdUseCase

    private lateinit var viewModel: MainViewModel
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        getAllNotesUseCase = mockk()
        insertNoteUseCase = mockk(relaxed = true)
        updateNoteUseCase = mockk(relaxed = true)
        deleteNoteUseCase = mockk(relaxed = true)
        getNoteByIdUseCase = mockk()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        unmockkAll()
    }

    @Test
    fun `init calls getAllNotes and updates response to Success`() = runTest(testDispatcher) {
        // ARRANGE
        val notes = listOf(Note(1, "Test Note", "Description"))
        coEvery { getAllNotesUseCase() } returns flowOf(notes)

        // ACT
        viewModel = MainViewModel(
            getAllNotesUseCase,
            insertNoteUseCase,
            updateNoteUseCase,
            deleteNoteUseCase,
            getNoteByIdUseCase
        )
        advanceUntilIdle()

        // ASSERT
        val result = viewModel.response.value
        assertTrue(result is Response.Success)
        assertEquals(notes, (result as Response.Success).data)
    }

    @Test
    fun `insertNote calls insertNoteUseCase`() = runTest(testDispatcher) {
        // ARRANGE
        val noteToInsert = Note(1, "Titulo", "Contenido")
        coEvery { getAllNotesUseCase() } returns flowOf(emptyList())

        viewModel = MainViewModel(
            getAllNotesUseCase,
            insertNoteUseCase,
            updateNoteUseCase,
            deleteNoteUseCase,
            getNoteByIdUseCase
        )
        advanceUntilIdle()

        // ACT
        viewModel.insertNote(noteToInsert)
        advanceUntilIdle()

        // ASSERT
        coVerify(exactly = 1) { insertNoteUseCase(noteToInsert) }
    }

    @Test
    fun `updateNote calls updateNoteUseCase`() = runTest(testDispatcher) {
        // ARRANGE
        val noteToUpdate = Note(1, "Test", "Content")
        coEvery { getAllNotesUseCase() } returns flowOf(emptyList())

        viewModel = MainViewModel(
            getAllNotesUseCase,
            insertNoteUseCase,
            updateNoteUseCase,
            deleteNoteUseCase,
            getNoteByIdUseCase
        )
        advanceUntilIdle()

        // ACT
        viewModel.updateNote(noteToUpdate)
        advanceUntilIdle()

        // ASSERT
        coVerify(exactly = 1) { updateNoteUseCase(noteToUpdate) }
    }

    @Test
    fun `deleteNote sets internal deletedNote and calls deleteNoteUseCase`() =
        runTest(testDispatcher) {
            // ARRANGE
            val noteToDelete = Note(1, "To Delete", "Desc")
            coEvery { getAllNotesUseCase() } returns flowOf(emptyList())

            viewModel = MainViewModel(
                getAllNotesUseCase,
                insertNoteUseCase,
                updateNoteUseCase,
                deleteNoteUseCase,
                getNoteByIdUseCase
            )
            advanceUntilIdle()

            // ACT
            viewModel.deleteNote(noteToDelete)
            advanceUntilIdle() // Para la corutina de deleteNote

            // ASSERT
            coVerify(exactly = 1) { deleteNoteUseCase(noteToDelete) }
        }

    @Test
    fun `undoDeleteNote calls insertNoteUseCase with the previously deleted note`() =
        runTest(testDispatcher) {
            // ARRANGE
            val noteToOperate = Note(1, "Undo", "Desc")
            coEvery { getAllNotesUseCase() } returns flowOf(emptyList())

            viewModel = MainViewModel(
                getAllNotesUseCase,
                insertNoteUseCase,
                updateNoteUseCase,
                deleteNoteUseCase,
                getNoteByIdUseCase
            )
            advanceUntilIdle() // Para init

            // ACT
            viewModel.deleteNote(noteToOperate)
            advanceUntilIdle()

            viewModel.undoDeleteNote()
            advanceUntilIdle()

            // ASSERT
            coVerify(exactly = 1) { deleteNoteUseCase(noteToOperate) }
            coVerify(exactly = 1) { insertNoteUseCase(noteToOperate) }
        }

    @Test
    fun `getNoteById updates note state`() = runTest(testDispatcher) {
        // ARRANGE
        val noteIdToFetch = 5
        val expectedNote = Note(noteIdToFetch, "Some Note", "Test")
        coEvery { getAllNotesUseCase() } returns flowOf(emptyList())
        coEvery { getNoteByIdUseCase(noteIdToFetch) } returns expectedNote

        viewModel = MainViewModel(
            getAllNotesUseCase,
            insertNoteUseCase,
            updateNoteUseCase,
            deleteNoteUseCase,
            getNoteByIdUseCase
        )
        advanceUntilIdle()

        // ACT
        viewModel.getNoteById(noteIdToFetch)
        advanceUntilIdle()

        // ASSERT
        assertEquals(expectedNote, viewModel.note)
    }

    @Test
    fun `updateNoteTitle changes note title state`() = runTest(testDispatcher) {
        // ARRANGE
        val initialTitle = "Old Title"
        val initialDescription = "Old Desc"
        val noteId = 1
        val newTitle = "New Title"

        coEvery { getAllNotesUseCase() } returns flowOf(emptyList())
        val initialNote = Note(id = noteId, title = initialTitle, description = initialDescription)
        coEvery { getNoteByIdUseCase(noteId) } returns initialNote

        viewModel = MainViewModel(
            getAllNotesUseCase,
            insertNoteUseCase,
            updateNoteUseCase,
            deleteNoteUseCase,
            getNoteByIdUseCase
        )
        advanceUntilIdle()

        viewModel.getNoteById(noteId)
        advanceUntilIdle()

        assertEquals(initialNote, viewModel.note)

        // ACT
        viewModel.updateNoteTitle(newTitle)

        // ASSERT
        assertEquals(newTitle, viewModel.note.title)
        assertEquals(initialDescription, viewModel.note.description)
        assertEquals(noteId, viewModel.note.id)
    }

    @Test
    fun `updateNoteDescription changes note description state`() = runTest(testDispatcher) {
        // ARRANGE
        val initialTitle = "Old Title"
        val initialDescription = "Old Desc"
        val noteId = 1
        val newDescription = "New Desc"

        coEvery { getAllNotesUseCase() } returns flowOf(emptyList())
        val initialNote = Note(id = noteId, title = initialTitle, description = initialDescription)
        coEvery { getNoteByIdUseCase(noteId) } returns initialNote

        viewModel = MainViewModel(
            getAllNotesUseCase,
            insertNoteUseCase,
            updateNoteUseCase,
            deleteNoteUseCase,
            getNoteByIdUseCase
        )
        advanceUntilIdle()

        viewModel.getNoteById(noteId)
        advanceUntilIdle()

        assertEquals(initialNote, viewModel.note)

        // ACT
        viewModel.updateNoteDescription(newDescription)

        // ASSERT
        assertEquals(newDescription, viewModel.note.description)
        assertEquals(initialTitle, viewModel.note.title)
        assertEquals(noteId, viewModel.note.id)
    }
}