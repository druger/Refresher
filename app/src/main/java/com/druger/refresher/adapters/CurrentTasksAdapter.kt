package com.druger.refresher.adapters

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.druger.refresher.R
import com.druger.refresher.ui.fragments.CurrentTaskFragment
import com.druger.refresher.models.ModelSeparator
import com.druger.refresher.models.ModelTask
import com.druger.refresher.utils.DateHelper
import de.hdodenhof.circleimageview.CircleImageView
import java.util.*

/**
* Created by druger on 16.09.2015.
*/
class CurrentTasksAdapter(taskFragment: CurrentTaskFragment) : TaskAdapter(taskFragment) {

    companion object {
        private const val TYPE_TASK = 0
        private const val TYPE_SEPARATOR = 1
    }

    private lateinit var viewHolder: RecyclerView.ViewHolder

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        when (viewType) {
            TYPE_TASK -> {
                val view = LayoutInflater.from(parent.context)
                        .inflate(R.layout.model_task, parent, false)
                val title: TextView = view.findViewById(R.id.tvTaskTitle)
                val date: TextView = view.findViewById(R.id.tvTaskDate)
                val priority: CircleImageView = view.findViewById(R.id.cvTaskPriority)

                viewHolder = TaskViewHolder (view, title, date, priority)
                return viewHolder
            }
            TYPE_SEPARATOR -> {
                val separator = LayoutInflater.from (parent.context)
                        .inflate(R.layout.model_separator, parent, false)
                val type: TextView = separator.findViewById (R.id.tvSeparatorName)
                viewHolder = SeparatorViewHolder (separator, type)
                return viewHolder
            }
            else -> return viewHolder;
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = items[position]

        if (item.isTask()) {
            holder.itemView.isEnabled = true
            val task = item as ModelTask
            val taskViewHolder = holder as TaskViewHolder

            val itemView = taskViewHolder.itemView

            taskViewHolder.title.text = task.title
            if (task.date != 0L) {
                taskViewHolder.date.text = DateHelper.getFullDate(task.date)
            } else {
                taskViewHolder.date.text = null
            }

            itemView.visibility = View.VISIBLE
            taskViewHolder.priority.isEnabled = true

            if (task.date != 0L && task.date < Calendar.getInstance().timeInMillis) {
                itemView.setBackgroundColor(ContextCompat.getColor(taskFragment.requireContext(), R.color.gray_200))
            } else {
                itemView.setBackgroundColor(ContextCompat.getColor(taskFragment.requireContext(), R.color.gray_50))
            }

            taskViewHolder.title.setTextColor(ContextCompat.getColor(taskFragment.requireContext(), R.color.primary_text_light))
            taskViewHolder.date.setTextColor(ContextCompat.getColor(taskFragment.requireContext(), R.color.secondary_text_light))
            taskViewHolder.priority.setColorFilter(ContextCompat.getColor(taskFragment.requireContext(), task.getPriorityColor()))
            taskViewHolder.priority.setImageResource(R.drawable.ic_checkbox_blank_circle_white_48dp)

            itemView.setOnClickListener {taskFragment.showEditTaskDialog(task)}

            itemView.setOnLongClickListener{
                // for delay ripple animation
                val handler = Handler()
                handler.postDelayed({
                    taskFragment.removeTaskDialog(
                            taskViewHolder.layoutPosition)
                },1000)
            }

            taskViewHolder.priority.setOnClickListener {
                taskViewHolder.priority.isEnabled = false
                task.status = ModelTask.STATUS_DONE
//                taskFragment.activity.realmHelper.updateTask(task)

                taskViewHolder.title.setTextColor(ContextCompat.getColor(taskFragment.requireContext(), R.color.primary_text_light))
                taskViewHolder.date.setTextColor(ContextCompat.getColor(taskFragment.requireContext(), R.color.secondary_text_light))
                taskViewHolder.priority.setColorFilter(ContextCompat.getColor(taskFragment.requireContext(), task.getPriorityColor()))

                val flipIn: ObjectAnimator = ObjectAnimator.ofFloat(taskViewHolder.priority, "rotationY", -180f, 0f)

                flipIn.addListener(object : Animator.AnimatorListener {

                    override fun onAnimationStart(animation: Animator) {

                    }

                    override fun onAnimationEnd(animation: Animator) {
                        if (task.status == ModelTask.STATUS_DONE) {
                            taskViewHolder.priority.setImageResource(R.drawable.ic_check_circle_white_48dp)

                            val translationX: ObjectAnimator = ObjectAnimator.ofFloat(itemView,
                                "translationX", 0f, itemView.width.toFloat())

                            val translationXBack: ObjectAnimator = ObjectAnimator.ofFloat(itemView,
                                "translationX", itemView.width.toFloat(), 0f)

                            translationX.addListener(object : Animator.AnimatorListener {

                                override fun onAnimationStart(animation: Animator) {

                                }

                                override fun onAnimationEnd(animation: Animator) {
                                    itemView.visibility = View.GONE
                                    taskFragment.moveTask(task)
                                    removeItem(taskViewHolder.layoutPosition)

                                }

                                override fun onAnimationCancel(animation: Animator) {

                                }

                                override fun onAnimationRepeat(animation: Animator) {

                                }
                            })

                            val translationSet = AnimatorSet()
                            translationSet.play(translationX).before(translationXBack)
                            translationSet.start()
                        }
                    }

                    override fun onAnimationCancel(animation: Animator) {

                    }

                    override fun onAnimationRepeat(animation: Animator) {

                    }
                })

                flipIn.start()
            }
        } else {
            val separator = item as ModelSeparator
            val separatorViewHolder = holder as SeparatorViewHolder

            val resources = holder.itemView.resources
            separatorViewHolder.type.text = resources?.getString(separator.type)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (getItem(position).isTask()) {
            TYPE_TASK
        } else {
            TYPE_SEPARATOR
        }
    }
}
