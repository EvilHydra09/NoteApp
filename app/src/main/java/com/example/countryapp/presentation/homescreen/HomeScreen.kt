package com.example.countryapp.presentation.homescreen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*


import androidx.compose.foundation.lazy.staggeredgrid.LazyStaggeredGridState
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.countryapp.domain.model.Note
import com.example.countryapp.presentation.homescreen.components.state.EmptyPlaceholder
import com.example.countryapp.presentation.homescreen.components.state.ErrorMessage
import com.example.countryapp.presentation.homescreen.components.state.LoadingIndicator
import com.example.countryapp.presentation.homescreen.components.NoteBottomSheetContent
import com.example.countryapp.presentation.homescreen.components.ReplySearchBar
import com.example.countryapp.presentation.homescreen.components.isScrollingUp

@OptIn(ExperimentalMaterialApi::class, ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(modifier: Modifier = Modifier, viewModel: HomeScreenViewModel = hiltViewModel()) {


    val uiState by viewModel.state.collectAsState()
    val lazyListState = rememberLazyStaggeredGridState()

    var searchQuery by remember { mutableStateOf(TextFieldValue("")) }

    val swipeRefreshState = rememberPullRefreshState(
        refreshing = uiState.isLoading,
        onRefresh = { viewModel.refreshNotes() }
    )
    val filteredNotes by viewModel.filteredNotes.collectAsState()

    var currentNote by remember { mutableStateOf<Note?>(null) }

    val bottomSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true,
    )
    var showBottomSheet by remember { mutableStateOf(false) }

    var showDeleteOption by remember { mutableStateOf(false) }

    if (showBottomSheet) {
        ModalBottomSheet(
            modifier = Modifier.fillMaxHeight(),
            onDismissRequest = { showBottomSheet = false },
            sheetState = bottomSheetState,

            ) {
            NoteBottomSheetContent(note = currentNote, onSave = { title, content ->
                if (currentNote == null) {
                    viewModel.createNote(title, content)
                } else {
                    viewModel.updateNote(title, content, currentNote!!.id)
                }
                showBottomSheet = false
            }, onDismiss = { showBottomSheet = false })
        }
    }

    Surface(tonalElevation = 5.dp) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .pullRefresh(state = swipeRefreshState)
        ) {
            Column {
                when {
                    uiState.isLoading -> LoadingIndicator()
                    uiState.error.isNotBlank() -> ErrorMessage(uiState.error)
                    else -> HomeContent(
                        notes = filteredNotes,
                        navigationToEdit = { noteId ->
                            currentNote = uiState.notes.find {currentNote -> currentNote.id == noteId }
                            showBottomSheet = true
                        },
                        lazyListState = lazyListState,
                        onDelete = { noteId ->
                            showDeleteOption = true
                            viewModel.deleteNoteById(noteId)
                        }
                    )
                }
            }
            ReplySearchBar(
                searchQuery = searchQuery,
                onSearchQueryChange = {
                    searchQuery = it
                    viewModel.searchNotes(it.text)
                },
                extended = lazyListState.isScrollingUp()
            )
            PullRefreshIndicator(
                refreshing = uiState.isLoading,
                state = swipeRefreshState,
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 100.dp),
                backgroundColor = if (uiState.isLoading) MaterialTheme.colorScheme.surfaceVariant else MaterialTheme.colorScheme.tertiaryContainer
            )
            FloatingActionButton(
                onClick = {
                    currentNote = null
                    showBottomSheet = true
                },
                modifier = Modifier
                    .navigationBarsPadding()
                    .align(Alignment.BottomEnd)
                    .padding(16.dp),
                containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                contentColor = MaterialTheme.colorScheme.onTertiaryContainer
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 16.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = null,
                        modifier = Modifier.size(28.dp)
                    )
                    AnimatedVisibility(!lazyListState.isScrollingUp()) {
                        Text(
                            text = "New",
                            modifier = Modifier
                                .padding(start = 8.dp, top = 3.dp)
                        )
                    }
                }
            }
        }
    }
}


@Composable
fun HomeContent(
    notes: List<Note>,
    navigationToEdit: (Int) -> Unit,
    lazyListState: LazyStaggeredGridState,
    onDelete: (Int) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        if (notes.isEmpty()) {
            EmptyPlaceholder()
        } else {
            HomeListContent(
                notes = notes,
                navigationToEdit = navigationToEdit,
                lazyListState = lazyListState,
                onDelete = onDelete
            )
        }
    }
}

@Composable
fun HomeListContent(
    notes: List<Note>,
    navigationToEdit: (Int) -> Unit,
    lazyListState: LazyStaggeredGridState,
    onDelete: (Int) -> Unit
) {
    NoteStaggeredGrid(
        notes = notes,
        lazyListState = lazyListState,
        navigationToEdit = navigationToEdit,
        onDelete = onDelete
    )
}

@Composable
fun NoteStaggeredGrid(
    notes: List<Note>,
    lazyListState: LazyStaggeredGridState,
    navigationToEdit: (Int) -> Unit,
    onDelete: (Int) -> Unit
) {
    LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Adaptive(150.dp),
        verticalItemSpacing = 8.dp,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(
            top = 110.dp,
            start = 8.dp,
            end = 8.dp,
            bottom = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()
        ),
        state = lazyListState,
    ) {
        items(items = notes, key = { it.id }) {
            NoteCard(
                note = it,
                navigationToEdit = navigationToEdit,
                onDelete = onDelete
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun NoteCard(
    note: Note,
    navigationToEdit: (Int) -> Unit,
    onDelete: (Int) -> Unit
) {
    var isLongPressed by remember { mutableStateOf(false) }

    val borderStroke by animateFloatAsState(
        targetValue = if (isLongPressed) 2f else 0.5f,
        label = "Border Stroke"
    )
    OutlinedCard(
        modifier = Modifier
            .combinedClickable(
                onClick = {
                    if (isLongPressed) {
                        isLongPressed = false
                    } else {
                        navigationToEdit(note.id)
                    }

                },
                onLongClick = {
                    isLongPressed = !isLongPressed
                }
            ),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer,
        ),
        border = BorderStroke(borderStroke.dp, MaterialTheme.colorScheme.outline)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = note.title,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = note.content,
                maxLines = 8,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                overflow = TextOverflow.Ellipsis
            )
            AnimatedVisibility(isLongPressed) {
                TextButton(
                    onClick = { onDelete(note.id) }
                ) {
                    Text(
                        "Delete",
                        color = Color.Red,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

