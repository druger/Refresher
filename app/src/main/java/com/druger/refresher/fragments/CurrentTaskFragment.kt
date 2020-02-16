package com.druger.refresher.fragments


import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.druger.refresher.R
import com.druger.refresher.adapters.CurrentTasksAdapter
import com.druger.refresher.models.ModelSeparator
import com.druger.refresher.models.ModelTask
import kotlinx.android.synthetic.main.fragment_current_task.*
import java.util.*
import kotlin.collections.ArrayList


class CurrentTaskFragment : TaskFragment() {

    private lateinit var onTaskDoneListener: OnTaskDoneListener

    interface OnTaskDoneListener {
        fun onTaskDone(task: ModelTask)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
//            onTaskDoneListener = activity
        } catch (e: ClassCastException) {
            throw ClassCastException(activity.toString()
                    + " must implement OnTaskDoneListener")
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_current_task, container, false)
//        setupRecycler()
        return rootView
    }

    private fun setupRecycler() {
        tasksAdapter = CurrentTasksAdapter(this)
        rvCurrentTasks.adapter = tasksAdapter
    }

    override fun addTask(newTask: ModelTask, saveToDB: Boolean) {
        var position = -1
        var separator: ModelSeparator? = null

        for (i in 0..tasksAdapter.itemCount) {
            if (tasksAdapter.getItem(i).isTask()) {
                val task = tasksAdapter.getItem(i) as ModelTask
                if (newTask.date < task.date) {
                    position = i
                    break
                }
            }
        }

        if (newTask.date.toInt() != 0) {
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = newTask.date

            if (calendar.get(Calendar.DAY_OF_YEAR) < Calendar.getInstance().get(Calendar.DAY_OF_YEAR)) {
                newTask.dateStatus = ModelSeparator.TYPE_OVERDUE
                if (!tasksAdapter.isContainsSeparatorOverdue()) {
                    tasksAdapter.setContainsSeparatorOverdue(true)
                    separator = ModelSeparator(ModelSeparator.TYPE_OVERDUE)
                }
            } else if (calendar.get(Calendar.DAY_OF_YEAR) == Calendar.getInstance().get(Calendar.DAY_OF_YEAR)) {
                newTask.dateStatus = ModelSeparator.TYPE_TODAY
                if (!tasksAdapter.isContainsSeparatorToday()) {
                    tasksAdapter.setContainsSeparatorToday(true)
                    separator = ModelSeparator(ModelSeparator.TYPE_TODAY)
                }
            } else if (calendar.get(Calendar.DAY_OF_YEAR) == Calendar.getInstance().get(Calendar.DAY_OF_YEAR) + 1) {
                newTask.dateStatus = ModelSeparator.TYPE_TOMORROW
                if (!tasksAdapter.isContainsSeparatorTomorrow()) {
                    tasksAdapter.setContainsSeparatorTomorrow(true)
                    separator = ModelSeparator(ModelSeparator.TYPE_TOMORROW)
                }
            } else if (calendar.get(Calendar.DAY_OF_YEAR) > Calendar.getInstance().get(Calendar.DAY_OF_YEAR) + 1) {
                newTask.dateStatus = ModelSeparator.TYPE_FUTURE
                if (!tasksAdapter.isContainsSeparatorFuture()) {
                    tasksAdapter.setContainsSeparatorFuture(true)
                    separator = ModelSeparator(ModelSeparator.TYPE_FUTURE)
                }
            }
        }

        if (position != -1) {
            if (!tasksAdapter.getItem(position - 1).isTask()) {
                if (position - 2 >= 0 && tasksAdapter.getItem(position - 2).isTask()) {
                    val task = tasksAdapter.getItem(position - 2) as ModelTask
                    if (task.dateStatus == newTask.dateStatus) {
                        position -= 1
                    }
                } else if (position - 2 < 0 && newTask.date.toInt() == 0) {
                    position -= 1
                }
            }

            tasksAdapter.addItem(position - 1, separator!!)

            tasksAdapter.addItem(position, newTask)
        } else {
            tasksAdapter.addItem(separator!!)
            tasksAdapter.addItem(newTask)
        }

        if (saveToDB) {
//            activity.realmHelper.saveTask(newTask)
        }
    }

    override fun findTasks(title: String) {
        tasksAdapter.removeAllItems()
        val tasks: MutableList<ModelTask> = ArrayList()
//        tasks.addAll(activity.realmHelper.getTasksByTitleAndAnyStatus(title))
        for (task in tasks) {
            addTask(task, false)
        }
    }

    override fun checkAdapter() {
        tasksAdapter = CurrentTasksAdapter(this)
        addTaskFromDB()
    }

    override fun addTaskFromDB() {
        tasksAdapter.removeAllItems()
        val tasks: MutableList<ModelTask> = ArrayList()
//        tasks.addAll(activity.realmHelper.getTasksByAnyStatus())
        for (task in tasks) {
            addTask(task, false)
        }
    }

    override fun moveTask(task: ModelTask) {
        alarmHelper.removeAlarm(task.timeStamp)
        onTaskDoneListener.onTaskDone(task)
    }
}
