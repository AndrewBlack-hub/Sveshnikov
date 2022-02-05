package com.example.tinkoffapp.features.domain

import com.example.tinkoffapp.features.data.DevsLifeRepository

class DevsLifeUseCase(private val repository: DevsLifeRepository) {

    suspend fun getRandomPost(): RandomPostDomain? {
        val request = repository.getRandomPost()

        if (request.failed) return null
        if (!request.isSuccessful) return null

        return request.body.mapToPresentation()
    }
}