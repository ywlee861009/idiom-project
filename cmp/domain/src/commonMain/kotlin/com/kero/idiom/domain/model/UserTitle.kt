package com.kero.idiom.domain.model

/**
 * 사용자의 학습 성취도에 따른 타이틀(칭호) 정의.
 */
enum class UserTitle(
    val minLevel: Int,
    val maxLevel: Int,
    val title: String,
    val defaultDescription: String
) {
    CHORIPDONGI(1, 10, "초립동이", "이제 막 글공부를 시작한 아이"),
    YUSAENG(11, 20, "유생", "성균관에서 정진하는 학생"),
    JINSA(21, 30, "진사", "학문적 깊이가 깊어진 선비"),
    DAEJEHAK(31, 40, "대제학", "나라의 문장을 책임지는 석학"),
    MUNCHANGSEONG(41, 50, "문창성", "문장을 주관하는 신선의 경지");

    /**
     * 해당 레벨에 특화된 설명을 반환합니다.
     */
    fun getDescription(level: Int): String {
        return levelDescriptions[level] ?: defaultDescription
    }

    companion object {
        private val levelDescriptions = mapOf(
            1 to "글눈을 갓 뜬 아이",
            2 to "천자문을 들추어보는 소년",
            3 to "하늘 천, 따 지를 읊기 시작함",
            4 to "붓 잡는 법을 처음 배운 서동",
            5 to "동네 글방에서 으뜸인 동자",
            6 to "훈장님께 칭찬을 듣기 시작함",
            7 to "동시를 지어 어른들을 놀래킴",
            8 to "글귀 하나에 세상을 담는 아이",
            9 to "책 읽는 소리가 담장 밖까지 들림",
            10 to "드디어 관례를 앞둔 총명한 소년",
            11 to "성균관의 문턱을 처음 넘은 유생",
            12 to "논어와 맹자를 통달하기 시작함",
            13 to "밤늦도록 호롱불 아래 정진함",
            14 to "학우들과 깊은 토론을 나누는 선비",
            15 to "사서삼경의 깊은 뜻을 깨우침",
            16 to "임금님의 정치를 걱정하는 청년",
            17 to "과거 시험을 준비하며 붓을 벼림",
            18 to "문장이 수려하여 문묘의 자랑임",
            19 to "학문에 대한 열정이 식지 않는 인재",
            20 to "향시에 합격하여 진사의 길로 나아감",
            21 to "진사과에 합격하여 명성을 떨침",
            22 to "고결한 성품과 깊은 학식을 갖춤",
            23 to "산수화 한 편에 시를 곁들임",
            24 to "세상의 이치를 꿰뚫는 혜안",
            25 to "학문적 성취가 하늘에 닿기 시작함",
            26 to "제자들을 가르치며 덕을 쌓음",
            27 to "나랏일에 대한 올곧은 상소를 올림",
            28 to "만 권의 책을 독파한 지혜의 상징",
            29 to "천하의 문장가들과 어깨를 나란히 함",
            30 to "드디어 조정의 부름을 받은 대가",
            31 to "홍문관의 수장, 대제학의 위엄",
            32 to "나라의 모든 문장을 총괄함",
            33 to "역사의 기록을 책임지는 사관들의 스승",
            34 to "임금님의 옆에서 지혜를 전하는 책사",
            35 to "한 마디 글귀로 만인의 마음을 다스림",
            36 to "온 나라의 학자들이 우러러보는 거목",
            37 to "학문적 업적이 불멸의 기록으로 남음",
            38 to "세종대왕의 뜻을 받드는 집현전의 기둥",
            39 to "동방 예의지국의 문풍을 세우는 어른",
            40 to "인간으로서 도달할 수 있는 최고의 경지",
            41 to "인간계를 넘어 신선의 영역에 발을 들임",
            42 to "별의 기운을 받아 글을 쓰는 자",
            43 to "붓 끝에서 우주가 탄생함",
            44 to "세상의 모든 지식을 손안에 쥠",
            45 to "시간과 공간을 초월한 지혜의 수호자",
            46 to "하늘의 뜻을 문장으로 풀어내는 신관",
            47 to "영겁의 세월 동안 회자될 성현",
            48 to "은하수처럼 빛나는 문장의 원천",
            49 to "만물의 근원을 꿰뚫어 보는 통찰력",
            50 to "지상의 글공부를 완성하고 별이 된 존재"
        )

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
