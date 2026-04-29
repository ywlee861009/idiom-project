package com.kero.idiom.domain.model

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class UserStatsTest {

    @Test
    fun nextLevelXp_isLevelTimes20() {
        val stats = UserStats(level = 5)
        assertEquals(100, stats.nextLevelXp)
    }

    @Test
    fun nextLevelXp_atLevel1() {
        val stats = UserStats(level = 1)
        assertEquals(20, stats.nextLevelXp)
    }

    @Test
    fun xpProgress_halfWay() {
        val stats = UserStats(level = 5, currentXp = 50) // 50/100
        assertEquals(0.5f, stats.xpProgress)
    }

    @Test
    fun xpProgress_zero() {
        val stats = UserStats(level = 5, currentXp = 0)
        assertEquals(0f, stats.xpProgress)
    }

    @Test
    fun xpProgress_full() {
        val stats = UserStats(level = 5, currentXp = 100)
        assertEquals(1f, stats.xpProgress)
    }

    @Test
    fun xpProgress_overflowClampedToOne() {
        val stats = UserStats(level = 5, currentXp = 150)
        assertEquals(1f, stats.xpProgress)
    }

    @Test
    fun title_level1_isChoripdongi() {
        val stats = UserStats(level = 1)
        assertEquals("초립동이", stats.title)
    }

    @Test
    fun title_level10_isChoripdongi() {
        val stats = UserStats(level = 10)
        assertEquals("초립동이", stats.title)
    }

    @Test
    fun title_level11_isYusaeng() {
        val stats = UserStats(level = 11)
        assertEquals("유생", stats.title)
    }

    @Test
    fun title_level21_isJinsa() {
        val stats = UserStats(level = 21)
        assertEquals("진사", stats.title)
    }

    @Test
    fun title_level31_isDaejehak() {
        val stats = UserStats(level = 31)
        assertEquals("대제학", stats.title)
    }

    @Test
    fun title_level41_isMunchangseong() {
        val stats = UserStats(level = 41)
        assertEquals("문창성", stats.title)
    }

    @Test
    fun nextTitleInfo_fromLevel1_isYusaeng() {
        val stats = UserStats(level = 1)
        assertEquals(UserTitle.YUSAENG, stats.nextTitleInfo)
    }

    @Test
    fun nextTitleInfo_atMaxTitle_isNull() {
        val stats = UserStats(level = 50)
        assertNull(stats.nextTitleInfo)
    }

    @Test
    fun levelsUntilNextTitle_fromLevel1() {
        val stats = UserStats(level = 1)
        assertEquals(10, stats.levelsUntilNextTitle) // 11 - 1
    }

    @Test
    fun levelsUntilNextTitle_atMaxTitle() {
        val stats = UserStats(level = 50)
        assertEquals(0, stats.levelsUntilNextTitle)
    }

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
