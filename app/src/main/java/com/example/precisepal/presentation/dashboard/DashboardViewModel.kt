package com.example.precisepal.presentation.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.precisepal.domain.repository.AuthRepository
import com.example.precisepal.domain.repository.DatabaseRepository
import com.example.precisepal.presentation.util.UIEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val databaseRepository: DatabaseRepository,
) :
    ViewModel() {
    //for snackBar event
    private val _uiEvent = Channel<UIEvent>()

    //convert the uiEvent channel to flow
    val uiEvent = _uiEvent.receiveAsFlow()

    //event
    fun onEvent(event: DashboardEvents) {
        when (event) {
            is DashboardEvents.AnonymousUserSignInWithGoogle -> {}
            DashboardEvents.SignOut -> {
                signOut()
            }
        }
    }

    //state
    private val _state = MutableStateFlow(DashboardState())
    val state = combine(
        _state,
        databaseRepository.getSignInUserName()
    ){
            state, user ->
        state.copy(
            user = user
        )
    }.catch { e ->
        _uiEvent.send(UIEvent.ShowSnackBar("Something went wrong. please try again later ${e.message}"))
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000),
        initialValue = DashboardState()
    )

    private fun signOut() {
        viewModelScope.launch {
            authRepository.signOut()
                .onSuccess {
                    _uiEvent.send(UIEvent.ShowSnackBar("Sign Out Successfully"))
                }
                .onFailure { e ->
                    _uiEvent.send(UIEvent.ShowSnackBar("Couldn't sign out. please try again later ${e.message}"))
                }
        }
    }
}