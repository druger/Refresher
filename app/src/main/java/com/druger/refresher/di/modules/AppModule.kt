package com.druger.refresher.di.modules;

import android.app.AlarmManager
import android.content.Context
import com.druger.refresher.App
import com.druger.refresher.alarms.AlarmHelper
import com.druger.refresher.database.RealmHelper
import com.druger.refresher.utils.PreferenceHelper
import com.druger.refresher.utils.PreferenceHelper.PREFERENCES_NAME
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
* Created by druger on 04.09.2017.
*/

@Module
class AppModule(val app: App) {

    @Singleton
    @Provides
    fun provideAppContext(): Context = app

    @Singleton
    @Provides
    fun providePreferenceHelper(): PreferenceHelper =
            PreferenceHelper(app.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE))

    @Singleton
    @Provides
    fun provideAlarmHelper(): AlarmHelper =
            AlarmHelper(app.getSystemService(Context.ALARM_SERVICE) as AlarmManager, app)

    @Singleton
    @Provides
    fun provideDBHelper(): RealmHelper = RealmHelper(app)
}
