package com.druger.refresher.models;

import com.druger.refresher.R;

/**
 * Created by druger on 30.09.2015.
 */
class ModelSeparator(val type: Int) : Item {

    companion object {
        const val TYPE_OVERDUE = R.string.separator_overdue;
        const val TYPE_TODAY = R.string.separator_today;
        const val TYPE_TOMORROW =  R.string.separator_tomorrow;
        const val TYPE_FUTURE = R.string.separator_future;
    }

    override fun isTask(): Boolean {
        return false;
    }
}
