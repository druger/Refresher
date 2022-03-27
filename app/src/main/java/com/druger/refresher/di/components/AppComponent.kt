package com.druger.refresher.di.components

import com.druger.refresher.di.modules.AppModule
import com.druger.refresher.util.alarms.AlarmSetter
import dagger.Component
import javax.inject.Singleton

/**
* Created by druger on 04.09.2017.
*/

@Singleton
@Component(modules = arrayOf(AppModule::class))
interface AppComponent {

    fun inject(alarmSetter: AlarmSetter)
}
