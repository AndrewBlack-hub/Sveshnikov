package com.example.tinkoffapp.features.devsLife.domain

import com.example.tinkoffapp.features.devsLife.data.DevsLifeRepository

class DevsLifeUseCase(private val repository: DevsLifeRepository) {

    suspend fun getRandomPost(): BasePostDomain? {
        val request = repository.getRandomPost()

        if (request.failed) return null
        if (!request.isSuccessful) return null

        return request.body.mapToPresentation()
    }

    suspend fun getPostsList(type: String, page: String): List<BasePostDomain>? {
        val request = repository.getPostsList(type = type, page = page)

        if (request.failed) return null
        if (!request.isSuccessful) return null
        return request.body.result?.map { it.mapToPresentation() }
    }
}