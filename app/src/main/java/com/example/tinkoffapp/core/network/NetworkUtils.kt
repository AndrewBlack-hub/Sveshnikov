package com.example.tinkoffapp.core.network

import retrofit2.Response

inline fun <T> safeApiCall(apiCall: () -> Response<T>): SimpleResponse<T> {
    return try {
        SimpleResponse.success(apiCall.invoke())
    } catch (e: Exception) {
        SimpleResponse.failure(e)
    }
}