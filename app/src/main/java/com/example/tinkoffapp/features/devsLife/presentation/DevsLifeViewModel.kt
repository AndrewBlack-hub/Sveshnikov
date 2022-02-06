package com.example.tinkoffapp.features.devsLife.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tinkoffapp.core.view.utils.lazyLiveData
import com.example.tinkoffapp.features.devsLife.domain.BasePostDomain
import com.example.tinkoffapp.features.devsLife.domain.DevsLifeUseCase
import kotlinx.coroutines.launch

class DevsLifeViewModel(private val useCase: DevsLifeUseCase) : ViewModel() {
    val randomPostLiveData by lazyLiveData<BasePostDomain?>()
    val postsListLiveData by lazyLiveData<List<BasePostDomain>?>()
    val postsList = mutableListOf<BasePostDomain?>()
    val randomPostList: MutableList<BasePostDomain?> = mutableListOf()
    var position = 0
    var page = 0
    var type = "latest"

    fun getRandomPost() {
        viewModelScope.launch {
            randomPostLiveData.postValue(useCase.getRandomPost())
        }
    }

    fun getPostsList(type: String, page: String) {
        viewModelScope.launch {
            postsListLiveData.postValue(useCase.getPostsList(type = type, page = page))
        }
    }
}