package com.example.precisepal.presentation.addItems

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.precisepal.domain.model.BodyPart
import com.example.precisepal.domain.model.MeasuringUnit
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
        databaseRepository.getAllBodyParts()
    ) { state, bodyParts ->
        state.copy(
            bodyParts = bodyParts
        )
    }.catch { e ->
        _uiEvent.send(UIEvent.ShowSnackBar("Something went wrong. please try again later ${e.message}"))
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
                        textFieldValue = event.bodyPart.name,
                        selectedBodyPart = event.bodyPart
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
                val selectedBodyPart = state.value.selectedBodyPart
                upsertBodyPart(
                    bodyPart = BodyPart(
                        name = state.value.textFieldValue.trim(),
                        isActive = selectedBodyPart?.isActive ?: true,
                        measuringUnit = selectedBodyPart?.measuringUnit
                            ?: MeasuringUnit.CENTIMETERS.code,
                        bodyPartId = selectedBodyPart?.bodyPartId
                    )
                )
                _state.update { it.copy(textFieldValue = "") }
            }

            //dismiss
            AddItemEvent.onAddItemDialogDismiss -> {
                _state.update {
                    it.copy(
                        textFieldValue = "",
                        selectedBodyPart = null
                    )
                }
            }
            //TOGGLE BUTTON for active/inactive
            is AddItemEvent.onItemIsActiveChange -> {
                upsertBodyPart(
                    bodyPart = event.bodyPart.copy(isActive = !event.bodyPart.isActive)
                )
            }
        }
    }

    //adding the bodyPart
    private fun upsertBodyPart(bodyPart: BodyPart) {
        viewModelScope.launch {
            databaseRepository.upsertBodyPort(bodyPart)
                .onSuccess {
                    _uiEvent.send(UIEvent.ShowSnackBar("Body part added successfully"))
                }
                .onFailure {
                    _uiEvent.send(UIEvent.ShowSnackBar("Something went wrong. please try again later ${it.message}"))
                }
        }
    }
}