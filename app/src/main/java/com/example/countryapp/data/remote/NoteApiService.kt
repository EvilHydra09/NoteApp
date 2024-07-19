package com.example.countryapp.data.remote

import com.example.countryapp.data.model.NoteDTO
import com.example.countryapp.data.model.NoteDTOItem
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface NoteApiService {

    // http://localhost:8080/api/notes
    @GET("api/notes")
    suspend fun getNotes(): NoteDTO

    @POST("api/notes")
    suspend fun addNote(@Body note: NoteDTOItem): NoteDTOItem

    @PUT("api/notes/{id}")
    suspend fun updateNote(@Path("id") id: Int, @Body note: NoteDTOItem): NoteDTOItem

    @DELETE("api/notes/{id}")
    suspend fun deleteNoteById(@Path("id") id: Int)

}