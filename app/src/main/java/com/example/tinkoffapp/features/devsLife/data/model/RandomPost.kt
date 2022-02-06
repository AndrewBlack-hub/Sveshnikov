package com.example.tinkoffapp.features.devsLife.data.model


data class BaseResponse(
    val result: List<BasePostModel>? = null,
    val totalCount: Int? = null
)

data class BasePostModel(
    val id: Int? = null,
    val description: String? = null,
    val votes: Int? = null,
    val author: String? = null,
    val date: String? = null,
    val gifURL: String? = null,
    val gifSize: Int? = null,
    val previewURL: String? = null,
    val videoURL: String? = null,
    val videoPath: String? = null,
    val videoSize: Int? = null,
    val type: String? = null,
    val width: String? = null,
    val height: String? = null,
    val commentsCount: Int? = null,
    val fileSize: Int? = null,
    val canVote: Boolean? = null
)