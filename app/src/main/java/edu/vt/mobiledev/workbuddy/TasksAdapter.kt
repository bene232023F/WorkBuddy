package edu.vt.mobiledev.workbuddy

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import edu.vt.mobiledev.workbuddy.data.Task

class TasksAdapter(
    private val onToggleCompleted: (Task) -> Unit
) : ListAdapter<Task, TasksAdapter.TaskViewHolder>(TaskDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_task, parent, false)
        return TaskViewHolder(view, onToggleCompleted)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class TaskViewHolder(
        itemView: View,
        private val onToggle: (Task) -> Unit
    ) : RecyclerView.ViewHolder(itemView) {
        private val checkBox: CheckBox = itemView.findViewById(R.id.cb_complete)
        private val titleView: TextView = itemView.findViewById(R.id.tv_task_title)
        private val subtitleView: TextView = itemView.findViewById(R.id.tv_task_subtitle)

        fun bind(task: Task) {
            titleView.text = task.title
            checkBox.isChecked = task.isCompleted

            val now    = System.currentTimeMillis()
            val diffMs = (task.dueAt - now).coerceAtLeast(0L)

            val totalMins = diffMs / 60_000       // total remaining minutes
            val hoursLeft = (diffMs / 3_600_000L).toInt()
            val minsLeft  = (totalMins % 60).toInt()

            val ctx = itemView.context
            subtitleView.text = if (hoursLeft > 0) {
                // "Due in X hours"
                ctx.resources.getQuantityString(
                    R.plurals.due_in_hours,
                    hoursLeft,
                    hoursLeft
                )
            } else {
                // "Due in Y minutes"
                ctx.resources.getQuantityString(
                    R.plurals.due_in_minutes,
                    minsLeft,
                    minsLeft
                )
            }

            checkBox.setOnClickListener { onToggle(task) }
        }
    }
}

private class TaskDiffCallback : DiffUtil.ItemCallback<Task>() {
    override fun areItemsTheSame(old: Task, new: Task) = old.id == new.id
    override fun areContentsTheSame(old: Task, new: Task) = old == new
}
