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
class DeleteNoteUseCaseTest {

    // 1. Declarar el mock para la dependencia (NoteRepository)
    private lateinit var mockRepository: NoteRepository

    // 2. Declarar la instancia de la clase que estamos probando
    private lateinit var deleteNoteUseCase: DeleteNoteUseCase

    @Before
    fun setUp() {
        // Crear el mock para NoteRepository
        mockRepository = mockk()

        // Crear una instancia real del UseCase, inyectando el mock
        deleteNoteUseCase = DeleteNoteUseCase(mockRepository)
    }

    @Test
    fun `invoke should call deleteNote on repository with the given note`() = runTest {
        // ARRANGE (Organizar)
        // a. Crear un dato de prueba (la nota a borrar)
        val noteToDelete = Note(
            id = 1, // El ID es importante para la eliminación
            title = "Test Note to Delete",
            description = "This note will be deleted."
        )

        // b. Definir el comportamiento esperado del mock para la función suspend:
        //    Cuando se llame a mockRepository.deleteNote(noteToDelete), simplemente debe ejecutarse.
        coEvery { mockRepository.deleteNote(noteToDelete) } just runs

        // ACT (Actuar)
        // Ejecutar el método que estamos probando (el operador invoke).
        deleteNoteUseCase(noteToDelete)

        // ASSERT (Afirmar)
        // Verificar que el método esperado del repositorio (deleteNote) fue llamado
        // exactamente una vez y con el objeto `noteToDelete` correcto.
        coVerify(exactly = 1) { mockRepository.deleteNote(noteToDelete) }
    }

    @Test
    fun `invoke should handle repository throwing an exception when deleting`() = runTest {
        // ARRANGE
        val noteToDelete =
            Note(id = 2, title = "Note causing error on delete", description = "Content")
        val expectedException =
            RuntimeException("Database error on delete") // Usar una excepción más específica si es posible

        // Configurar el mock para que lance una excepción cuando se llame a deleteNote
        coEvery { mockRepository.deleteNote(noteToDelete) } throws expectedException

        var actualException: Exception? = null

        // ACT
        try {
            deleteNoteUseCase(noteToDelete)
        } catch (e: Exception) {
            actualException = e
        }

        // ASSERT
        // Verificar que la excepción lanzada por el repositorio fue la que se propagó
        assertEquals(expectedException, actualException) // Compara la instancia de la excepción
        // o assertEquals(expectedException.message, actualException?.message) si solo comparas mensajes

        // Verificar que el método del repositorio fue llamado
        coVerify(exactly = 1) { mockRepository.deleteNote(noteToDelete) }
    }
}