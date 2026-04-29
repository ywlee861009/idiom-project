package com.kero.idiom.domain.model

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

/** 사용자 칭호(UserTitle) 시스템의 레벨 매핑, 승급 순서, 설명문 검증 */
class UserTitleTest {

    /** 각 타이틀의 레벨 경계(하한/상한)에서 올바른 타이틀이 반환되는지 검증 */
    @Test
    fun fromLevel_boundaries() {
        assertEquals(UserTitle.CHORIPDONGI, UserTitle.fromLevel(1))
        assertEquals(UserTitle.CHORIPDONGI, UserTitle.fromLevel(10))
        assertEquals(UserTitle.YUSAENG, UserTitle.fromLevel(11))
        assertEquals(UserTitle.YUSAENG, UserTitle.fromLevel(20))
        assertEquals(UserTitle.JINSA, UserTitle.fromLevel(21))
        assertEquals(UserTitle.JINSA, UserTitle.fromLevel(30))
        assertEquals(UserTitle.DAEJEHAK, UserTitle.fromLevel(31))
        assertEquals(UserTitle.DAEJEHAK, UserTitle.fromLevel(40))
        assertEquals(UserTitle.MUNCHANGSEONG, UserTitle.fromLevel(41))
        assertEquals(UserTitle.MUNCHANGSEONG, UserTitle.fromLevel(50))
    }

    /** 정의된 최대 레벨(50)을 초과해도 크래시 없이 최종 타���틀을 반환하는지 검증 */
    @Test
    fun fromLevel_overMax_returnsMunchangseong() {
        assertEquals(UserTitle.MUNCHANGSEONG, UserTitle.fromLevel(100))
    }

    /** 타이틀 승급 순서가 초립동이→유생→진사→대제학→��창성으로 올바른지 검증 */
    @Test
    fun getNextTitle_returnsCorrectSequence() {
        assertEquals(UserTitle.YUSAENG, UserTitle.getNextTitle(UserTitle.CHORIPDONGI))
        assertEquals(UserTitle.JINSA, UserTitle.getNextTitle(UserTitle.YUSAENG))
        assertEquals(UserTitle.DAEJEHAK, UserTitle.getNextTitle(UserTitle.JINSA))
        assertEquals(UserTitle.MUNCHANGSEONG, UserTitle.getNextTitle(UserTitle.DAEJEHAK))
    }

    /** 최종 타이틀(문창성) 이후 다음 타이틀이 null인지 검증 (승급 불가 상태) */
    @Test
    fun getNextTitle_atMax_returnsNull() {
        assertNull(UserTitle.getNextTitle(UserTitle.MUNCHANGSEONG))
    }

    /** 특정 레벨에 매핑된 고유 설명문이 반환되는지 검증 (Lv1 → "글눈을 갓 뜬 아이") */
    @Test
    fun getDescription_withSpecificLevel_returnsLevelDescription() {
        val description = UserTitle.CHORIPDONGI.getDescription(1)
        assertEquals("글눈을 갓 뜬 아이", description)
    }

    /** 매핑되지 않은 레벨에서 기본 설명문(defaultDescription)이 반환되는지 검증 */
    @Test
    fun getDescription_withUnmappedLevel_returnsDefault() {
        val description = UserTitle.CHORIPDONGI.getDescription(999)
        assertEquals("이제 막 글공부를 시작한 아이", description)
    }
}
