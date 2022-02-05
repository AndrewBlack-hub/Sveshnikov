package com.example.tinkoffapp.di

import com.example.tinkoffapp.features.domain.DevsLifeUseCase
import com.example.tinkoffapp.features.presentation.DevsLifeViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.context.loadKoinModules
import org.koin.dsl.module

fun injectModules() = loadModules

private val loadModules by lazy {
    loadKoinModules(
        listOf(
            domainModule,
            viewModelModule,
        )
    )
}

val domainModule = module {
    single { DevsLifeUseCase(get()) }
}

val viewModelModule = module {
    viewModel { DevsLifeViewModel(get()) }
}