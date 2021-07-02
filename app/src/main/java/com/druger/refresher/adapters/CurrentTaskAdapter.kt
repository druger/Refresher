package com.druger.refresher.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.druger.refresher.R
import com.druger.refresher.models.ModelTaskNew
import com.druger.refresher.utils.DateHelper

class CurrentTaskAdapter(val context: Context):
    RecyclerView.Adapter<CurrentTaskAdapter.CurrentTaskViewHolder>() {

    private var tasks = emptyList<ModelTaskNew>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurrentTaskViewHolder {
        val itemView = LayoutInflater.from(context).inflate(R.layout.task_item, parent, false)
        return CurrentTaskViewHolder(itemView)
    }

    override fun getItemCount() = tasks.size

    override fun onBindViewHolder(holder: CurrentTaskViewHolder, position: Int) {
        val task = tasks[position]
        holder.title.text = task.title
        holder.date.text = DateHelper.getFullDate(task.reminderDate)
    }

    inner class CurrentTaskViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.tvTaskTitle)
        val date: TextView = itemView.findViewById(R.id.tvTaskDate)
    }

    fun addTasks(tasks: List<ModelTaskNew>) {
        this.tasks = tasks
        notifyDataSetChanged()
    }
}