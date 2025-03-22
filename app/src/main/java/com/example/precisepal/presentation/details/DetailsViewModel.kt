package com.example.precisepal.presentation.details

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.example.precisepal.domain.model.BodyPart
import com.example.precisepal.domain.repository.DatabaseRepository
import com.example.precisepal.presentation.navigation.Routes
import com.example.precisepal.presentation.util.UIEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
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
                TODO()
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
            is DetailsEvent.OnDateChange -> TODO()
            is DetailsEvent.OnTextFieldValueChange -> TODO()
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
}