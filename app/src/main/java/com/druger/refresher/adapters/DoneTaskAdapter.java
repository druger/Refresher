package com.druger.refresher.adapters;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.res.Resources;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.druger.refresher.R;
import com.druger.refresher.fragments.DoneTaskFragment;
import com.druger.refresher.models.Item;
import com.druger.refresher.models.ModelTask;
import com.druger.refresher.utils.DateHelper;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by druger on 19.09.2015.
 */
public class DoneTaskAdapter extends TaskAdapter {

    public DoneTaskAdapter(DoneTaskFragment  taskFragment) {
        super(taskFragment);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.model_task, viewGroup, false);
        TextView title = (TextView) view.findViewById(R.id.tvTaskTitle);
        TextView date = (TextView) view.findViewById(R.id.tvTaskDate);
        CircleImageView priority = (CircleImageView) view.findViewById(R.id.cvTaskPriority);

        return new TaskAdapter.TaskViewHolder(view, title, date, priority);

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {

        Item item = items.get(position);

        if (item.isTask()){
            viewHolder.itemView.setEnabled(true);
            final ModelTask task = (ModelTask) item;
            final TaskAdapter.TaskViewHolder taskViewHolder = (TaskAdapter.TaskViewHolder) viewHolder;

            final View itemView = taskViewHolder.itemView;
            final Resources resources = itemView.getResources();

            taskViewHolder.title.setText(task.getTitle());
            if (task.getDate() != 0){
                taskViewHolder.date.setText(DateHelper.getFullDate(task.getDate()));
            } else {
                taskViewHolder.date.setText(null);
            }

            itemView.setVisibility(View.VISIBLE);
            taskViewHolder.priority.setEnabled(true);

            taskViewHolder.title.setTextColor(ContextCompat.getColor(taskFragment.getActivity(), R.color.primary_text_light));
            taskViewHolder.date.setTextColor(ContextCompat.getColor(taskFragment.getActivity(), R.color.secondary_text_light));
            taskViewHolder.priority.setColorFilter(ContextCompat.getColor(taskFragment.getActivity(), task.getPriorityColor()));
            taskViewHolder.priority.setImageResource(R.drawable.ic_check_circle_white_48dp);

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    // for delay ripple animation
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            getTaskFragment().removeTaskDialog(
                                    taskViewHolder.getLayoutPosition());
                        }
                    }, 1000);

                    return true;
                }
            });

            taskViewHolder.priority.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    taskViewHolder.priority.setEnabled(false);
                    task.setStatus(ModelTask.STATUS_CURRENT);
                    getTaskFragment().activity.dbHelper.update().
                            updateStatus(task.getTimeStamp(), ModelTask.STATUS_CURRENT);

                    taskViewHolder.title.setTextColor(ContextCompat.getColor(taskFragment.getActivity(), R.color.primary_text_light));
                    taskViewHolder.date.setTextColor(ContextCompat.getColor(taskFragment.getActivity(), R.color.secondary_text_light));
                    taskViewHolder.priority.setColorFilter(ContextCompat.getColor(taskFragment.getActivity(), task.getPriorityColor()));

                    ObjectAnimator flipIn = ObjectAnimator.ofFloat(taskViewHolder.priority, "rotationY", 180f, 0f);
                    taskViewHolder.priority.setImageResource(R.drawable.ic_check_circle_white_48dp);

                    flipIn.addListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {
                            if (task.getStatus() != ModelTask.STATUS_DONE){

                                ObjectAnimator translationX = ObjectAnimator.ofFloat(itemView,
                                        "translationX", 0f, -itemView.getWidth());

                                ObjectAnimator translationXBack = ObjectAnimator.ofFloat(itemView,
                                        "translationX", -itemView.getWidth(), 0f);

                                translationX.addListener(new Animator.AnimatorListener() {
                                    @Override
                                    public void onAnimationStart(Animator animation) {

                                    }

                                    @Override
                                    public void onAnimationEnd(Animator animation) {
                                        itemView.setVisibility(View.GONE);
                                        getTaskFragment().moveTask(task);
                                        removeItem(taskViewHolder.getLayoutPosition());

                                    }

                                    @Override
                                    public void onAnimationCancel(Animator animation) {

                                    }

                                    @Override
                                    public void onAnimationRepeat(Animator animation) {

                                    }
                                });

                                AnimatorSet translationSet = new AnimatorSet();
                                translationSet.play(translationX).before(translationXBack);
                                translationSet.start();
                            }
                        }

                        @Override
                        public void onAnimationCancel(Animator animation) {

                        }

                        @Override
                        public void onAnimationRepeat(Animator animation) {

                        }
                    });

                    flipIn.start();
                }
            });
        }
    }

}
