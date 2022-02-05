package com.example.tinkoffapp.features.devsLife.data

import com.example.tinkoffapp.features.devsLife.data.model.RandomPostModel
import retrofit2.Response
import retrofit2.http.GET

interface DevsLifeService {

    @GET("random?json=true")
    suspend fun getRandomPost(): Response<RandomPostModel>
}