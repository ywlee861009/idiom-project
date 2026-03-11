package com.kero.idiom.domain.model

enum class QuizType {
    FILL_BLANK,      // A타입: 한 글자 비우기 (객관식)
    MEANING_TO_WORD, // B타입: 뜻 보고 단어 맞히기 (객관식)
    HANJA_TO_HANGUL, // C타입: 한자 보고 음독 맞히기 (객관식)
    FILL_BLANKS_2,   // ID-29: 주관식 2칸 채우기
    FILL_BLANKS_4    // ID-30: 주관식 4칸 채우기
}

data class Quiz(
    val type: QuizType,
    val originalIdiom: Idiom,
    val questionText: String,  // 화면에 표시될 메인 텍스트
    val hintText: String,      // 보조 힌트 텍스트 (뜻 또는 한자 등)
    val answer: String,        // 정답 (문자 또는 단어 전체)
    val options: List<String>, // 4개의 선택지
    val blankIndices: List<Int> = emptyList() // 주관식일 때 빈칸의 위치 (0, 1, 2, 3)
)
