package com.example.countryapp.domain.use_case

import com.example.countryapp.common.Resource
import com.example.countryapp.domain.model.Note
import com.example.countryapp.domain.respository.NoteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class UpdateNoteUseCase(private val repository: NoteRepository) {
    operator fun invoke (note: Note): Flow<Resource<Note>> = flow {
        emit(Resource.OnLoading())
        try {
            emit(Resource.Success(repository.updateNote(note)))
        } catch (e: Exception) {
            emit(Resource.Error(e.message.toString()))
        }

    }
}