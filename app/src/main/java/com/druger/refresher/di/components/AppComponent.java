package com.druger.refresher.di.components;

import com.druger.refresher.activities.MainActivity;
import com.druger.refresher.alarms.AlarmSetter;
import com.druger.refresher.di.modules.AppModule;
import com.druger.refresher.dialogs.AddingTaskDialogFragment;
import com.druger.refresher.dialogs.EditTaskDialogFragment;
import com.druger.refresher.fragments.TaskFragment;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by druger on 04.09.2017.
 */

@Singleton
@Component(modules = {AppModule.class})
public interface AppComponent {

    void inject(MainActivity activity);

    void inject(TaskFragment fragment);

    void inject(EditTaskDialogFragment dialogFragment);

    void inject(AlarmSetter alarmSetter);

    void inject(AddingTaskDialogFragment dialogFragment);
}
