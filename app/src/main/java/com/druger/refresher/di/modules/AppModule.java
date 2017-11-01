package com.druger.refresher.di.modules;

import android.app.AlarmManager;
import android.content.Context;

import com.druger.refresher.App;
import com.druger.refresher.alarms.AlarmHelper;
import com.druger.refresher.database.RealmHelper;
import com.druger.refresher.utils.PreferenceHelper;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

import static com.druger.refresher.utils.PreferenceHelper.PREFERENCES_NAME;

/**
 * Created by druger on 04.09.2017.
 */

@Module
public class AppModule {

    private App app;
    private PreferenceHelper preferenceHelper;
    private AlarmHelper alarmHelper;
    private RealmHelper realmHelper;

    public AppModule(App app) {
        this.app = app;
        preferenceHelper = new PreferenceHelper(app.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE));
        alarmHelper = new AlarmHelper((AlarmManager) app.getSystemService(Context.ALARM_SERVICE), app);
        realmHelper = new RealmHelper(app);
    }

    @Singleton
    @Provides
    Context provideAppContext() {
        return app;
    }

    @Singleton
    @Provides
    PreferenceHelper providePreferenceHelper() {
        return preferenceHelper;
    }

    @Singleton
    @Provides
    AlarmHelper provideAlarmHelper() {
        return alarmHelper;
    }

    @Singleton
    @Provides
    RealmHelper provideDBHelper() {
        return realmHelper;
    }
}
