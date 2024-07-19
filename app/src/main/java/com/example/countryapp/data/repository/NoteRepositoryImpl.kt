package com.example.countryapp.data.repository

import com.example.countryapp.common.toNote
import com.example.countryapp.common.toNoteDTOItem
import com.example.countryapp.data.remote.NoteApiService
import com.example.countryapp.domain.model.Note
import com.example.countryapp.domain.respository.NoteRepository

class NoteRepositoryImpl(private val noteApiService: NoteApiService): NoteRepository {
    override suspend fun getNotes(): List<Note> {
        try {
            return noteApiService.getNotes().map { it.toNote() }
        } catch (e: Exception) {
            throw Exception(e)
        }
    }

    override suspend fun addNote(note: Note): Note {
        try {
            return noteApiService.addNote(note.toNoteDTOItem()).toNote()
        } catch (e: Exception) {
           throw Exception(e)
        }
    }

    override suspend fun updateNote(note: Note): Note {
        try {
            return noteApiService.updateNote(note.id, note.toNoteDTOItem()).toNote()
        } catch (e: Exception) {
            throw Exception(e)
        }
    }

    override suspend fun deleteNoteById(id: Int) {
        try {
            return noteApiService.deleteNoteById(id)
        } catch (e: Exception) {
           throw Exception(e)
        }
    }
}