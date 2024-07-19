package com.example.countryapp.domain.di

import com.example.countryapp.domain.respository.NoteRepository
import com.example.countryapp.domain.use_case.CreateNoteUseCase
import com.example.countryapp.domain.use_case.DeleteNoteByIdUseCase
import com.example.countryapp.domain.use_case.GetNoteListUseCase
import com.example.countryapp.domain.use_case.UpdateNoteUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
object DomainModule {

    @Provides
    fun provideGetNoteListUseCase(noteRepository: NoteRepository): GetNoteListUseCase {
        return GetNoteListUseCase(noteRepository)
    }

    @Provides
    fun provideCreateNoteUseCase(noteRepository: NoteRepository): CreateNoteUseCase {
        return CreateNoteUseCase(noteRepository)
    }

    @Provides
    fun provideDeleteNoteByIdUseCase(noteRepository: NoteRepository): DeleteNoteByIdUseCase {
        return DeleteNoteByIdUseCase(noteRepository)
    }
    @Provides
    fun provideUpdateNoteUseCase(noteRepository: NoteRepository): UpdateNoteUseCase {
        return UpdateNoteUseCase(noteRepository)
    }

}