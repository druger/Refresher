package com.druger.refresher.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.druger.refresher.R
import com.druger.refresher.adapters.CurrentTaskAdapterNew
import kotlinx.android.synthetic.main.fragment_current_task.*

class CurrentTaskFragmentNew: Fragment(R.layout.fragment_current_task) {

    lateinit var adapter: CurrentTaskAdapterNew

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecycler()
    }

    private fun setupRecycler() {
        adapter = CurrentTaskAdapterNew(requireContext())
        rvCurrentTasks.adapter = adapter
    }
}