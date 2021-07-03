package com.druger.refresher.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.druger.refresher.R
import com.druger.refresher.adapters.CurrentTaskAdapter
import com.druger.refresher.databinding.FragmentCurrentTaskBinding
import com.druger.refresher.viewmodel.TaskViewModel

class CurrentTasksFragment: Fragment() {

    private val taskModel: TaskViewModel by viewModels()

    private lateinit var adapter: CurrentTaskAdapter

    private var binding: FragmentCurrentTaskBinding? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentCurrentTaskBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecycler()
        setupViewModel()
        binding?.fabAddTask?.setOnClickListener { showAddingTaskFragment() }
    }

    override fun onDestroyView() {
        binding = null
        super.onDestroyView()
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
        adapter = CurrentTaskAdapter()
        binding?.rvCurrentTasks?.adapter = adapter
    }
}