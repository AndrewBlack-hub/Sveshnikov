package com.example.tinkoffapp.features.devsLife.domain

import com.example.tinkoffapp.features.devsLife.data.model.RandomPostModel

fun RandomPostModel.mapToPresentation(): RandomPostDomain {
    return RandomPostDomain(
        id = this.id,
        description = this.description,
        gifUrl = this.gifURL
    )
}