package com.example.tinkoffapp.features.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tinkoffapp.core.view.utils.lazyLiveData
import com.example.tinkoffapp.features.domain.DevsLifeUseCase
import com.example.tinkoffapp.features.domain.RandomPostDomain
import kotlinx.coroutines.launch

class DevsLifeViewModel(private val useCase: DevsLifeUseCase) : ViewModel() {
    val randomPostLiveData by lazyLiveData<RandomPostDomain?>()

    fun getRandomPost() {
        viewModelScope.launch {
            randomPostLiveData.postValue(useCase.getRandomPost())
        }
    }
}