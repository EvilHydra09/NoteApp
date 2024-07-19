package com.example.countryapp.common

import com.example.countryapp.data.model.NoteDTOItem
import com.example.countryapp.domain.model.Note

fun NoteDTOItem.toNote(): Note {
    return Note(
        content = content,
        id = id,
        title = title
    )
}

fun Note.toNoteDTOItem(): NoteDTOItem {
    return NoteDTOItem(
        content = content,
        id = id,
        title = title
    )
}
const val BASE_URL = "http://192.168.31.136:8080/"