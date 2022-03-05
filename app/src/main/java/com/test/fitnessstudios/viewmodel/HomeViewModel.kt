package com.test.fitnessstudios.viewmodel

import android.location.Location
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.test.fitnessstudios.data.Business
import com.test.fitnessstudios.data.SearchBusinessResponse
import com.test.fitnessstudios.repository.BusinessRepository
import com.test.fitnessstudios.repository.BusinessRepositoryImpl
import com.test.fitnessstudios.repository.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    val businessRepository: BusinessRepository
) : ViewModel() {
    private val _searchBusinessFlow = MutableStateFlow<Resource<SearchBusinessResponse>?>(null)
    val searchBusinessFlow = _searchBusinessFlow.asStateFlow()

    private val _locationFlow = MutableStateFlow<Location?>(null)
    val locationFlow = _locationFlow.asStateFlow()

    private val _businessListFlow = MutableStateFlow<List<Business>>(emptyList())
    val businessListFlow = _businessListFlow.asStateFlow()


    fun searchBusiness(lat: Double, long: Double) {
        viewModelScope.launch {
            _searchBusinessFlow.value = Resource.Loading

            _searchBusinessFlow.value = businessRepository.searchBusiness(
                lat, long, "fitness", 1000, "distance"
            )
        }
    }

    fun updateBusinessList(businessList : List<Business>?) {
        businessList?.let {
            _businessListFlow.value = businessList
        }
    }

    fun updateLocation(location: Location) {
        _locationFlow.value = location
    }
}