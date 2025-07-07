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

/**
 * RecyclerView adapter for displaying a list of Task items.
 * Uses ListAdapter for efficient diff-based updates.
 *
 * @param onToggleCompleted Callback invoked when a task's completion checkbox is toggled.
 */
class TasksAdapter(
    private val onToggleCompleted: (Task) -> Unit
) : ListAdapter<Task, TasksAdapter.TaskViewHolder>(TaskDiffCallback()) {

    /**
     * Inflate the item_task layout and wrap it in a ViewHolder.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_task, parent, false)
        return TaskViewHolder(view, onToggleCompleted)
    }

    /**
     * Bind the Task at the given position to the ViewHolder.
     */
    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    /**
     * ViewHolder class for task items.
     * Holds references to views and binds Task data.
     */
    class TaskViewHolder(
        itemView: View,
        private val onToggle: (Task) -> Unit
    ) : RecyclerView.ViewHolder(itemView) {
        private val checkBox: CheckBox = itemView.findViewById(R.id.cb_complete)
        private val titleView: TextView = itemView.findViewById(R.id.tv_task_title)
        private val subtitleView: TextView = itemView.findViewById(R.id.tv_task_subtitle)

        /**
         * Bind a Task object's data to UI elements.
         * Sets title text, updates checkbox state,
         * calculates remaining time until dueAt and displays it,
         * wire up checkbox click to toggle completion
         */
        fun bind(task: Task) {
            // Display the task title
            titleView.text = task.title
            // Reflect completion state
            checkBox.isChecked = task.isCompleted

            // Compute remaining milliseconds until dueAt (never negative)
            val now    = System.currentTimeMillis()
            val diffMs = (task.dueAt - now).coerceAtLeast(0L)

            // Convert to hours and minutes
            val totalMins = diffMs / 60_000       // total remaining minutes
            val hoursLeft = (diffMs / 3_600_000L).toInt()
            val minsLeft  = (totalMins % 60).toInt()

            // Choose the correct pluralized string
            val ctx = itemView.context
            subtitleView.text = if (hoursLeft > 0) {
                // Show "Due in X hours"
                ctx.resources.getQuantityString(
                    R.plurals.due_in_hours,
                    hoursLeft,
                    hoursLeft
                )
            } else {
                // Show "Due in Y minutes"
                ctx.resources.getQuantityString(
                    R.plurals.due_in_minutes,
                    minsLeft,
                    minsLeft
                )
            }

            // Notify ViewModel/Fragment when user toggles completion
            checkBox.setOnClickListener { onToggle(task) }
        }
    }
}

/**
 * DiffUtil callback for Task items.
 * Allows ListAdapter to determine which items changed.
 */
private class TaskDiffCallback : DiffUtil.ItemCallback<Task>() {
    // Items are the same if they have the same ID
    override fun areItemsTheSame(old: Task, new: Task) = old.id == new.id
    // Contents are the same if all properties match
    override fun areContentsTheSame(old: Task, new: Task) = old == new
}