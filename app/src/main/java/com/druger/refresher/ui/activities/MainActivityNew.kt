package com.druger.refresher.ui.activities

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.druger.refresher.R
import com.druger.refresher.adapters.TabAdapter
import com.druger.refresher.models.ModelTaskNew
import com.druger.refresher.viewmodel.TaskViewModel
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.activity_main.*

class MainActivityNew : AppCompatActivity(R.layout.activity_main) {

    private lateinit var tabAdapter: TabAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupViewPager()
        setupViewModel()
    }

    private fun setupViewModel() {
        val taskModel: TaskViewModel by viewModels()
        taskModel.getTasks().observe(this, Observer<List<ModelTaskNew>> { tasks ->

        })
    }

    private fun setupViewPager() {
        tabAdapter = TabAdapter(supportFragmentManager, 2)

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

}
