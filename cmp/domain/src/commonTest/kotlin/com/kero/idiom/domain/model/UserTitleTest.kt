package com.kero.idiom.domain.model

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class UserTitleTest {

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

    @Test
    fun fromLevel_overMax_returnsMunchangseong() {
        assertEquals(UserTitle.MUNCHANGSEONG, UserTitle.fromLevel(100))
    }

    @Test
    fun getNextTitle_returnsCorrectSequence() {
        assertEquals(UserTitle.YUSAENG, UserTitle.getNextTitle(UserTitle.CHORIPDONGI))
        assertEquals(UserTitle.JINSA, UserTitle.getNextTitle(UserTitle.YUSAENG))
        assertEquals(UserTitle.DAEJEHAK, UserTitle.getNextTitle(UserTitle.JINSA))
        assertEquals(UserTitle.MUNCHANGSEONG, UserTitle.getNextTitle(UserTitle.DAEJEHAK))
    }

    @Test
    fun getNextTitle_atMax_returnsNull() {
        assertNull(UserTitle.getNextTitle(UserTitle.MUNCHANGSEONG))
    }

    @Test
    fun getDescription_withSpecificLevel_returnsLevelDescription() {
        val description = UserTitle.CHORIPDONGI.getDescription(1)
        assertEquals("글눈을 갓 뜬 아이", description)
    }

    @Test
    fun getDescription_withUnmappedLevel_returnsDefault() {
        val description = UserTitle.CHORIPDONGI.getDescription(999)
        assertEquals("이제 막 글공부를 시작한 아이", description)
    }
}
