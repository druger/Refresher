package com.druger.refresher.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.druger.refresher.R
import com.druger.refresher.adapters.TaskAdapter
import com.druger.refresher.databinding.FragmentCurrentTaskBinding
import com.druger.refresher.db.entity.Task
import com.druger.refresher.models.ModelTask
import com.druger.refresher.viewmodel.TaskViewModel
import kotlinx.coroutines.launch

class CurrentTasksFragment: Fragment() {

    private val viewModel: TaskViewModel by viewModels()

    private lateinit var adapter: TaskAdapter

    private var binding: FragmentCurrentTaskBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.getCurrentTasks()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentCurrentTaskBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecycler()
        binding?.fabAddTask?.setOnClickListener { showAddingTaskFragment() }
    }

    override fun onDestroyView() {
        binding = null
        super.onDestroyView()
    }

    private fun showAddingTaskFragment() {
        findNavController().navigate(R.id.goToAddingTask)
    }

    private fun setupRecycler() {
        adapter = TaskAdapter { moveTaskToDone(it) }
        binding?.rvCurrentTasks?.adapter = adapter
        viewModel.tasks.observe(viewLifecycleOwner) { adapter.submitList(it) }
    }

    private fun moveTaskToDone(task: ModelTask) {
        lifecycleScope.launch {
            viewModel.updateTask(task.copy(status = Task.STATUS_DONE))
        }
    }
}