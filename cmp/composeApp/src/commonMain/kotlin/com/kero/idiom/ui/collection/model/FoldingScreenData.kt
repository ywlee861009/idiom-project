package com.kero.idiom.ui.collection.model

/**
 * 병풍의 한 폭(Panel)을 구성하는 데이터
 */
data class FoldingScreenPanelData(
    val title: String,
    val progress: Float, // 0.0 ~ 1.0 (얼마나 진하게 그려질지)
    val type: PanelType
)

enum class PanelType {
    MOUNTAIN, // 산맥 (XP 연동)
    FOREST,   // 소나무/숲 (수집수 연동)
    WATER,    // 폭포/강 (스트릭 연동)
    CRANE     // 학/구름 (정답률 연동)
}

data class FoldingScreenState(
    val panels: List<FoldingScreenPanelData> = emptyList(),
    val isSealed: Boolean = false // 낙관(Seal) 여부
)
