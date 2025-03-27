package com.example.precisepal.presentation.details

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.example.precisepal.domain.model.BodyPart
import com.example.precisepal.domain.model.BodyPartValues
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
        databaseRepository.getBodyPart(bodyPartID)
    ) { state, bodyPart ->
        state.copy(
            bodyPart = bodyPart
        )
    }.catch { e ->
        _uiEvent.send(UIEvent.ShowSnackBar("Something went wrong. please try again later ${e.message}"))
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000),
        initialValue = DetailsState()
    )

    //Events
    fun onEvent(event: DetailsEvent) {
        when (event) {
            DetailsEvent.AddNewValue -> {
                val state = state.value
                val id = state.allBodyPartValues.find { it.date == state.date }?.bodyPartValueID
                val bodyPartValues = BodyPartValues(
                    value = state.textFieldValue.toFloatValue(decimalPlace = 2),
                    date = state.date,
                    bodyPartId = bodyPartID,
                    bodyPartValueID = id
                )
                upsertBodyPartValue(bodyPartValues)
                _state.update { it.copy(textFieldValue = "") }
            }

            DetailsEvent.DeleteBodyPart -> {
                deleteBodyPart()
            }

            DetailsEvent.RestoreBodyPart -> TODO()
            is DetailsEvent.ChangeMeasuringUnit -> {
                val bodyPart = state.value.bodyPart?.copy(
                    measuringUnit = event.measuringUnit.code
                )
                changeMeasuringUnit(bodyPart = bodyPart)
            }

            is DetailsEvent.DeleteBodyPartValue -> TODO()
            is DetailsEvent.OnDateChange -> {
                val date = event.millis.changeMillisToGraphDate()
                _state.update { it.copy(date = date) }
            }

            is DetailsEvent.OnTextFieldValueChange -> {
                _state.update {
                    it.copy(textFieldValue = event.value)
                }
            }

            is DetailsEvent.OnTimeRangeChange -> TODO()

        }
    }

    //inserting and updating the measuring unit
    private fun changeMeasuringUnit(bodyPart: BodyPart?) {
        viewModelScope.launch {
            bodyPart ?: return@launch
            databaseRepository.upsertBodyPort(bodyPart)
                .onSuccess {
                    _uiEvent.send(UIEvent.ShowSnackBar("Measuring Unit updated!"))
                }
                .onFailure {
                    _uiEvent.send(UIEvent.ShowSnackBar("Something went wrong. please try again later ${it.message}"))
                }
        }
    }

    //delete the body part
    private fun deleteBodyPart() {
        viewModelScope.launch {
            databaseRepository.deleteBodyPart(bodyPartID)
                .onSuccess {
                    _uiEvent.send(UIEvent.NavigateBack)
                    _uiEvent.send(UIEvent.ShowSnackBar("Deleted successfully!"))
                }
                .onFailure {
                    _uiEvent.send(UIEvent.ShowSnackBar("Something went wrong. please try again later ${it.message}"))
                }
        }
    }

    private fun upsertBodyPartValue(bodyPartValues: BodyPartValues?) {
        viewModelScope.launch {
            bodyPartValues ?: return@launch
            databaseRepository.upsertBodyPartValues(bodyPartValues)
                .onSuccess {
                    _uiEvent.send(UIEvent.ShowSnackBar("Body Part Value saved!"))
                }
                .onFailure {
                    _uiEvent.send(UIEvent.ShowSnackBar("Something went wrong. please try again later ${it.message}"))
                }
        }
    }
}