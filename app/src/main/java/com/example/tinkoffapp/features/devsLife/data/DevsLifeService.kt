package com.example.tinkoffapp.features.devsLife.data

import com.example.tinkoffapp.features.devsLife.data.model.BasePostModel
import com.example.tinkoffapp.features.devsLife.data.model.BaseResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface DevsLifeService {

    @GET("random?json=true")
    suspend fun getRandomPost(): Response<BasePostModel>

    @GET("{type}/{page}?json=true")
    suspend fun getPostsList(
        @Path("type") type: String,
        @Path("page") page: String
    ): Response<BaseResponse>
}