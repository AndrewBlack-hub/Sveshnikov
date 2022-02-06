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

    val topList = mutableListOf<BasePostDomain?>()
    val latestList = mutableListOf<BasePostDomain?>()
    val hotList = mutableListOf<BasePostDomain?>()
    val randomPostList: MutableList<BasePostDomain?> = mutableListOf()

    var position = 0
    var topListPosition = 0
    var hotListPosition = 0
    var latestListPosition = 0

    var topPage = 0
    var hotPage = 0
    var latestPage = 0

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