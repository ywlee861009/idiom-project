package com.kero.idiom.core.util

object KoreanUtils {
    private val CHOSUNG = listOf(
        'ㄱ', 'ㄲ', 'ㄴ', 'ㄷ', 'ㄸ', 'ㄹ', 'ㅁ', 'ㅂ', 'ㅃ', 'ㅅ', 'ㅆ', 'ㅇ', 'ㅈ', 'ㅉ', 'ㅊ', 'ㅋ', 'ㅌ', 'ㅍ', 'ㅎ'
    )

    /**
     * 한글 문자열에서 초성을 추출합니다.
     * 한글이 아닌 문자는 그대로 유지합니다.
     */
    fun extractChosung(text: String): String {
        val result = StringBuilder()
        for (char in text) {
            if (char in '\uAC00'..'\uD7A3') {
                val index = (char - '\uAC00') / 28 / 21
                result.append(CHOSUNG[index])
            } else {
                result.append(char)
            }
        }
        return result.toString()
    }

    /**
     * 검색어가 대상 문자열에 포함되는지 확인합니다. (초성 검색 지원)
     */
    fun matches(target: String, query: String): Boolean {
        if (query.isEmpty()) return true
        
        val normalizedQuery = query.replace(" ", "").lowercase()
        val normalizedTarget = target.replace(" ", "").lowercase()

        // 1. 일반 포함 여부 확인
        if (normalizedTarget.contains(normalizedQuery)) return true

        // 2. 초성 검색 확인
        val targetChosung = extractChosung(normalizedTarget)
        if (targetChosung.contains(normalizedQuery)) return true

        return false
    }
}
