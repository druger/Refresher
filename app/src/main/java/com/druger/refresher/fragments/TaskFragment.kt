package com.druger.refresher.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.RecyclerView
import android.view.View
import com.druger.refresher.App
import com.druger.refresher.R
import com.druger.refresher.activities.MainActivity
import com.druger.refresher.adapters.TaskAdapter
import com.druger.refresher.alarms.AlarmHelper
import com.druger.refresher.dialogs.EditTaskDialogFragment
import com.druger.refresher.models.ModelTask
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.design.longSnackbar
import javax.inject.Inject

/**
 * Created by druger on 19.09.2015.
 */
abstract class TaskFragment : Fragment() {

    protected lateinit var recyclerView: RecyclerView
    protected lateinit var layoutManager: RecyclerView.LayoutManager

    protected lateinit var tasksAdapter: TaskAdapter

    lateinit var activity: MainActivity

    @Inject
    lateinit var alarmHelper: AlarmHelper

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (getActivity() != null) {
            activity = getActivity() as MainActivity
        }
        addTaskFromDB()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        App.getApplicationComponent().inject(this)
    }

    abstract fun addTask(newTask: ModelTask, saveToDB: Boolean)

    fun updateTask(task: ModelTask) {
        tasksAdapter.updateTask(task)
    }

    fun removeTaskDialog(location: Int) {
        val dialogBuilder = AlertDialog.Builder(getActivity())

        dialogBuilder.setMessage(R.string.dialog_removing_message)

        val item = tasksAdapter.getItem(location)

        if (item.isTask()) {
            val removingTask = item as ModelTask

            val timeStamp = removingTask.timeStamp
            var isRemoved: Boolean

            dialogBuilder.setPositiveButton(R.string.dialog_ok, { dialog, _ ->
                run {

                    tasksAdapter.removeItem(location)
                    isRemoved = true
                    showSnackbar(timeStamp, isRemoved)
                    dialog.dismiss()
                }
            })

            dialogBuilder.setNegativeButton(R.string.dialog_cancel, { dialog, _ -> dialog.cancel() })
        }

        dialogBuilder.show()
    }

    private fun showSnackbar(timeStamp: Long, isRemoved: Boolean) {
        var removed = isRemoved
        val snackbar = longSnackbar(activity.coordinator, R.string.removed, R.string.dialog_cancel) {
            addTask(activity.realmHelper.getTaskByTimestamp(timeStamp)!!, false)
            removed = false
        }
        snackbar.view.addOnAttachStateChangeListener(object : View.OnAttachStateChangeListener {

            override fun onViewAttachedToWindow(v: View) {

            }

            override fun onViewDetachedFromWindow(v: View) {
                if (removed) {
                    alarmHelper.removeAlarm(timeStamp)
                    activity.realmHelper.removeTaskByTimestamp(timeStamp)
                }
            }
        })
    }

    fun showEditTaskDialog(task: ModelTask) {
        val editingTaskDialog = EditTaskDialogFragment.newInstance(task)
        editingTaskDialog.show(getActivity()?.fragmentManager, "EditTaskDialogFragment")
    }

    abstract fun findTasks(title: String)

    abstract fun checkAdapter()

    abstract fun addTaskFromDB()

    abstract fun moveTask(task: ModelTask)
}
