package com.kero.idiom.domain.model

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

/** 사용자 통계(UserStats) 모델의 계산 프로퍼티(레벨, XP, 타이틀) 검증 */
class UserStatsTest {

    /** 다음 레벨 필요 XP가 "레벨 x 20" 공식대로 계산되는지 검증 (Lv5 → 100XP) */
    @Test
    fun nextLevelXp_isLevelTimes20() {
        val stats = UserStats(level = 5)
        assertEquals(100, stats.nextLevelXp)
    }

    /** 레벨 1에서 다음 레벨까지 20XP가 필요한지 검증 (최소 레벨 경계) */
    @Test
    fun nextLevelXp_atLevel1() {
        val stats = UserStats(level = 1)
        assertEquals(20, stats.nextLevelXp)
    }

    /** XP 진행률이 50/100 = 0.5로 정확히 계산되는지 검증 */
    @Test
    fun xpProgress_halfWay() {
        val stats = UserStats(level = 5, currentXp = 50) // 50/100
        assertEquals(0.5f, stats.xpProgress)
    }

    /** XP가 0일 때 진행률이 0.0인지 검증 */
    @Test
    fun xpProgress_zero() {
        val stats = UserStats(level = 5, currentXp = 0)
        assertEquals(0f, stats.xpProgress)
    }

    /** XP가 최대치일 때 진행률이 1.0(100%)인지 검증 */
    @Test
    fun xpProgress_full() {
        val stats = UserStats(level = 5, currentXp = 100)
        assertEquals(1f, stats.xpProgress)
    }

    /** XP가 최대치를 초과해도 진행률이 1.0으로 클램핑되는지 검증 (오버플로우 방지) */
    @Test
    fun xpProgress_overflowClampedToOne() {
        val stats = UserStats(level = 5, currentXp = 150)
        assertEquals(1f, stats.xpProgress)
    }

    /** 레벨 1에서 타이틀이 "초립동이"(최초 칭호)인지 검증 */
    @Test
    fun title_level1_isChoripdongi() {
        val stats = UserStats(level = 1)
        assertEquals("초립동이", stats.title)
    }

    /** 레벨 10(초립동이 상한)에서도 타이틀이 변경되지 않는지 검증 */
    @Test
    fun title_level10_isChoripdongi() {
        val stats = UserStats(level = 10)
        assertEquals("초립동이", stats.title)
    }

    /** 레벨 11에서 "유생"으로 타이틀이 승급되는지 검증 */
    @Test
    fun title_level11_isYusaeng() {
        val stats = UserStats(level = 11)
        assertEquals("유생", stats.title)
    }

    /** 레벨 21에서 "진사"로 타이틀이 승급되는지 검증 */
    @Test
    fun title_level21_isJinsa() {
        val stats = UserStats(level = 21)
        assertEquals("진사", stats.title)
    }

    /** 레벨 31에서 "대제학"으로 타이틀이 승급되는지 검증 */
    @Test
    fun title_level31_isDaejehak() {
        val stats = UserStats(level = 31)
        assertEquals("대제학", stats.title)
    }

    /** 레벨 41에서 최종 타이틀 "문창성"이 부여되는지 검증 */
    @Test
    fun title_level41_isMunchangseong() {
        val stats = UserStats(level = 41)
        assertEquals("문창성", stats.title)
    }

    /** 레벨 1에서 다음 타이틀이 "유생"(2번째 칭호)인지 검증 */
    @Test
    fun nextTitleInfo_fromLevel1_isYusaeng() {
        val stats = UserStats(level = 1)
        assertEquals(UserTitle.YUSAENG, stats.nextTitleInfo)
    }

    /** 최종 타이틀(문창성) 도달 시 다음 타이틀이 null인지 검증 */
    @Test
    fun nextTitleInfo_atMaxTitle_isNull() {
        val stats = UserStats(level = 50)
        assertNull(stats.nextTitleInfo)
    }

    /** 레벨 1에서 다음 타이틀(유생, Lv11)까지 남은 레벨이 10인지 검증 */
    @Test
    fun levelsUntilNextTitle_fromLevel1() {
        val stats = UserStats(level = 1)
        assertEquals(10, stats.levelsUntilNextTitle) // 11 - 1
    }

    /** 최종 타이틀 도달 시 남은 레벨이 0인지 검증 */
    @Test
    fun levelsUntilNextTitle_atMaxTitle() {
        val stats = UserStats(level = 50)
        assertEquals(0, stats.levelsUntilNextTitle)
    }

    /** UserStats 기본 생성 시 초기값이 올바른지 검증 (Lv1, XP 0, 알림 켜짐 등) */
    @Test
    fun defaultValues() {
        val stats = UserStats()
        assertEquals(0, stats.totalSolvedCount)
        assertEquals(1, stats.level)
        assertEquals(0, stats.currentXp)
        assertEquals(0, stats.globalCombo)
        assertEquals(true, stats.isNotificationEnabled)
    }
}
