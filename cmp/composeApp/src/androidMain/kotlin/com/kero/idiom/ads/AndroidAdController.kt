package com.kero.idiom.ads

import android.app.Activity
import android.content.Context
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.kero.idiom.core.ads.AdController

/**
 * [AndroidAdController]
 * Android 전용 AdMob 구현체입니다.
 * [activity]는 광고를 띄울 컨텍스트입니다.
 */
class AndroidAdController(
    private val context: Context,
    private val activityProvider: () -> Activity?
) : AdController {

    private var interstitialAd: InterstitialAd? = null
    
    // 테스트용 전면 광고 ID
    private val interstitialAdUnitId = "ca-app-pub-3940256099942544/1033173712"

    override fun loadInterstitial() {
        if (interstitialAd != null) return

        val adRequest = AdRequest.Builder().build()
        InterstitialAd.load(
            context,
            interstitialAdUnitId,
            adRequest,
            object : InterstitialAdLoadCallback() {
                override fun onAdLoaded(ad: InterstitialAd) {
                    interstitialAd = ad
                    setupCallbacks()
                }

                override fun onAdFailedToLoad(error: LoadAdError) {
                    interstitialAd = null
                }
            }
        )
    }

    private fun setupCallbacks() {
        interstitialAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
            override fun onAdDismissedFullScreenContent() {
                interstitialAd = null
                loadInterstitial() // 다음 광고 미리 로드
            }
        }
    }

    override fun showInterstitial() {
        val currentActivity = activityProvider()
        if (interstitialAd != null && currentActivity != null) {
            interstitialAd?.show(currentActivity)
        } else {
            // 광고가 로드되지 않았다면 로드 시도
            loadInterstitial()
        }
    }
}
