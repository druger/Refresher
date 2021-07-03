package com.druger.refresher.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.druger.refresher.databinding.TaskItemBinding
import com.druger.refresher.models.ModelTask
import com.druger.refresher.utils.DateHelper

class CurrentTaskAdapter :
    RecyclerView.Adapter<CurrentTaskAdapter.CurrentTaskViewHolder>() {

    private var tasks = emptyList<ModelTask>()

    private lateinit var binding: TaskItemBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurrentTaskViewHolder {
        binding = TaskItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CurrentTaskViewHolder(binding)
    }

    override fun getItemCount() = tasks.size

    override fun onBindViewHolder(holder: CurrentTaskViewHolder, position: Int) {
        val task = tasks[position]
        with(holder) {
            title.text = task.title
            date.text = DateHelper.getFullDate(task.reminderDate)
        }
    }

    inner class CurrentTaskViewHolder(binding: TaskItemBinding) : RecyclerView.ViewHolder(binding.root) {
        val title = binding.tvTaskTitle
        val date = binding.tvTaskDate
    }

    fun addTasks(tasks: List<ModelTask>) {
        this.tasks = tasks
        notifyDataSetChanged()
    }
}