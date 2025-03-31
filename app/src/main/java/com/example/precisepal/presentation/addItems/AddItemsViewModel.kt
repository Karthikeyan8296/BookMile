package com.example.precisepal.presentation.addItems

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.precisepal.domain.model.Book
import com.example.precisepal.domain.model.ProgressStatus
import com.example.precisepal.domain.repository.DatabaseRepository
import com.example.precisepal.presentation.util.UIEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddItemsViewModel @Inject constructor(
    private val databaseRepository: DatabaseRepository,
) : ViewModel() {

    //for snackBar event
    private val _uiEvent = Channel<UIEvent>()

    //convert the uiEvent channel to flow
    val uiEvent = _uiEvent.receiveAsFlow()


    //state
    private val _state = MutableStateFlow(AddItemsState())
    val state = combine(
        _state,
        databaseRepository.getAllBooks()
    ) { state, bodyParts ->
        state.copy(
            books = bodyParts
        )
    }.catch { e ->
        _uiEvent.send(UIEvent.ShowSnackBar("❌ Oops! Something went wrong. Please try again later. ${e.message}"))
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000),
        initialValue = AddItemsState()
    )


    fun onEvent(event: AddItemEvent) {
        when (event) {
            //update the item
            is AddItemEvent.OnItemClick -> {
                _state.update {
                    it.copy(
                        textFieldValue = event.book.name,
                        selectedBook = event.book
                    )
                }
            }
            //TEXT FIELD
            is AddItemEvent.OnTextFieldValueChange -> {
                _state.update {
                    it.copy(
                        textFieldValue = event.value
                    )
                }
            }

            //save the item
            AddItemEvent.UpsertItem -> {
                val selectedBook = state.value.selectedBook
                upsertBodyPart(
                    book = Book(
                        name = state.value.textFieldValue.trim(),
                        isActive = selectedBook?.isActive ?: true,
                        progress = selectedBook?.progress
                            ?: ProgressStatus.IN_PROGRESS.code,
                        bookId = selectedBook?.bookId
                    )
                )
                _state.update { it.copy(textFieldValue = "") }
            }

            //dismiss
            AddItemEvent.OnAddItemDialogDismiss -> {
                _state.update {
                    it.copy(
                        textFieldValue = "",
                        selectedBook = null
                    )
                }
            }
            //TOGGLE BUTTON for active/inactive
            is AddItemEvent.OnItemIsActiveChange -> {
                upsertBodyPart(
                    book = event.book.copy(isActive = !event.book.isActive)
                )
            }
        }
    }

    //adding the bodyPart
    private fun upsertBodyPart(book: Book) {
        viewModelScope.launch {
            databaseRepository.upsertBook(book)
                .onSuccess {
                    _uiEvent.send(UIEvent.ShowSnackBar("✅ Your book has been added successfully!"))
                }
                .onFailure {
                    _uiEvent.send(UIEvent.ShowSnackBar("❌ Oops! Something went wrong. Please try again later. ${it.message}"))
                }
        }
    }
}