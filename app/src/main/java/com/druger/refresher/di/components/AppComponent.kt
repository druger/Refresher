package com.druger.refresher.di.components

import com.druger.refresher.activities.MainActivity
import com.druger.refresher.alarms.AlarmSetter
import com.druger.refresher.di.modules.AppModule
import com.druger.refresher.dialogs.AddingTaskDialogFragment
import com.druger.refresher.dialogs.EditTaskDialogFragment
import com.druger.refresher.fragments.TaskFragment
import dagger.Component
import javax.inject.Singleton

/**
* Created by druger on 04.09.2017.
*/

@Singleton
@Component(modules = arrayOf(AppModule::class))
interface AppComponent {

    fun inject(activity: MainActivity)

    fun inject(fragment: TaskFragment)

    fun inject(dialogFragment: EditTaskDialogFragment)

    fun inject(alarmSetter: AlarmSetter)

    fun inject(dialogFragment: AddingTaskDialogFragment)
}