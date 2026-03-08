package com.kero.idiom.domain.model

data class Quiz(
    val originalIdiom: Idiom,
    val questionWord: String, // 예: "사자_어"
    val answerChar: Char,     // 예: '성'
    val options: List<Char>   // 예: ['성', '명', '공', '경']
)
