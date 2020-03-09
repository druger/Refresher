package com.druger.refresher.adapters

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.druger.refresher.ui.fragments.TaskFragment
import com.druger.refresher.models.Item
import com.druger.refresher.models.ModelSeparator
import com.druger.refresher.models.ModelTask
import de.hdodenhof.circleimageview.CircleImageView

/**
* Created by druger on 19.09.2015.
*/
abstract class TaskAdapter(val taskFragment: TaskFragment) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var items: MutableList<Item>

    init {
        items = mutableListOf()
    }

    private var containsSeparatorOverdue = false
    private var containsSeparatorToday = false
    private var containsSeparatorTomorrow = false
    private var containsSeparatorFuture = false

    fun getItem(position: Int): Item {
        return items[position]
    }

    fun addItem(item: Item) {
        items.add(item)
        notifyItemInserted(itemCount - 1)
    }

    fun addItem(location: Int, item: Item) {
        items.add(location, item)
        notifyItemInserted(location)
    }

    fun updateTask(newTask: ModelTask) {
        for (i in 0..itemCount) {
            if (getItem(i).isTask()) {
                val task = getItem(i) as ModelTask
                if (newTask.timeStamp == task.timeStamp) {
                    removeItem(i)
                    taskFragment.addTask(newTask, false)
                }
            }
        }
    }

    fun removeItem(location: Int) {
        if (location >= 0 && location < itemCount - 1) {
            items.removeAt(location)
            notifyItemRemoved(location)
            if (location - 1 >= 0 && location <= itemCount - 1) {
                if (!getItem(location).isTask() && !getItem(location - 1).isTask()) {
                    val separator = getItem(location - 1) as ModelSeparator
                    checkSeparators(separator.type)
                    items.removeAt(location - 1)
                    notifyItemRemoved(location - 1)
                }
            } else if (itemCount - 1 >= 0 && !getItem(itemCount - 1).isTask()) {
                val separator = getItem(itemCount - 1) as ModelSeparator
                checkSeparators(separator.type)

                val locationTemp = itemCount - 1
                items.removeAt(locationTemp)
                notifyItemRemoved(locationTemp)
            }
        }
    }

    private fun checkSeparators(type: Int) {
        when (type) {
            ModelSeparator.TYPE_OVERDUE -> containsSeparatorOverdue = false

            ModelSeparator.TYPE_TODAY -> containsSeparatorToday = false

            ModelSeparator.TYPE_TOMORROW -> containsSeparatorTomorrow = false

            ModelSeparator.TYPE_FUTURE -> containsSeparatorFuture = false
        }
    }

    fun removeAllItems() {
        if (itemCount != 0) {
            items = mutableListOf()
            notifyDataSetChanged()
            containsSeparatorOverdue = false
            containsSeparatorToday = false
            containsSeparatorTomorrow = false
            containsSeparatorFuture = false
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun isContainsSeparatorOverdue(): Boolean {
        return containsSeparatorOverdue
    }

    fun setContainsSeparatorOverdue(containsSeparatorOverdue: Boolean) {
        this.containsSeparatorOverdue = containsSeparatorOverdue
    }

    fun isContainsSeparatorToday(): Boolean {
        return containsSeparatorToday
    }

    fun setContainsSeparatorToday(containsSeparatorToday: Boolean) {
        this.containsSeparatorToday = containsSeparatorToday
    }

    fun isContainsSeparatorTomorrow(): Boolean {
        return containsSeparatorTomorrow
    }

    fun setContainsSeparatorTomorrow(containsSeparatorTomorrow: Boolean) {
        this.containsSeparatorTomorrow = containsSeparatorTomorrow
    }

    fun isContainsSeparatorFuture(): Boolean {
        return containsSeparatorFuture
    }

    fun setContainsSeparatorFuture(containsSeparatorFuture: Boolean) {
        this.containsSeparatorFuture = containsSeparatorFuture
    }

    protected class TaskViewHolder(itemView: View,
                                   val title: TextView,
                                   val date: TextView,
                                   val priority: CircleImageView) :
            RecyclerView.ViewHolder(itemView)


    protected class SeparatorViewHolder(itemView: View, val type: TextView) : RecyclerView.ViewHolder(itemView)
}
