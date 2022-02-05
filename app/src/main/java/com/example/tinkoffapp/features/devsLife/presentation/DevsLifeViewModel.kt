package com.example.tinkoffapp.features.devsLife.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tinkoffapp.core.view.utils.lazyLiveData
import com.example.tinkoffapp.features.devsLife.domain.DevsLifeUseCase
import com.example.tinkoffapp.features.devsLife.domain.RandomPostDomain
import kotlinx.coroutines.launch

class DevsLifeViewModel(private val useCase: DevsLifeUseCase) : ViewModel() {
    val randomPostLiveData by lazyLiveData<RandomPostDomain?>()

    fun getRandomPost() {
        viewModelScope.launch {
            randomPostLiveData.postValue(useCase.getRandomPost())
        }
    }
}