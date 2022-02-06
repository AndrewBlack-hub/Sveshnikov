package com.example.tinkoffapp.features.devsLife.domain

import com.example.tinkoffapp.features.devsLife.data.model.RandomPostModel

fun RandomPostModel.mapToPresentation(): RandomPostDomain {
    var buildTrueUrl = ""
    // По запросу приходит gifUrl в виде: "http://...", который глайд не может обработать без ошибки
    // Поэтому меняем "http" на "https", который glide успешно обрабатывает и выдает в результате
    // гифку
    if (!this.gifURL.isNullOrEmpty()) {
        buildTrueUrl = if (this.gifURL.startsWith("https")) {
            this.gifURL
        } else {
            this.gifURL.replace("http", "https")
        }
    }

    return RandomPostDomain(
        id = this.id,
        description = this.description,
        gifUrl = buildTrueUrl
    )
}