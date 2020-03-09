package com.druger.refresher.ui.fragments


import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.druger.refresher.R
import com.druger.refresher.adapters.DoneTaskAdapter
import com.druger.refresher.models.ModelTask
import kotlinx.android.synthetic.main.fragment_done_task.*


/**
 * A simple {@link Fragment} subclass.
 */
class DoneTaskFragment : TaskFragment() {

    private lateinit var onTaskRestoreListener: OnTaskRestoreListener

    interface OnTaskRestoreListener {
        fun onTaskRestore(task: ModelTask)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
//            onTaskRestoreListener = activity
        } catch (e: ClassCastException) {
            throw ClassCastException(activity.toString()
                    + " must implement OnTaskRestoreListener")
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_done_task, container, false)
//        setupRecycler()
        return rootView
    }

    private fun setupRecycler() {
        tasksAdapter = DoneTaskAdapter(this)
        rvDoneTasks.adapter = tasksAdapter
    }

    override fun addTask(newTask: ModelTask, saveToDB: Boolean) {
        var position = -1

        for (i in 0..tasksAdapter.itemCount) {
            if (tasksAdapter.getItem(i).isTask()) {
                val task = tasksAdapter.getItem(i) as ModelTask
                if (newTask.date < task.date) {
                    position = i
                    break
                }
            }
        }

        if (position != -1) {
            tasksAdapter.addItem(position, newTask)
        } else {
            tasksAdapter.addItem(newTask)
        }

        if (saveToDB) {
//            activity.realmHelper.saveTask(newTask)
        }
    }

    override fun findTasks(title: String) {
        tasksAdapter.removeAllItems()
        val tasks: MutableList<ModelTask> = ArrayList()
//        tasks.addAll(activity.realmHelper.getTasksByTitleAndDoneStatus(title))
        for (i in 0..tasks.size) {
            addTask(tasks.get(i), false)
        }
    }

    override fun checkAdapter() {
        tasksAdapter = DoneTaskAdapter(this)
        addTaskFromDB()
    }

    override fun addTaskFromDB() {
        tasksAdapter.removeAllItems()
        val tasks: MutableList<ModelTask> = ArrayList()
//        tasks.addAll(activity.realmHelper.getTasksByDoneStatus())
        for (i in 0..tasks.size) {
            addTask(tasks.get(i), false)
        }
    }

    override fun moveTask(task: ModelTask) {
        if (task.date.toInt() != 0) {
            alarmHelper.setAlarm(task)
        }
        onTaskRestoreListener.onTaskRestore(task)
    }
}
