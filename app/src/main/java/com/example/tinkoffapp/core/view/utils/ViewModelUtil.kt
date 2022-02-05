package com.example.tinkoffapp.core.view.utils

import androidx.lifecycle.MutableLiveData

inline fun <reified T> lazyLiveData() = lazy {
    MutableLiveData<T>()
}

inline fun <reified T> lazyLiveData(defaultValue: T) = lazy {
    MutableLiveData(defaultValue)
}