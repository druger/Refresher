package com.druger.refresher.di.modules;

import android.app.AlarmManager
import android.content.Context
import com.druger.refresher.utils.alarm.AlarmHelper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Created by druger on 04.09.2017.
 */

@Module
@InstallIn(SingletonComponent::class)
class AppModule() {

    @Singleton
    @Provides
    fun provideAlarmHelper(@ApplicationContext context: Context): AlarmHelper =
        AlarmHelper(
            context.getSystemService(Context.ALARM_SERVICE) as AlarmManager,
            context
        )
}
