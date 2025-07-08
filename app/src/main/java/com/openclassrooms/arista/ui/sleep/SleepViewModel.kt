package com.openclassrooms.arista.ui.sleep

import androidx.lifecycle.ViewModel
import com.openclassrooms.arista.domain.model.Sleep
import com.openclassrooms.arista.domain.usecase.GetAllSleepsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class SleepViewModel @Inject constructor(private val getAllSleepsUseCase: GetAllSleepsUseCase) :
    ViewModel() {
    private val _sleeps = MutableStateFlow<List<Sleep>>(emptyList())
    val sleeps: StateFlow<List<Sleep>> = _sleeps.asStateFlow()

    suspend fun fetchSleeps() {
        val sleepList = getAllSleepsUseCase.execute()
        _sleeps.value = sleepList
    }
}
