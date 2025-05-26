package com.example.notesapp.di

import android.content.Context
import androidx.room.Room
import com.example.notesapp.feature.notes.data.LocalDatabase
import com.example.notesapp.feature.notes.data.NoteRepositoryImpl
import com.example.notesapp.feature.notes.domain.NoteRepository
import com.example.notesapp.feature.notes.domain.use_case.DeleteNoteUseCase
import com.example.notesapp.feature.notes.domain.use_case.GetAllNotesUseCase
import com.example.notesapp.feature.notes.domain.use_case.GetNoteByIdUseCase
import com.example.notesapp.feature.notes.domain.use_case.InsertNoteUseCase
import com.example.notesapp.feature.notes.domain.use_case.UpdateNoteUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideLocalDatabase(@ApplicationContext context: Context) =
        Room.databaseBuilder(
            context,
            LocalDatabase::class.java,
            "local_database"
        ).fallbackToDestructiveMigration()
            .build()

    @Provides
    @Singleton
    fun provideNoteRepository(db: LocalDatabase): NoteRepository {
        return NoteRepositoryImpl(dao = db.noteDao())
    }

    @Module
    @InstallIn(ViewModelComponent::class)
    object UseCaseModule {

        @Provides
        fun provideGetAllNotesUseCase(repository: NoteRepository): GetAllNotesUseCase =
            GetAllNotesUseCase(repository)

        @Provides
        fun provideInsertNoteUseCase(repository: NoteRepository): InsertNoteUseCase =
            InsertNoteUseCase(repository)

        @Provides
        fun provideUpdateNoteUseCase(repository: NoteRepository): UpdateNoteUseCase =
            UpdateNoteUseCase(repository)

        @Provides
        fun provideDeleteNoteUseCase(repository: NoteRepository): DeleteNoteUseCase =
            DeleteNoteUseCase(repository)

        @Provides
        fun provideGetNoteByIdUseCase(repository: NoteRepository): GetNoteByIdUseCase =
            GetNoteByIdUseCase(repository)

    }
}