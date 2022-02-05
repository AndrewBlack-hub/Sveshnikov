package com.example.tinkoffapp.features.data

import com.example.tinkoffapp.features.data.model.RandomPostModel
import retrofit2.Response
import retrofit2.http.GET

interface DevsLifeService {

    @GET("random?json=true")
    suspend fun getRandomPost(): Response<RandomPostModel>
}