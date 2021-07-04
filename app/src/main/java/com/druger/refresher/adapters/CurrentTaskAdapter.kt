package com.druger.refresher.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.druger.refresher.databinding.TaskItemBinding
import com.druger.refresher.models.ModelTask
import com.druger.refresher.utils.DateHelper

class CurrentTaskAdapter :
    ListAdapter<ModelTask, CurrentTaskAdapter.CurrentTaskViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurrentTaskViewHolder {
        val binding = TaskItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CurrentTaskViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CurrentTaskViewHolder, position: Int) {
        val task = getItem(position)
        with(holder) {
            title.text = task.title
            val reminderDate = task.reminderDate
            if (reminderDate != 0L) {
                date.text = DateHelper.getFullDate(reminderDate)
            }
        }
    }

    inner class CurrentTaskViewHolder(binding: TaskItemBinding) : RecyclerView.ViewHolder(binding.root) {
        val title = binding.tvTaskTitle
        val date = binding.tvTaskDate
    }

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<ModelTask>() {
            override fun areItemsTheSame(oldItem: ModelTask, newItem: ModelTask): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: ModelTask, newItem: ModelTask): Boolean {
                return oldItem == newItem
            }
        }
    }
}