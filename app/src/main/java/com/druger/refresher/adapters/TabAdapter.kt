package com.druger.refresher.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.druger.refresher.ui.fragments.CurrentTaskFragmentNew
import com.druger.refresher.ui.fragments.DoneTaskFragmentNew

/**
* Created by druger on 12.09.2015.
*/
class TabAdapter(
    fm: FragmentManager, private var numberOfTabs: Int
) : FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    companion object {
        const val CURRENT_TASK_FRAGMENT_POSITION = 0
        const val DONE_TASK_FRAGMENT_POSITION = 1
    }

    private var currentTaskFragment = CurrentTaskFragmentNew()
    private var doneTaskFragment = DoneTaskFragmentNew()


    override fun getItem(position: Int): Fragment {
        return if (position == 0) currentTaskFragment else doneTaskFragment
    }

    override fun getCount(): Int {
        return numberOfTabs
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return when(position) {
            0 -> "Current"
            1 -> "Done"
            else -> super.getPageTitle(position)
        }
    }
}
