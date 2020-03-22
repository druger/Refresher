package com.druger.refresher.ui.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.druger.refresher.R
import com.druger.refresher.adapters.TabAdapter
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.activity_main.*

class MainActivityNew : AppCompatActivity(R.layout.activity_main) {

    private lateinit var tabAdapter: TabAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupViewPager()
        fabAddTask.setOnClickListener { showAddingTaskFragment() }
    }

    private fun showAddingTaskFragment() {

    }

    private fun setupViewPager() {
        tabAdapter = TabAdapter(supportFragmentManager, NUMBER_OF_TABS)

        viewPager.adapter = tabAdapter
        viewPager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayout))

        tabLayout.setupWithViewPager(viewPager)
        tabLayout.addOnTabSelectedListener(object: TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                viewPager.currentItem = tab.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {

            }

            override fun onTabReselected(tab: TabLayout.Tab) {

            }
        })
    }

    companion object {
        const val NUMBER_OF_TABS = 2
    }
}
