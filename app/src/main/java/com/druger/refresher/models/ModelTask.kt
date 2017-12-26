package com.druger.refresher.models;

import com.druger.refresher.R
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import java.util.*

/**
 * Created by druger on 16.09.2015.
 */
class ModelTask(var status: Int = -1, var timeStamp: Long = Date().time) : RealmObject(), Item {

    companion object {
        private const val PRIORITY_LOW = 0
        private const val PRIORITY_NORMAL = 1
        private const val PRIORITY_HIGH = 2

        const val STATUS_OVERDUE = 0
        const val STATUS_CURRENT = 1
        const val STATUS_DONE = 2
    }

    @PrimaryKey
    private lateinit var id: String
    var priority:Int = 0;
    var title: String = ""
    var date: Long = 0

    constructor (title: String, date: Long, priority: Int, status: Int, timeStamp: Long) : this() {
        this.title = title
        this.date = date
        this.priority = priority
    }

    fun getPriorityColor(): Int {
        when(priority) {
            PRIORITY_HIGH ->
            if (status == STATUS_CURRENT || status == STATUS_OVERDUE) {
                return R.color.priority_high;
            } else {
                return R.color.priority_high_selected;
            }
            PRIORITY_NORMAL ->
            if (status == STATUS_CURRENT || status == STATUS_OVERDUE) {
                return R.color.priority_normal;
            } else {
                return R.color.priority_normal_selected;
            }
            PRIORITY_LOW ->
            if (status == STATUS_CURRENT || status == STATUS_OVERDUE) {
                return R.color.priority_low;
            } else {
                return R.color.priority_low_selected;
            }
            else -> return 0
        }
    }

    override fun isTask(): Boolean {
        return true;
    }
}
