package com.druger.refresher

import android.app.Activity
import android.view.View
import com.druger.refresher.R.id.banner

import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView

/**
* Created by druger on 01.10.2015.
*/
class Ads {

    companion object {
        fun showBanner(activity: Activity) {

            val banner: AdView = activity.findViewById(banner)
            val adRequest = AdRequest.Builder().build()
            banner.loadAd(adRequest)

            banner.adListener = object : AdListener() {
                override fun onAdLoaded() {
                    super.onAdLoaded()
                    setupContentViewPadding(activity, banner.height)
                }
            }
        }

        fun setupContentViewPadding(activity: Activity, padding: Int) {
            val view: View = activity.findViewById(R.id.coordinator)
            view.setPadding(view.paddingLeft, view.paddingTop,
                    view.paddingRight, padding)
        }
    }
}
