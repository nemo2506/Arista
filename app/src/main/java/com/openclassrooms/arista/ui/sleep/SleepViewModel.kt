package com.openclassrooms.arista.ui.sleep

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.openclassrooms.arista.domain.model.Sleep
import com.openclassrooms.arista.domain.usecase.GetAllSleepsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SleepViewModel @Inject constructor(
    private val getAllSleepsUseCase: GetAllSleepsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    init {
        observeSleeps()
    }   

    private fun observeSleeps() {
        viewModelScope.launch {
            getAllSleepsUseCase.execute()
                .catch {
                    _uiState.update { it.copy(isSleepReady = false) }
                }
                .collect { sleeps ->
                    if (sleeps.isEmpty()) {
                        _uiState.update { it.copy(isSleepReady = false) }
                    } else {
                        _uiState.update { it.copy(sleeps = sleeps) }
                    }
                }
        }
    }    
}
/**
 * Data class that represents the UI state for the exercise screen.
 *
 * @param sleeps List of sleeps to display.
 */
data class UiState(
    var sleeps: List<Sleep>? = null,
    var isSleepReady: Boolean? = null
)

