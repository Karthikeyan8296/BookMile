package com.example.precisepal.presentation.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.precisepal.domain.repository.AuthRepository
import com.example.precisepal.presentation.util.UIEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(private val authRepository: AuthRepository) :
    ViewModel() {
    //for snackBar event
    private val _uiEvent = Channel<UIEvent>()
    //convert the uiEvent channel to flow
    val uiEvent = _uiEvent.receiveAsFlow()

    //event
    fun onEvent(event: DashboardEvents) {
        when(event){
            is DashboardEvents.AnonymousUserSignInWithGoogle -> {}
            DashboardEvents.SignOut -> {
                signOut()
            }
        }
    }

    //state
    private val _state = MutableStateFlow(DashboardState())
    val state = _state.asStateFlow()


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