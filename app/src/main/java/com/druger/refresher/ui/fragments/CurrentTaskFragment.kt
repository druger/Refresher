package com.druger.refresher.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.druger.refresher.R
import com.druger.refresher.adapters.CurrentTaskAdapter
import com.druger.refresher.models.ModelTaskNew
import com.druger.refresher.viewmodel.TaskViewModel
import kotlinx.android.synthetic.main.fragment_current_task.*

class CurrentTaskFragment: Fragment(R.layout.fragment_current_task) {

    private val taskModel: TaskViewModel by viewModels()

    lateinit var adapter: CurrentTaskAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecycler()
        setupViewModel()
    }

    private fun setupViewModel() {
        taskModel.getTasks().observe(viewLifecycleOwner, Observer<List<ModelTaskNew>> { tasks ->
            adapter.addTasks(tasks)
        })
    }

    private fun setupRecycler() {
        adapter = CurrentTaskAdapter(requireContext())
        rvCurrentTasks.adapter = adapter
    }
}