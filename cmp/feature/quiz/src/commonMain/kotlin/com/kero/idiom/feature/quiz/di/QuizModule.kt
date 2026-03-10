package com.kero.idiom.feature.quiz.di

import com.kero.idiom.domain.usecase.GetRandomQuizUseCase
import com.kero.idiom.feature.quiz.viewmodel.QuizViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val featureQuizModule = module {
    factory { GetRandomQuizUseCase(get()) }
    viewModel { QuizViewModel(get<GetRandomQuizUseCase>()) }
}
