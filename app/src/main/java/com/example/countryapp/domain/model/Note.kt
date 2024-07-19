package com.example.countryapp.domain.model

import androidx.compose.runtime.Immutable

@Immutable
data class Note(
    val id: Int,
    val title: String,
    val content: String
)
