package com.druger.refresher

import android.app.Application
import com.druger.refresher.database.RealmHelper.Companion.REALM_NAME
import com.druger.refresher.database.RealmHelper.Companion.SCHEMA_VERSION

import com.druger.refresher.di.components.AppComponent
import com.druger.refresher.di.components.DaggerAppComponent
import com.druger.refresher.di.modules.AppModule

import io.realm.Realm
import io.realm.RealmConfiguration

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

        fun activityResumed() {
            activityVisible = true
        }

        fun activityPaused() {
            activityVisible = false
        }

        fun getApplicationComponent(): AppComponent {
            return appComponent
        }
    }

    override fun onCreate() {
        super.onCreate()
        setupRealm()
        setupDagger2()
    }

    private fun setupRealm() {
        Realm.init(this)
        val config = RealmConfiguration.Builder()
                .schemaVersion(SCHEMA_VERSION)
                .name(REALM_NAME)
                .build()
        Realm.setDefaultConfiguration(config)
    }

    private fun setupDagger2() {
        appComponent = DaggerAppComponent.builder()
                .appModule(AppModule(this))
                .build()
    }
}
