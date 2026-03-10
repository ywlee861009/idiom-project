package com.kero.idiom.feature.quiz.di

import com.kero.idiom.domain.usecase.GetRandomQuizUseCase
import com.kero.idiom.feature.quiz.viewmodel.QuizViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val featureQuizModule = module {
    // get() 이름 충돌을 피하기 위해 명시적으로 GetRandomQuizUseCase를 가져옵니다.
    viewModel { QuizViewModel(get<GetRandomQuizUseCase>()) }
}
