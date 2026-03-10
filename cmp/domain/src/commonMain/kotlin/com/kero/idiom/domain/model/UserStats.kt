package com.kero.idiom.domain.model

data class UserStats(
    val totalSolvedCount: Int = 0,
    val currentStreak: Int = 0,
    val maxStreak: Int = 0,
    val lastSolvedDateMillis: Long = 0,
    val totalCorrectCount: Int = 0
) {
    // 맞힌 개수에 따른 레벨 계산 (정답 10개당 1레벨 상승)
    val level: Int get() = (totalCorrectCount / 10).coerceIn(0, 49) + 1
    
    val title: String get() = when (level) {
        in 1..10 -> "초립동이"
        in 11..20 -> "유생"
        in 21..30 -> "진사"
        in 31..40 -> "대제학"
        else -> "문창성"
    }
    
    val titleDescription: String get() = when (level) {
        in 1..10 -> "이제 막 글공부를 시작한 아이"
        in 11..20 -> "성균관에서 정진하는 학생"
        in 21..30 -> "학문적 깊이가 깊어진 선비"
        in 31..40 -> "나라의 문장을 책임지는 석학"
        else -> "문장을 주관하는 신선의 경지"
    }
}
