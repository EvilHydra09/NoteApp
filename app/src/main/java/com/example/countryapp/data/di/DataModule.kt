package com.example.countryapp.data.di

import com.example.countryapp.common.BASE_URL
import com.example.countryapp.data.remote.NoteApiService
import com.example.countryapp.data.repository.NoteRepositoryImpl
import com.example.countryapp.domain.respository.NoteRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@InstallIn(SingletonComponent::class)
@Module
object DataModule {

    @Provides
    fun provideNoteApiService(): NoteApiService {

        val logger = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        val client = OkHttpClient.Builder()
            .addInterceptor(logger)
            .build()


        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
            .create(NoteApiService::class.java)
    }


    @Provides
    fun provideNoteRepository(noteApiService: NoteApiService): NoteRepository {
        return NoteRepositoryImpl(noteApiService)
    }




}