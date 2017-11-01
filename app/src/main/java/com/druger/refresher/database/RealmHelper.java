package com.druger.refresher.database;

import android.content.Context;

import com.druger.refresher.models.ModelTask;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

import static com.druger.refresher.models.ModelTask.STATUS_CURRENT;
import static com.druger.refresher.models.ModelTask.STATUS_DONE;
import static com.druger.refresher.models.ModelTask.STATUS_OVERDUE;

/**
 * Created by druger on 21.09.2015.
 */
public class RealmHelper {

    public static final int SCHEMA_VERSION = 1;

    public static final String REALM_NAME = "refresher.realm";

    private static final String TITLE_COLUMN = "title";
    private static final String DATE_COLUMN = "date";
    private static final String STATUS_COLUMN = "status";
    private static final String STAMP_COLUMN = "timeStamp";

    private Realm realm;

    public RealmHelper(Context context) {
        realm = Realm.getDefaultInstance();
    }

    public void saveTask(ModelTask task) {
        realm.beginTransaction();
        realm.insert(task);
        realm.commitTransaction();
    }

    public ModelTask getTaskByTimestamp(long timestamp) {
        return realm.where(ModelTask.class)
                .equalTo(STAMP_COLUMN, timestamp)
                .findFirst();
    }

    public List<ModelTask> getTasksByAnyStatus() {
        RealmResults<ModelTask> results = realm.where(ModelTask.class)
                .equalTo(STATUS_COLUMN, STATUS_CURRENT)
                .or()
                .equalTo(STATUS_COLUMN, STATUS_OVERDUE)
                .findAllSorted(DATE_COLUMN);
        return realm.copyFromRealm(results);
    }

    public List<ModelTask> getTasksByDoneStatus() {
        RealmResults<ModelTask> results = realm.where(ModelTask.class)
                .equalTo(STATUS_COLUMN, STATUS_DONE)
                .findAllSorted(DATE_COLUMN);
        return realm.copyFromRealm(results);
    }

    public List<ModelTask> getTasksByTitleAndAnyStatus(String title) {
        RealmResults<ModelTask> results = realm.where(ModelTask.class)
                .like(TITLE_COLUMN, "*" + title + "*")
                .equalTo(STATUS_COLUMN, STATUS_CURRENT)
                .or()
                .equalTo(STATUS_COLUMN, STATUS_OVERDUE)
                .findAllSorted(DATE_COLUMN);
        return realm.copyFromRealm(results);
    }

    public List<ModelTask> getTasksByTitleAndDoneStatus(String title) {
        RealmResults<ModelTask> results = realm.where(ModelTask.class)
                .like(TITLE_COLUMN, "*" + title + "*")
                .equalTo(STATUS_COLUMN, STATUS_DONE)
                .findAllSorted(DATE_COLUMN);
        return realm.copyFromRealm(results);
    }

    public void updateTask(ModelTask modelTask) {
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(modelTask);
        realm.commitTransaction();
    }

    public void removeTaskByTimestamp(long timeStamp) {
        realm.beginTransaction();
        RealmResults results = realm.where(ModelTask.class)
                .equalTo(STAMP_COLUMN, timeStamp)
                .findAll();
        results.deleteAllFromRealm();
        realm.commitTransaction();
    }
}
