package com.kero.idiom.feature.quiz.viewmodel

import com.kero.idiom.core.ads.AdController

class FakeAdController : AdController {
    var interstitialLoaded = false
    var rewardedAdLoaded = false
    var rewardedAdAvailable = true

    override fun loadInterstitial() {
        interstitialLoaded = true
    }

    override fun loadRewardedAd() {
        rewardedAdLoaded = true
    }

    override fun showInterstitial() {}

    override fun showRewardedAd(onRewardEarned: () -> Unit): Boolean {
        if (rewardedAdAvailable) {
            onRewardEarned()
            return true
        }
        return false
    }
}
