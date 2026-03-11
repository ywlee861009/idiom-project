package com.kero.idiom.core.ads

/**
 * [AdController]
 * 퀴즈 앱 내에서 광고를 제어하기 위한 공통 인터페이스입니다.
 * 플랫폼별(Android, iOS)로 실제 구현체가 주입됩니다.
 */
interface AdController {
    /**
     * 전면 광고(Interstitial Ad)를 로드합니다.
     * 앱 시작 시나 퀴즈 시작 시 미리 호출해두는 것이 좋습니다.
     */
    fun loadInterstitial()

    /**
     * 로드된 전면 광고를 보여줍니다.
     * 광고가 준비되지 않았다면 아무 동작도 하지 않거나 다음 로드를 트리거합니다.
     */
    fun showInterstitial()
}
