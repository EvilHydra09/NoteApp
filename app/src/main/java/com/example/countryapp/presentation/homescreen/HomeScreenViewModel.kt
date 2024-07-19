package com.example.countryapp.presentation.homescreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.countryapp.common.Resource
import com.example.countryapp.domain.model.Note
import com.example.countryapp.domain.use_case.CreateNoteUseCase
import com.example.countryapp.domain.use_case.DeleteNoteByIdUseCase
import com.example.countryapp.domain.use_case.GetNoteListUseCase
import com.example.countryapp.domain.use_case.UpdateNoteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val getNoteListUseCase: GetNoteListUseCase,
    private val createNoteUseCase: CreateNoteUseCase,
    private val updateNoteUseCase: UpdateNoteUseCase,
    private val deleteNoteByIdUseCase: DeleteNoteByIdUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(NoteState())
    val state: StateFlow<NoteState> get() = _state.asStateFlow()

    private val _filteredNotes = MutableStateFlow<List<Note>>(emptyList())
    val filteredNotes: StateFlow<List<Note>> get() = _filteredNotes.asStateFlow()

    init {
        loadNotes()
    }

    private fun loadNotes() {
        getNoteListUseCase().onEach { resource ->
            _state.value = when (resource) {
                is Resource.OnLoading -> _state.value.copy(isLoading = true)
                is Resource.Success -> {
                    val reversedNotes = resource.data?.reversed() ?: emptyList()
                    _filteredNotes.value = reversedNotes
                    _state.value.copy(notes = reversedNotes, isLoading = false)
                }
                is Resource.Error -> _state.value.copy(error = resource.message ?: "", isLoading = false)
            }
        }.launchIn(viewModelScope)
    }

    fun refreshNotes() {
        loadNotes()
    }

    fun createNote(title: String, content: String) {
        handleNoteOperation(createNoteUseCase(Note(title = title, content = content, id = 0)))
    }

    fun updateNote(title: String, content: String, id: Int) {
        handleNoteOperation(updateNoteUseCase(Note(title = title, content = content, id = id)))
    }

    fun deleteNoteById(id: Int) {
        handleUnitOperation(deleteNoteByIdUseCase(id))
    }

    private fun handleNoteOperation(operation: Flow<Resource<Note>>) {
        operation.onEach { resource ->
            _state.value = when (resource) {
                is Resource.OnLoading -> _state.value.copy(isLoading = true)
                is Resource.Success -> {
                    loadNotes()
                    _state.value.copy(note = resource.data, isLoading = false)
                }
                is Resource.Error -> _state.value.copy(error = resource.message ?: "", isLoading = false)
            }
        }.launchIn(viewModelScope)
    }

    private fun handleUnitOperation(operation: Flow<Resource<Unit>>) {
        operation.onEach { resource ->
            _state.value = when (resource) {
                is Resource.OnLoading -> _state.value.copy(isLoading = true)
                is Resource.Success -> {
                    loadNotes()
                    _state.value.copy(isLoading = false)
                }
                is Resource.Error -> _state.value.copy(error = resource.message ?: "", isLoading = false)
            }
        }.launchIn(viewModelScope)
    }

    fun setSelectedEmail(emailId: Int) {
        val email = _state.value.notes.find { it.id == emailId }
        _state.value = _state.value.copy(
            note = email,
            isEditOnlyOpen = true
        )
    }

    fun searchNotes(query: String) {
        _filteredNotes.value = if (query.isEmpty()) {
            _state.value.notes
        } else {
            _state.value.notes.filter {
                it.title.contains(query, ignoreCase = true) || it.content.contains(query, ignoreCase = true)
            }
        }
    }
}

data class NoteState(
    val notes: List<Note> = emptyList(),
    val note: Note? = null,
    val isLoading: Boolean = false,
    val error: String = "",
    val isEditOnlyOpen: Boolean = false
)
