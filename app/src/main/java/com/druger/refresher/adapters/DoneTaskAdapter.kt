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
import com.druger.refresher.fragments.DoneTaskFragment
import com.druger.refresher.models.ModelTask
import com.druger.refresher.utils.DateHelper
import de.hdodenhof.circleimageview.CircleImageView

/**
* Created by druger on 19.09.2015.
*/
class DoneTaskAdapter(taskFragment: DoneTaskFragment) : TaskAdapter(taskFragment) {


    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        val view: View = LayoutInflater.from(viewGroup.context)
                .inflate(R.layout.model_task, viewGroup, false)
        val title: TextView = view.findViewById(R.id.tvTaskTitle)
        val date: TextView = view.findViewById(R.id.tvTaskDate)
        val priority: CircleImageView = view.findViewById(R.id.cvTaskPriority)

        return TaskViewHolder(view, title, date, priority)
    }

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {

        val item = items[position]

        if (item.isTask()) {
            viewHolder.itemView.isEnabled = true
            val task = item as ModelTask
            val taskViewHolder = viewHolder as TaskViewHolder

            val itemView = taskViewHolder.itemView

            taskViewHolder.title.text = task.title
            if (task.date != 0L) {
                taskViewHolder.date.text = DateHelper.getFullDate(task.date)
            } else {
                taskViewHolder.date.text = null
            }

            itemView.visibility = View.VISIBLE
            taskViewHolder.priority.isEnabled = true

            taskViewHolder.title.setTextColor(ContextCompat.getColor(taskFragment.requireContext(), R.color.primary_text_light))
            taskViewHolder.date.setTextColor(ContextCompat.getColor(taskFragment.requireContext(), R.color.secondary_text_light))
            taskViewHolder.priority.setColorFilter(ContextCompat.getColor(taskFragment.requireContext(), task.getPriorityColor()))
            taskViewHolder.priority.setImageResource(R.drawable.ic_check_circle_white_48dp)

            itemView.setOnLongClickListener {
                // for delay ripple animation
                val handler = Handler()
                handler.postDelayed({
                    taskFragment.removeTaskDialog(
                        taskViewHolder.layoutPosition)
                }, 1000)
            }

            taskViewHolder.priority.setOnClickListener {
                taskViewHolder.priority.isEnabled = false
                task.status = ModelTask.STATUS_CURRENT
                taskFragment.activity.realmHelper.updateTask(task)

                taskViewHolder.title.setTextColor(ContextCompat.getColor(taskFragment.requireContext(), R.color.primary_text_light))
                taskViewHolder.date.setTextColor(ContextCompat.getColor(taskFragment.requireContext(), R.color.secondary_text_light))
                taskViewHolder.priority.setColorFilter(ContextCompat.getColor(taskFragment.requireContext(), task.getPriorityColor()))

                val flipIn: ObjectAnimator = ObjectAnimator.ofFloat(taskViewHolder.priority, "rotationY", 180f, 0f)
                taskViewHolder.priority.setImageResource(R.drawable.ic_check_circle_white_48dp)

                flipIn.addListener(object : Animator.AnimatorListener {

                    override fun onAnimationStart(animation: Animator) {

                    }

                    override fun onAnimationEnd(animation: Animator) {
                        if (task.status != ModelTask.STATUS_DONE) {

                            val translationX: ObjectAnimator = ObjectAnimator.ofFloat(itemView,
                                "translationX", 0f, -itemView.width.toFloat())

                            val translationXBack: ObjectAnimator = ObjectAnimator.ofFloat(itemView,
                                "translationX", -itemView.width.toFloat(), 0f)

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
        }
    }
}
