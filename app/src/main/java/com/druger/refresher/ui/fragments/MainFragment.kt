package com.druger.refresher.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.druger.refresher.R
import com.druger.refresher.adapters.TabAdapter
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.fragment_main.*


class MainFragment: Fragment(R.layout.fragment_main) {

    private lateinit var tabAdapter: TabAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViewPager()
        fabAddTask.setOnClickListener { showAddingTaskFragment() }
    }

    private fun showAddingTaskFragment() {
        findNavController().navigate(R.id.goToAddingTaskFragment)
    }

    private fun setupViewPager() {
        tabAdapter = TabAdapter(parentFragmentManager, NUMBER_OF_TABS)

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
        private const val NUMBER_OF_TABS = 2
    }
}