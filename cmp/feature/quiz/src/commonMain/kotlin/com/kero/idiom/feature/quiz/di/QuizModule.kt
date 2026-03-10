package com.kero.idiom.feature.quiz.di

import com.kero.idiom.domain.usecase.GetRandomQuizUseCase
import com.kero.idiom.domain.usecase.RecordCorrectAnswerUseCase
import com.kero.idiom.domain.usecase.UpdateUserStatsUseCase
import com.kero.idiom.feature.quiz.viewmodel.QuizViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val featureQuizModule = module {
    factory { GetRandomQuizUseCase(get()) }
    factory { RecordCorrectAnswerUseCase(get()) }
    factory { UpdateUserStatsUseCase(get()) }
    
    viewModel { 
        QuizViewModel(
            getRandomQuizUseCase = get(),
            recordCorrectAnswerUseCase = get(),
            updateUserStatsUseCase = get()
        ) 
    }
}
