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
     */
    fun showInterstitial()

    /**
     * 보상형 광고(Rewarded Ad)를 보여줍니다.
     * @param onRewardEarned 사용자가 광고를 끝까지 시청하여 보상을 받을 때 호출될 콜백
     */
    fun showRewardedAd(onRewardEarned: () -> Unit)
}
