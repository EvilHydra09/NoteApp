package com.example.countryapp.domain.use_case

import com.example.countryapp.common.Resource
import com.example.countryapp.domain.respository.NoteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class DeleteNoteByIdUseCase(private val noteRepository : NoteRepository) {
    operator fun invoke(id: Int) : Flow<Resource<Unit>> = flow {
        emit(Resource.OnLoading())
        try {
            emit(Resource.Success(noteRepository.deleteNoteById(id)))
        } catch (e: Exception) {
            emit(Resource.Error(e.message.toString()))
        }
    }
}