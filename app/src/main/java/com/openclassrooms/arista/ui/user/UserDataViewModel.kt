package com.openclassrooms.arista.ui.user

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.openclassrooms.arista.domain.model.User
import com.openclassrooms.arista.domain.usecase.GetUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserDataViewModel @Inject constructor(
    private val getUserUseCase: GetUserUseCase
) :
    ViewModel() {

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    init {
        loadUserData()
    }

    private fun loadUserData() {
        viewModelScope.launch {
            getUserUseCase.execute().collect { user ->
                if (user == null) {
                    _uiState.update {
                        it.copy(isUserReady = false)
                    }
                } else {
                    _uiState.update {
                        it.copy(user = user, isUserReady = true)
                    }
                }
            }
        }
    }
}

data class UiState(
    var user: User? = null,
    var message: String? = null,
    var isUserReady: Boolean? = null
)
