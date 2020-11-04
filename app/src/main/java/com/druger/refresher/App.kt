package com.druger.refresher

import android.app.Application

import com.druger.refresher.di.components.AppComponent
import com.druger.refresher.di.components.DaggerAppComponent
import com.druger.refresher.di.modules.AppModule

/**
* Created by druger on 29.09.2015.
*/
class App : Application() {

    companion object {
        private lateinit var appComponent: AppComponent
        private var activityVisible: Boolean = false

        fun isActivityVisible(): Boolean {
            return activityVisible
        }

        fun getApplicationComponent(): AppComponent {
            return appComponent
        }
    }

    override fun onCreate() {
        super.onCreate()
        setupDagger2()
    }

    private fun setupDagger2() {
        appComponent = DaggerAppComponent.builder()
                .appModule(AppModule(this))
                .build()
    }
}
