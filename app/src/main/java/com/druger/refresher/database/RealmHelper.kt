package com.druger.refresher.database

import android.content.Context
import com.druger.refresher.models.ModelTask
import com.druger.refresher.models.ModelTask.Companion.STATUS_CURRENT
import com.druger.refresher.models.ModelTask.Companion.STATUS_DONE
import com.druger.refresher.models.ModelTask.Companion.STATUS_OVERDUE
import io.realm.Realm
import io.realm.RealmResults


/**
* Created by druger on 21.09.2015.
*/

class RealmHelper(var context: Context) {

    companion object {
        const val SCHEMA_VERSION: Long = 1
        const val REALM_NAME = "refresher.realm"

        const private val TITLE_COLUMN = "title"
        const private val DATE_COLUMN = "date"
        const private val STATUS_COLUMN = "status"
        const private val STAMP_COLUMN = "timeStamp"
    }

    private var realm: Realm = Realm.getDefaultInstance()

    fun saveTask(task: ModelTask) {
        realm.beginTransaction()
        realm.insert(task)
        realm.commitTransaction()
    }

    fun getTaskByTimestamp(timestamp: Long): ModelTask? {
        return realm.where(ModelTask::class.java)
                .equalTo(STAMP_COLUMN, timestamp)
                .findFirst()
    }

    fun getTasksByAnyStatus(): List<ModelTask> {
        val results: RealmResults<ModelTask> = realm.where(ModelTask::class.java)
                .equalTo(STATUS_COLUMN, STATUS_CURRENT)
                .or()
                .equalTo(STATUS_COLUMN, STATUS_OVERDUE)
                .sort(DATE_COLUMN)
                .findAll()
        return realm.copyFromRealm(results)
    }

    fun getTasksByDoneStatus(): List<ModelTask> {
        val results: RealmResults<ModelTask> = realm.where(ModelTask::class.java)
                .equalTo(STATUS_COLUMN, STATUS_DONE)
                .sort(DATE_COLUMN)
                .findAll()
        return realm.copyFromRealm(results)
    }

    fun getTasksByTitleAndAnyStatus(title: String): List<ModelTask> {
        val results: RealmResults<ModelTask> = realm.where(ModelTask::class.java)
                .like(TITLE_COLUMN, "*$title*")
                .equalTo(STATUS_COLUMN, STATUS_CURRENT)
                .or()
                .equalTo(STATUS_COLUMN, STATUS_OVERDUE)
                .sort(DATE_COLUMN)
                .findAll()
        return realm.copyFromRealm(results)
    }

    fun getTasksByTitleAndDoneStatus(title: String): List<ModelTask> {
        val results: RealmResults<ModelTask> = realm.where(ModelTask::class.java)
                .like(TITLE_COLUMN, "*$title*")
                .equalTo(STATUS_COLUMN, STATUS_DONE)
                .sort(DATE_COLUMN)
                .findAll()
        return realm.copyFromRealm(results)
    }

    fun updateTask(modelTask: ModelTask) {
        realm.beginTransaction()
        realm.copyToRealmOrUpdate(modelTask)
        realm.commitTransaction()
    }

    fun removeTaskByTimestamp(timeStamp: Long) {
        realm.beginTransaction()
        val results = realm.where(ModelTask::class.java)
                .equalTo(STAMP_COLUMN, timeStamp)
                .findAll()
        results.deleteAllFromRealm()
        realm.commitTransaction()
    }
}
