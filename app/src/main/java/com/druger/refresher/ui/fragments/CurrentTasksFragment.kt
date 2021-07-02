package com.druger.refresher.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.druger.refresher.R
import com.druger.refresher.adapters.CurrentTaskAdapter
import com.druger.refresher.viewmodel.TaskViewModel
import kotlinx.android.synthetic.main.fragment_current_task.*

class CurrentTasksFragment: Fragment(R.layout.fragment_current_task) {

    private val taskModel: TaskViewModel by viewModels()

    lateinit var adapter: CurrentTaskAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecycler()
        setupViewModel()
        fabAddTask.setOnClickListener { showAddingTaskFragment() }
    }

    private fun showAddingTaskFragment() {
        findNavController().navigate(R.id.goToAddingTask)
    }

    private fun setupViewModel() {
        taskModel.getTasks().observe(viewLifecycleOwner, { tasks ->
            adapter.addTasks(tasks)
        })
    }

    private fun setupRecycler() {
        adapter = CurrentTaskAdapter(requireContext())
        rvCurrentTasks.adapter = adapter
    }
}