package com.example.precisepal.presentation.addItems

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.precisepal.domain.repository.DatabaseRepository
import com.example.precisepal.presentation.dashboard.DashboardState
import com.example.precisepal.presentation.util.UIEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
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
}