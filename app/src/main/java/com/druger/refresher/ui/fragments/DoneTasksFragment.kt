package com.druger.refresher.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.druger.refresher.adapters.TaskAdapter
import com.druger.refresher.databinding.FragmentDoneTaskBinding
import com.druger.refresher.db.entity.Task
import com.druger.refresher.models.ModelTask
import com.druger.refresher.viewmodel.TaskViewModel
import kotlinx.coroutines.launch

class DoneTasksFragment: Fragment() {

    private val viewModel: TaskViewModel by viewModels()

    private var binding: FragmentDoneTaskBinding? = null

    private lateinit var adapter: TaskAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.getDoneTasks()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentDoneTaskBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecycler()
    }

    private fun setupRecycler() {
        adapter = TaskAdapter { moveTaskToCurrent(it) }
        binding?.rvDoneTasks?.adapter = adapter
        viewModel.tasks.observe(viewLifecycleOwner) { adapter.submitList(it) }
    }

    private fun moveTaskToCurrent(task: ModelTask) {
        lifecycleScope.launch {
            viewModel.updateTask(task.copy(status = Task.STATUS_CURRENT))
        }
    }

    override fun onDestroyView() {
        binding = null
        super.onDestroyView()
    }
}