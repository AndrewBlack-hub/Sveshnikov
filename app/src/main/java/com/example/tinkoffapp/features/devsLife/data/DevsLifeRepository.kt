package com.example.tinkoffapp.features.devsLife.data

import com.example.tinkoffapp.core.network.RetrofitProvider
import com.example.tinkoffapp.core.network.SimpleResponse
import com.example.tinkoffapp.core.network.safeApiCall
import com.example.tinkoffapp.features.devsLife.data.model.BasePostModel
import com.example.tinkoffapp.features.devsLife.data.model.BaseResponse

class DevsLifeRepository {
    private val service = RetrofitProvider.retrofit.create(DevsLifeService::class.java)

    suspend fun getRandomPost(): SimpleResponse<BasePostModel> {
        return safeApiCall { service.getRandomPost() }
    }

    suspend fun getPostsList(type: String, page: String): SimpleResponse<BaseResponse> {
        return safeApiCall { service.getPostsList(type = type, page = page) }
    }
}