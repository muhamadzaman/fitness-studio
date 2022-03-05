package com.test.fitnessstudios.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.test.fitnessstudios.data.DirectionsResponse
import com.test.fitnessstudios.data.SearchBusinessResponse
import com.test.fitnessstudios.repository.AppRepository
import com.test.fitnessstudios.repository.BusinessRepository
import com.test.fitnessstudios.repository.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val appRepository: AppRepository
) : ViewModel() {

    private val _directionsFlow = MutableStateFlow<Resource<DirectionsResponse>?>(null)
    val directionsFlow = _directionsFlow.asStateFlow()

    fun getDirections(url: String) {
        viewModelScope.launch {
            _directionsFlow.value = Resource.Loading

            _directionsFlow.value = appRepository.getDirections(
                url
            )
        }
    }
}