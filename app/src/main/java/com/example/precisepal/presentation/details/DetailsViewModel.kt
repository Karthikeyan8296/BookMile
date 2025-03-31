package com.example.precisepal.presentation.details

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.example.precisepal.domain.model.Book
import com.example.precisepal.domain.model.BookDetails
import com.example.precisepal.domain.model.TimeRange
import com.example.precisepal.domain.repository.DatabaseRepository
import com.example.precisepal.presentation.navigation.Routes
import com.example.precisepal.presentation.util.UIEvent
import com.example.precisepal.presentation.util.changeMillisToGraphDate
import com.example.precisepal.presentation.util.toFloatValue
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
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
@RequiresApi(Build.VERSION_CODES.O)
class DetailsViewModel @Inject constructor(
    private val databaseRepository: DatabaseRepository,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    //for snackBar event
    private val _uiEvent = Channel<UIEvent>()

    //convert the uiEvent channel to flow
    val uiEvent = _uiEvent.receiveAsFlow()

    //route of the id
    private val bodyPartID = savedStateHandle.toRoute<Routes.DetailsScreen>().bodyPartId

    //state
    private val _state = MutableStateFlow(DetailsState())
    val state = combine(
        _state,
        databaseRepository.getBook(bodyPartID),
        databaseRepository.getAllBookPageValues(bodyPartID)
    ) { state, bodyPart, bodyPartValues ->
        val currentDate = LocalDate.now()
        val last7DaysValues = bodyPartValues.filter { bodyPartValue ->
            bodyPartValue.date.isAfter(currentDate.minusDays(7))
        }
        val last30DaysValues = bodyPartValues.filter { bodyPartValue ->
            bodyPartValue.date.isAfter(currentDate.minusDays(30))
        }
        state.copy(
            bookName = bodyPart,
            allBookPageValues = bodyPartValues,
            graphBookPageValues = when (state.timeRange) {
                TimeRange.LAST_7_DAYS -> last7DaysValues
                TimeRange.LAST_30_DAYS -> last30DaysValues
                TimeRange.ALL_TIME -> bodyPartValues
            }
        )
    }.catch { e ->
        _uiEvent.send(UIEvent.ShowSnackBar("❌ Oops! Something went wrong. Please try again later. ${e.message}"))
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000),
        initialValue = DetailsState()
    )

    //Events
    fun onEvent(event: DetailsEvent) {
        when (event) {
            DetailsEvent.AddNewPage -> {
                val state = state.value
                val id = state.allBookPageValues.find { it.date == state.date }?.bookPagesID
                val bodyPartValues = BookDetails(
                    value = state.textFieldValue.toFloatValue(decimalPlace = 2),
                    date = state.date,
                    bookId = bodyPartID,
                    bookPagesID = id
                )
                upsertBodyPartValue(bodyPartValues)
                _state.update { it.copy(textFieldValue = "") }
            }

            DetailsEvent.DeleteBook -> {
                deleteBodyPart()
            }

            DetailsEvent.RestoreBook -> {
                upsertBodyPartValue(state.value.recentlyDeletedBook)
                _state.update { it.copy(recentlyDeletedBook = null) }
            }

            is DetailsEvent.ChangeProgress -> {
                val bodyPart = state.value.bookName?.copy(
                    progress = event.progressStatus.code
                )
                changeMeasuringUnit(bodyPart = bodyPart)
            }

            is DetailsEvent.DeleteBookPageValue -> {
                deleteBodyPartValue(event.bookPageValue)
                _state.update { it.copy(recentlyDeletedBook = event.bookPageValue) }
            }

            is DetailsEvent.OnDateChange -> {
                val date = event.millis.changeMillisToGraphDate()
                _state.update { it.copy(date = date) }
            }

            is DetailsEvent.OnTextFieldValueChange -> {
                _state.update {
                    it.copy(textFieldValue = event.value)
                }
            }

            is DetailsEvent.OnTimeRangeChange -> {
                _state.update {
                    it.copy(timeRange = event.timeRange)
                }
            }

        }
    }

    //inserting and updating the measuring unit
    private fun changeMeasuringUnit(bodyPart: Book?) {
        viewModelScope.launch {
            bodyPart ?: return@launch
            databaseRepository.upsertBook(bodyPart)
                .onSuccess {
                    _uiEvent.send(UIEvent.ShowSnackBar("✅ Your reading progress has been updated!"))
                }
                .onFailure {
                    _uiEvent.send(UIEvent.ShowSnackBar("❌ Oops! Something went wrong. Please try again later. ${it.message}"))
                }
        }
    }

    //delete the body part
    private fun deleteBodyPart() {
        viewModelScope.launch {
            databaseRepository.deleteBook(bodyPartID)
                .onSuccess {
                    _uiEvent.send(UIEvent.NavigateBack)
                    _uiEvent.send(UIEvent.ShowSnackBar("✅ Book deleted successfully!"))
                }
                .onFailure {
                    _uiEvent.send(UIEvent.ShowSnackBar("❌ Oops! Something went wrong. Please try again later. ${it.message}"))
                }
        }
    }

    private fun upsertBodyPartValue(bodyPartValues: BookDetails?) {
        viewModelScope.launch {
            bodyPartValues ?: return@launch
            databaseRepository.upsertBookPageValues(bodyPartValues)
                .onSuccess {
                    _uiEvent.send(UIEvent.ShowSnackBar("✅ Your progress is saved! Keep up the great reading!"))
                }
                .onFailure {
                    _uiEvent.send(UIEvent.ShowSnackBar("❌ Oops! Something went wrong. Please try again later. ${it.message}"))
                }
        }
    }

    private fun deleteBodyPartValue(bookDetails: BookDetails) {
        viewModelScope.launch {
            databaseRepository.deleteBookPageValue(bookDetails)
                .onSuccess {
                    _uiEvent.send(
                        UIEvent.ShowSnackBar(
                            "✅ The pages you read have been deleted.",
                            actionLabel = "Undo"
                        )
                    )
                }
                .onFailure {
                    _uiEvent.send(UIEvent.ShowSnackBar("❌ Oops! Something went wrong. Please try again later. ${it.message}"))
                }
        }
    }
}