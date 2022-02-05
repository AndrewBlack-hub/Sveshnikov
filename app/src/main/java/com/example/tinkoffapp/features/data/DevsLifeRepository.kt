package com.example.tinkoffapp.features.data

import com.example.tinkoffapp.core.network.RetrofitProvider
import com.example.tinkoffapp.core.network.SimpleResponse
import com.example.tinkoffapp.core.network.safeApiCall
import com.example.tinkoffapp.features.data.model.RandomPostModel

class DevsLifeRepository {
    private val service = RetrofitProvider.retrofit.create(DevsLifeService::class.java)

    suspend fun getRandomPost(): SimpleResponse<RandomPostModel> {
        return safeApiCall { service.getRandomPost() }
    }
}