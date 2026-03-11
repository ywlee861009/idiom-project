package com.kero.idiom.domain.model

/**
 * 사용자의 학습 성취도에 따른 타이틀(칭호) 정의.
 */
enum class UserTitle(
    val minLevel: Int,
    val maxLevel: Int,
    val title: String,
    val description: String
) {
    CHORIPDONGI(1, 10, "초립동이", "이제 막 글공부를 시작한 아이"),
    YUSAENG(11, 20, "유생", "성균관에서 정진하는 학생"),
    JINSA(21, 30, "진사", "학문적 깊이가 깊어진 선비"),
    DAEJEHAK(31, 40, "대제학", "나라의 문장을 책임지는 석학"),
    MUNCHANGSEONG(41, 50, "문창성", "문장을 주관하는 신선의 경지");

    companion object {
        /**
         * 현재 레벨에 해당하는 타이틀을 반환합니다.
         */
        fun fromLevel(level: Int): UserTitle {
            return entries.find { level in it.minLevel..it.maxLevel } ?: MUNCHANGSEONG
        }

        /**
         * 다음 단계의 타이틀을 반환합니다. (마지막 단계인 경우 null)
         */
        fun getNextTitle(currentTitle: UserTitle): UserTitle? {
            val nextOrdinal = currentTitle.ordinal + 1
            return if (nextOrdinal < entries.size) entries[nextOrdinal] else null
        }
    }
}
