package com.kero.idiom.domain.model

data class UserStats(
    val totalSolvedCount: Int = 0,
    val currentStreak: Int = 0,
    val maxStreak: Int = 0,
    val lastSolvedDateMillis: Long = 0,
    val totalCorrectCount: Int = 0,
    val isNotificationEnabled: Boolean = true,
    val dataVersion: Int = 0, // 📜 서책(데이터) 버전 추가
    val level: Int = 1, // 🌟 현재 레벨 (개편: 영구 저장)
    val currentXp: Int = 0 // 🌟 현재 레벨에서의 경험치 (개편: 영구 저장)
) {
    // 다음 레벨까지 필요한 경험치 (개편 공식: Level * 20)
    val nextLevelXp: Int get() = level * 20

    // UI용 경험치 진행률
    val xpProgress: Float get() = if (nextLevelXp > 0) {
        (currentXp.toFloat() / nextLevelXp.toFloat()).coerceIn(0f, 1f)
    } else 0f

    // 현재 타이틀 정보
    val currentTitleInfo: UserTitle get() = UserTitle.fromLevel(level)
    
    // 다음 타이틀 정보
    val nextTitleInfo: UserTitle? get() = UserTitle.getNextTitle(currentTitleInfo)

    // 다음 타이틀까지 남은 레벨 수
    val levelsUntilNextTitle: Int get() {
        val next = nextTitleInfo ?: return 0
        return (next.minLevel - level).coerceAtLeast(0)
    }

    val title: String get() = currentTitleInfo.title
    val titleDescription: String get() = currentTitleInfo.description
}
