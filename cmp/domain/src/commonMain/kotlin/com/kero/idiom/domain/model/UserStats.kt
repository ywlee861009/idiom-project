package com.kero.idiom.domain.model

data class UserStats(
    val totalSolvedCount: Int = 0,
    val currentStreak: Int = 0,
    val maxStreak: Int = 0,
    val lastSolvedDateMillis: Long = 0,
    val totalCorrectCount: Int = 0,
    val isNotificationEnabled: Boolean = true
) {
    // 맞힌 개수에 따른 레벨 계산 (정답 10개당 1레벨 상승)
    val level: Int get() = (totalCorrectCount / 10).coerceIn(0, 49) + 1
    
    // 현재 타이틀 정보
    val currentTitleInfo: UserTitle get() = UserTitle.fromLevel(level)
    
    // 다음 타이틀 정보
    val nextTitleInfo: UserTitle? get() = UserTitle.getNextTitle(currentTitleInfo)

    // 다음 타이틀까지 남은 레벨 수
    val levelsUntilNextTitle: Int get() {
        val next = nextTitleInfo ?: return 0
        return (next.minLevel - level).coerceAtLeast(0)
    }

    // 기존 프로퍼티 유지
    val title: String get() = currentTitleInfo.title
    val titleDescription: String get() = currentTitleInfo.description
}
