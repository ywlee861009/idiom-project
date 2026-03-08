package com.kero.idiom.domain.model

enum class QuizType {
    FILL_BLANK,      // A타입: 한 글자 비우기
    MEANING_TO_WORD, // B타입: 뜻 보고 단어 맞히기
    HANJA_TO_HANGUL  // C타입: 한자 보고 음독 맞히기
}

data class Quiz(
    val type: QuizType,
    val originalIdiom: Idiom,
    val questionText: String,  // 화면에 표시될 메인 텍스트
    val hintText: String,      // 보조 힌트 텍스트 (뜻 또는 한자 등)
    val answer: String,        // 정답 (문자 또는 단어 전체)
    val options: List<String>  // 4개의 선택지
)
