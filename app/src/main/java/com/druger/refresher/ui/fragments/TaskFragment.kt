package com.druger.refresher.ui.fragments

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.druger.refresher.App
import com.druger.refresher.R
import com.druger.refresher.ui.activities.MainActivityNew
import com.druger.refresher.adapters.TaskAdapter
import com.druger.refresher.alarms.AlarmHelper
import com.druger.refresher.ui.dialogs.EditTaskDialogFragment
import com.druger.refresher.models.ModelTask
import javax.inject.Inject

/**
 * Created by druger on 19.09.2015.
 */
abstract class TaskFragment : Fragment() {

    protected lateinit var tasksAdapter: TaskAdapter

    lateinit var activity: MainActivityNew

    @Inject
    lateinit var alarmHelper: AlarmHelper

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (getActivity() != null) {
            activity = getActivity() as MainActivityNew
        }
//        addTaskFromDB()
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

            dialogBuilder.setPositiveButton(R.string.dialog_ok) { dialog, _ ->
                run {

                    tasksAdapter.removeItem(location)
                    isRemoved = true
//                    showSnackbar(timeStamp, isRemoved)
                    dialog.dismiss()
                }
            }

            dialogBuilder.setNegativeButton(R.string.dialog_cancel) { dialog, _ -> dialog.cancel() }
        }

        dialogBuilder.show()
    }

//    private fun showSnackbar(timeStamp: Long, isRemoved: Boolean) {
//        var removed = isRemoved
//        Snackbar.make(activity.coordinator, R.string.removed, Snackbar.LENGTH_LONG).apply {
//            setAction(R.string.dialog_cancel) {
//                addTask(activity.realmHelper.getTaskByTimestamp(timeStamp)!!, false)
//                removed = false
//            }
//            view.addOnAttachStateChangeListener(object : View.OnAttachStateChangeListener {
//
//                override fun onViewAttachedToWindow(v: View) {}
//
//                override fun onViewDetachedFromWindow(v: View) {
//                    if (removed) {
//                        alarmHelper.removeAlarm(timeStamp)
//                        activity.realmHelper.removeTaskByTimestamp(timeStamp)
//                    }
//                }
//            })
//            show()
//        }
//    }

    fun showEditTaskDialog(task: ModelTask) {
        val editingTaskDialog = EditTaskDialogFragment.newInstance(task)
        fragmentManager?.let { editingTaskDialog.show(it, "EditTaskDialogFragment") }
    }

    abstract fun findTasks(title: String)

    abstract fun checkAdapter()

    abstract fun addTaskFromDB()

    abstract fun moveTask(task: ModelTask)
}
