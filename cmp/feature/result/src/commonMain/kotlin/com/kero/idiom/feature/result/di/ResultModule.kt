package com.kero.idiom.feature.result.di

import com.kero.idiom.feature.result.viewmodel.ResultViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val featureResultModule = module {
    // ViewModel
    viewModel { ResultViewModel() }
}
