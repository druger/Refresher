package com.druger.refresher.adapters

import android.app.Fragment
import android.app.FragmentManager
import android.support.v13.app.FragmentStatePagerAdapter

import com.druger.refresher.fragments.CurrentTaskFragment
import com.druger.refresher.fragments.DoneTaskFragment

/**
* Created by druger on 12.09.2015.
*/
class TabAdapter(fm: FragmentManager, private var numberOfTabs: Int) : FragmentStatePagerAdapter(fm) {

    companion object {
        const val CURRENT_TASK_FRAGMENT_POSITION = 0
        const val DONE_TASK_FRAGMENT_POSITION = 1
    }

    private var currentTaskFragment = CurrentTaskFragment()
    private var doneTaskFragment = DoneTaskFragment()

    override fun getItem(position: Int): Fragment? {
        return when (position) {
            0 -> currentTaskFragment
            1 -> doneTaskFragment
            else -> null
        }
    }

    override fun getCount(): Int {
        return numberOfTabs
    }
}
