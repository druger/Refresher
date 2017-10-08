package com.druger.refresher;

import android.app.Application;

import com.druger.refresher.di.components.AppComponent;
import com.druger.refresher.di.components.DaggerAppComponent;
import com.druger.refresher.di.modules.AppModule;

import io.realm.Realm;
import io.realm.RealmConfiguration;

import static com.druger.refresher.database.DBHelper.REALM_NAME;
import static com.druger.refresher.database.DBHelper.SCHEMA_VERSION;

/**
 * Created by druger on 29.09.2015.
 */
public class App extends Application {

    private static AppComponent appComponent;

    private static boolean activityVisible;

    @Override
    public void onCreate() {
        super.onCreate();
        setupDagger2();
        setupRealm();
    }

    private void setupRealm() {
        Realm.init(this);
        RealmConfiguration config = new RealmConfiguration.Builder()
                .schemaVersion(SCHEMA_VERSION)
                .name(REALM_NAME)
                .build();
        Realm.setDefaultConfiguration(config);
    }

    private void setupDagger2() {
        appComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .build();
    }

    public static boolean isActivityVisible() {
        return activityVisible;
    }

    public static void activityResumed(){
        activityVisible = true;
    }

    public static void activityPaused(){
        activityVisible = false;
    }

    public static AppComponent getAppComponent() {
        return appComponent;
    }

}
