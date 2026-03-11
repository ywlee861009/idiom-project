package com.kero.idiom.ads

import android.app.Activity
import android.content.Context
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import com.kero.idiom.BuildConfig
import com.kero.idiom.core.ads.AdController

/**
 * [AndroidAdController]
 * Android 전용 AdMob 구현체입니다.
 */
class AndroidAdController(
    private val context: Context,
    private val activityProvider: () -> Activity?
) : AdController {

    private var interstitialAd: InterstitialAd? = null
    private var rewardedAd: RewardedAd? = null
    
    private val interstitialAdUnitId = BuildConfig.ADMOB_INTERSTITIAL_ID
    private val rewardedAdUnitId = BuildConfig.ADMOB_REWARDED_ID

    override fun loadInterstitial() {
        if (interstitialAd != null) return
        val adRequest = AdRequest.Builder().build()
        InterstitialAd.load(context, interstitialAdUnitId, adRequest, object : InterstitialAdLoadCallback() {
            override fun onAdLoaded(ad: InterstitialAd) {
                interstitialAd = ad
                interstitialAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
                    override fun onAdDismissedFullScreenContent() {
                        interstitialAd = null
                        loadInterstitial()
                    }
                }
            }
            override fun onAdFailedToLoad(error: LoadAdError) { interstitialAd = null }
        })
    }

    override fun loadRewardedAd() {
        if (rewardedAd != null) return
        val adRequest = AdRequest.Builder().build()
        RewardedAd.load(context, rewardedAdUnitId, adRequest, object : RewardedAdLoadCallback() {
            override fun onAdLoaded(ad: RewardedAd) {
                rewardedAd = ad
                rewardedAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
                    override fun onAdDismissedFullScreenContent() {
                        rewardedAd = null
                        loadRewardedAd()
                    }
                }
            }
            override fun onAdFailedToLoad(error: LoadAdError) { rewardedAd = null }
        })
    }

    override fun showInterstitial() {
        val currentActivity = activityProvider()
        if (interstitialAd != null && currentActivity != null) {
            interstitialAd?.show(currentActivity)
        } else {
            loadInterstitial()
        }
    }

    override fun showRewardedAd(onRewardEarned: () -> Unit): Boolean {
        val currentActivity = activityProvider()
        return if (rewardedAd != null && currentActivity != null) {
            rewardedAd?.show(currentActivity) { 
                onRewardEarned() 
            }
            true
        } else {
            loadRewardedAd()
            false
        }
    }
}
