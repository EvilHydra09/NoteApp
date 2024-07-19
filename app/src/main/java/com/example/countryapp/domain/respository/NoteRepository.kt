package com.example.countryapp.domain.respository

import com.example.countryapp.domain.model.Note

interface NoteRepository {

    suspend fun getNotes(): List<Note>
    suspend fun addNote(note: Note): Note

    suspend fun updateNote(note: Note): Note
    suspend fun deleteNoteById(id: Int)


}