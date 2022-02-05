package com.example.tinkoffapp.features.domain

import com.example.tinkoffapp.features.data.model.RandomPostModel

fun RandomPostModel.mapToPresentation(): RandomPostDomain {
    return RandomPostDomain(
        id = this.id,
        description = this.description,
        gifUrl = this.gifURL
    )
}